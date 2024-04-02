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

    public RequestService(RequestRepository requestRepository, ExchangeService exchangeService, UserService userService, BookService bookService) {
        this.requestRepository = requestRepository;
        this.exchangeService = exchangeService;
        this.userService = userService;
        this.bookService = bookService;
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

        if (request.getStatus() == RequestStatus.REJECTED) {
            log.error("Приянть заявку {} невозможно - она уже была отклонена!", requestId);
            return;
        }

        Book bookReceiverWants = bookService.getBookById(bookId);

        User sender = request.getSender();

        if (!sender.getOfferedBooks().contains(bookReceiverWants)) {
            throw new IllegalArgumentException("Отправитель заявки не предлагает данную книгу.");
        }

        List<Request> relatedRequests = requestRepository.findByStatusAndBookSenderWants(RequestStatus.ACTUAL, request.getBookSenderWants());

        for (Request relatedRequest : relatedRequests) {
            if (relatedRequest.getId() == requestId) {
                relatedRequest.setStatus(RequestStatus.ACCEPTED);
                relatedRequest.setBookReceiverWants(bookReceiverWants);
                Exchange exchange = new Exchange(relatedRequest.getSender(), relatedRequest.getReceiver(),
                        relatedRequest.getBookReceiverWants(), relatedRequest.getBookSenderWants(),
                        relatedRequest.getSender().getMainAddress(), relatedRequest.getReceiver().getMainAddress());
                exchangeService.createExchange(exchange);
                log.info("Заявка с id {} принята, создан обмен", requestId);
            } else {
                relatedRequest.setStatus(RequestStatus.REJECTED);
                log.info("Заявка с id {} отклонена, так как с запрашиваемой книгой принята к обмену другая заявка", relatedRequest.getId());
            }
            requestRepository.save(relatedRequest);
        }
    }


    /**
     * Отклоняет заявку с указанным идентификатором.
     *
     * @param requestId Идентификатор заявки
     */
    public void rejectRequest(long requestId) {
        Request request = getRequestById(requestId);
        request.setStatus(RequestStatus.REJECTED);
        requestRepository.save(request);
        log.info("Заявка с id {} отклонена", requestId);
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
