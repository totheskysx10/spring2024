package com.spring.vsurin.bookexchange.app;

import com.spring.vsurin.bookexchange.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Сервисный класс для работы с заявками на обмен.
 */
@Slf4j
@Component
public class RequestService {
    private final RequestRepository requestRepository;
    private final ExchangeService exchangeService;
    private final UserService userService;
    private final BookService bookService;
    private final MailBuilder mailBuilder;
    private final EmailService emailService;
    private final SecurityContextService securityContextService;

    public RequestService(RequestRepository requestRepository, ExchangeService exchangeService, UserService userService, BookService bookService, MailBuilder mailBuilder, EmailService emailService, SecurityContextService securityContextService) {
        this.requestRepository = requestRepository;
        this.exchangeService = exchangeService;
        this.userService = userService;
        this.bookService = bookService;
        this.mailBuilder = mailBuilder;
        this.emailService = emailService;
        this.securityContextService = securityContextService;
    }

    /**
     * Создает новую заявку и сохраняет ее в базе данных, с проверкой на null и на наличие желаемой книги у получателя заявки.
     *
     * @param request Заявка для создания
     * @return Созданная заявка
     * @throws IllegalArgumentException если переданная заявка равна null или у получателя отсутствует/недоступна книга, которую хочет отправитель
     * @throws RuntimeException         если возникла ошибка при создании заявки
     */
    public Request createRequest(Request request) {
        if (request == null) {
            throw new IllegalArgumentException("Заявка не может быть null");
        }

        long receiverId = request.getReceiver().getId();
        Book bookSenderWants = request.getBookSenderWants();
        if (!userService.getUserById(receiverId).getOfferedBooks().contains(bookSenderWants))
            throw new IllegalArgumentException("Заявка не может быть создана - у получателя заявки отсутствует/недоступна книга, которую хочет отправитель!");

        try {
            request.setStatus(RequestStatus.ACTUAL);
            requestRepository.save(request);
            log.info("Создана заявка с id {}", request.getId());

            EmailData emailData = mailBuilder.buildCreateRequestMessage(request.getReceiver().getEmail(), request.getId(), request.getBookSenderWants());
            emailService.sendEmail(emailData);
            return request;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании заявки", e);
        }
    }

    /**
     * Получает заявку по ее идентификатору.
     *
     * @param requestId Идентификатор заявки
     * @return Найденная заявка
     * @throws IllegalArgumentException если заявка с указанным идентификатором не найдена
     */
    public Request getRequestById(long requestId) {
        Request foundRequest = requestRepository.findById(requestId);
        if (foundRequest == null) {
            throw new IllegalArgumentException("Заявка с id " + requestId + " не найдена");
        }
        else {
            log.info("Найдена заявка с id {}", requestId);
            return foundRequest;
        }
    }

    /**
     * Принимает заявку с указанным идентификатором и создает обмен для нее, отклоняя все остальные заявки на эту книгу.
     *
     * @param requestId Идентификатор заявки
     * @param bookId    Идентификатор книги, которую хочет получатель заявки
     * @throws IllegalArgumentException если заявка не может быть принята
     */
    public void acceptRequest(long requestId, long bookId) {
        Request request = getRequestById(requestId);

        if (request != null) {
            if (request.getStatus() == RequestStatus.REJECTED) {
                log.warn("Приянть заявку {} невозможно - она уже была отклонена!", requestId);
                return;
            }

            Book bookReceiverWants = bookService.getBookById(bookId);

            User sender = request.getSender();
            User receiver = request.getReceiver();

            if (!sender.getOfferedBooks().contains(bookReceiverWants)) {
                throw new IllegalArgumentException("Отправитель заявки не предлагает данную книгу.");
            }

            List<Request> relatedRequests = requestRepository.findByStatusAndBookSenderWants(RequestStatus.ACTUAL, request.getBookSenderWants());

            userService.addUserWithAccessToMainAddress(sender.getId(), receiver.getId());
            userService.addUserWithAccessToMainAddress(receiver.getId(), sender.getId());

            Long currentAuthId = securityContextService.getCurrentAuthId();

            for (Request relatedRequest : relatedRequests) {
                if (relatedRequest.getId() == requestId) {
                    relatedRequest.setStatus(RequestStatus.ACCEPTED);
                    relatedRequest.setBookReceiverWants(bookReceiverWants);
                    requestRepository.save(relatedRequest);
                    Exchange exchange = Exchange.builder()
                            .member1(relatedRequest.getSender())
                            .member2(relatedRequest.getReceiver())
                            .exchangedBook1(relatedRequest.getBookReceiverWants())
                            .exchangedBook2(relatedRequest.getBookSenderWants())
                            .address1(relatedRequest.getSender().getMainAddress(currentAuthId))
                            .address2(relatedRequest.getReceiver().getMainAddress(currentAuthId))
                            .build();
                    exchangeService.createExchange(exchange);
                    log.info("Заявка с id {} принята, создан обмен", requestId);

                    EmailData emailData = mailBuilder.buildAcceptRequestMessage(relatedRequest.getSender().getEmail(), relatedRequest);
                    emailService.sendEmail(emailData);
                } else {
                    rejectRequest(relatedRequest.getId());
                    log.info("Заявка с id {} отклонена, так как с запрашиваемой книгой принята к обмену другая заявка", relatedRequest.getId());
                }
            }
        }
    }


    /**
     * Отклоняет заявку с указанным идентификатором.
     *
     * @param requestId Идентификатор заявки
     */
    public void rejectRequest(long requestId) {
        Request request = getRequestById(requestId);
        if (request != null) {
            request.setStatus(RequestStatus.REJECTED);
            requestRepository.save(request);
            log.info("Заявка с id {} отклонена", requestId);

            EmailData emailData = mailBuilder.buildRejectRequestMessage(request.getSender().getEmail(), request.getId());
            emailService.sendEmail(emailData);
        }
    }

    /**
     * Поиск заявок по статусу и отправителю заявки.
     *
     * @param status   Статус заявки
     * @param senderId Идентификатор отправителя
     * @param pageable Объект страницы и размера страницы для поиска
     * @return Страница найденных заявок
     */
    public Page<Request> findByStatusAndSender(RequestStatus status, long senderId, Pageable pageable) {
        return requestRepository.findByStatusAndSenderId(status, senderId, pageable);
    }

    /**
     * Поиск заявок по статусу и получателю заявки.
     *
     * @param status     Статус заявки
     * @param receiverId Идентификатор получателя
     * @param pageable   Объект страницы и размера страницы для поиска
     * @return Страница найденных заявок
     */
    public Page<Request> findByStatusAndReceiver(RequestStatus status, long receiverId, Pageable pageable) {
        return requestRepository.findByStatusAndReceiverId(status, receiverId, pageable);
    }

    /**
     * Обновляет комментарий заявке с указанным идентификатором.
     *
     * @param requestId идентификатор заявки
     * @param comment комментарий, который нужно установить
     */
    public void updateComment(long requestId, String comment) {
        Request request = getRequestById(requestId);
        if (request != null) {
            request.setCommentForReceiver(comment);
            requestRepository.save(request);
            log.info("Комментарий для получателя заявки обновлён для заявки с id {}", requestId);
        }
    }
}
