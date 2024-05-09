package com.spring.vsurin.bookexchange.app;

import com.spring.vsurin.bookexchange.domain.EmailData;
import com.spring.vsurin.bookexchange.domain.Exchange;
import com.spring.vsurin.bookexchange.domain.ExchangeStatus;
import com.spring.vsurin.bookexchange.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * Сервисный класс для работы с обменами.
 */
@Component
@Slf4j
public class ExchangeService {
    private final ExchangeRepository exchangeRepository;
    private final UserService userService;
    private final EmailService emailService;
    private final MailBuilder mailBuilder;

    public ExchangeService(ExchangeRepository exchangeRepository, UserService userService, EmailService emailService, MailBuilder mailBuilder) {
        this.exchangeRepository = exchangeRepository;
        this.userService = userService;
        this.emailService = emailService;
        this.mailBuilder = mailBuilder;
    }

    /**
     * Создает новый обмен в системе и сохраняет его в базе данных.
     * При этом участники обмена лишаются возможности создать ещё одну заявку на обмен с книгами, принимающими участие в этом обмене.
     *
     * @param exchange Объект Exchange, который необходимо сохранить.
     * @return Созданный объект Exchange.
     * @throws IllegalStateException если обмен равен null
     */
    protected Exchange createExchange(Exchange exchange) {
        if (exchange == null) {
            throw new IllegalArgumentException("Обмен не может быть null");
        }

        try {
            userService.removeBookFromOfferedByUser(exchange.getMember1().getId(), exchange.getExchangedBook1().getId());
            userService.removeBookFromOfferedByUser(exchange.getMember2().getId(), exchange.getExchangedBook2().getId());
            exchange.setStatus(ExchangeStatus.CONFIRMED);
            exchange.setCurrentDate();
            exchangeRepository.save(exchange);
            log.info("Создан обмен с id {}", exchange.getId());
            return exchange;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании обмена", e);
        }
    }

    /**
     * Получает обмен по его идентификатору.
     * @param exchangeId идентификатор обмена
     * @return найденный обмен
     */
    public Exchange getExchangeById(long exchangeId) {
        Exchange foundExchange = exchangeRepository.findById(exchangeId);
        if (foundExchange == null) {
            throw new IllegalArgumentException("Обмен с id " + exchangeId + " не найден");
        }
        else {
            log.info("Найден обмен с id {}", exchangeId);
            return foundExchange;
        }
    }

    /**
     * Обновляет номер трека, установленный участником обмена.
     *
     * @param userId     Идентификатор пользователя.
     * @param exchangeId Идентификатор обмена.
     * @param track      Номер трека.
     */
    public void updateTrackSetByUser(long userId, long exchangeId, String track) {
        Exchange exchange = getExchangeById(exchangeId);

        if (exchange.getMember1().getId() == userId) {
            exchange.setTrack1(track);
            log.info("Обмену {} присвоен трек-номер участника 1", exchange.getId());

            EmailData emailData = mailBuilder.buildUpdateTrackMessage(exchange.getMember2().getEmail(), exchange.getId(), track);
            emailService.sendEmail(emailData.getEmailReceiver(), emailData.getEmailSubject(), emailData.getEmailMessage());
        }
        else if (exchange.getMember2().getId() == userId) {
            exchange.setTrack2(track);
            log.info("Обмену {} присвоен трек-номер участника 2", exchange.getId());

            EmailData emailData = mailBuilder.buildUpdateTrackMessage(exchange.getMember1().getEmail(), exchange.getId(), track);
            emailService.sendEmail(emailData.getEmailReceiver(), emailData.getEmailSubject(), emailData.getEmailMessage());
        }

        exchangeRepository.save(exchange);
        if (exchange.getTrack1() != null && exchange.getTrack2() != null)
            checkAndSetInProgressStatus(exchangeId);

        exchangeRepository.save(exchange);
        Exchange a = exchange;
    }

    /**
     * Устанавливает вместо трека "Доставка без трек-номера" для указанного участника обмена.
     *
     * @param userId     Идентификатор пользователя.
     * @param exchangeId Идентификатор обмена.
     */
    public void setNoTrack(long userId, long exchangeId) {
        Exchange exchange = getExchangeById(exchangeId);

        final String noTrack = "DELIVERY_WITHOUT_TRACK";
        if (exchange.getMember1().getId() == userId) {
            exchange.setTrack1(noTrack);
            log.info("Обмен {} доставляется без трек-номера участника 1", exchange.getId());

            EmailData emailData = mailBuilder.buildNoTrackMessage(exchange.getMember2().getEmail(), exchange.getId());
            emailService.sendEmail(emailData.getEmailReceiver(), emailData.getEmailSubject(), emailData.getEmailMessage());
        }
        else if (exchange.getMember2().getId() == userId) {
            exchange.setTrack2(noTrack);
            log.info("Обмен {} доставляется без трек-номера участника 2", exchange.getId());

            EmailData emailData = mailBuilder.buildNoTrackMessage(exchange.getMember1().getEmail(), exchange.getId());
            emailService.sendEmail(emailData.getEmailReceiver(), emailData.getEmailSubject(), emailData.getEmailMessage());
        }

        exchangeRepository.save(exchange);
        checkAndSetInProgressStatus(exchangeId);
    }

    /**
     * Проверяет трек-номера и устанавливает статус "В процессе доставки", если оба участника отправили книги.
     *
     * @param exchangeId Идентификатор обмена.
     */
    private void checkAndSetInProgressStatus(long exchangeId) {
        Exchange exchange = getExchangeById(exchangeId);

        boolean member1HasTrack = exchange.getTrack1() != null;
        boolean member2HasTrack = exchange.getTrack2() != null;

        if (member1HasTrack && member2HasTrack) {
            exchange.setStatus(ExchangeStatus.IN_PROGRESS);
            exchangeRepository.save(exchange);
            log.info("Статус обмена {}: в процессе доставки", exchange.getId());

            EmailData emailData1 = mailBuilder.buildSetInProgressStatusMessage(exchange.getMember1().getEmail(), exchange.getId());
            emailService.sendEmail(emailData1.getEmailReceiver(), emailData1.getEmailSubject(), emailData1.getEmailMessage());

            EmailData emailData2 = mailBuilder.buildSetInProgressStatusMessage(exchange.getMember2().getEmail(), exchange.getId());
            emailService.sendEmail(emailData2.getEmailReceiver(), emailData2.getEmailSubject(), emailData2.getEmailMessage());
        }
    }

    /**
     * Ищет обмен по указанному статусу и участнику.
     *
     * @param status статус, по которому нужно выполнить поиск
     * @return список обменов, соответствующих условию
     */
    public List<Exchange> searchByStatusAndMember(ExchangeStatus status, long UserId) {
        return exchangeRepository.findByStatusAndMember1IdOrStatusAndMember2Id(status, UserId, status, UserId);
    }

    /**
     * Подтверждает получение книги участником в результате обмена.
     * Если оба участника подтвердили получение, вызывает метод завершения обмена.
     *
     * @param exchangeId Идентификатор обмена.
     * @param userId Идентификатор пользователя, принимающего книгу.
     */
    public void receiveBook(long exchangeId, long userId) {
        Exchange exchange = getExchangeById(exchangeId);

        if (exchange.getMember1().getId() == userId) {
            exchange.setReceived1(true);
            log.info("Участник 1 получил книгу");

            EmailData emailData = mailBuilder.buildReceiveBookMessage(exchange.getMember2().getEmail(), exchange.getId());
            emailService.sendEmail(emailData.getEmailReceiver(), emailData.getEmailSubject(), emailData.getEmailMessage());
        }
        else if (exchange.getMember2().getId() == userId) {
            exchange.setReceived2(true);
            log.info("Участник 2 получил книгу");

            EmailData emailData = mailBuilder.buildReceiveBookMessage(exchange.getMember1().getEmail(), exchange.getId());
            emailService.sendEmail(emailData.getEmailReceiver(), emailData.getEmailSubject(), emailData.getEmailMessage());
        }

        exchangeRepository.save(exchange);
        if (exchange.isReceived1() && exchange.isReceived2())
            finalizeExchange(exchangeId);
    }

    /**
     * Завершает обмен и обновляет библиотеки пользователей при успешной доставке книг.
     *
     * @param exchangeId Идентификатор обмена.
     */
    private void finalizeExchange(long exchangeId) {
        Exchange exchange = getExchangeById(exchangeId);
        long member1Id = exchange.getMember1().getId();
        long member2Id = exchange.getMember2().getId();
        long exchangedBook1Id = exchange.getExchangedBook1().getId();
        long exchangedBook2Id = exchange.getExchangedBook2().getId();

        exchange.setStatus(ExchangeStatus.COMPLETED);
        exchangeRepository.save(exchange);
        userService.removeBookFromUserLibrary(member1Id, exchangedBook1Id);
        userService.removeBookFromUserLibrary(member2Id, exchangedBook2Id);
        userService.addBookToUserLibrary(member2Id, exchangedBook1Id);
        userService.addBookToUserLibrary(member1Id, exchangedBook2Id);
        log.info("Обмен {} успешно завершён, библиотеки пользователей обновлены", exchange.getId());

        EmailData emailData1 = mailBuilder.buildFinalizeExchangeMessage(exchange.getMember1().getEmail(), exchange.getId());
        emailService.sendEmail(emailData1.getEmailReceiver(), emailData1.getEmailSubject(), emailData1.getEmailMessage());

        EmailData emailData2 = mailBuilder.buildFinalizeExchangeMessage(exchange.getMember2().getEmail(), exchange.getId());
        emailService.sendEmail(emailData2.getEmailReceiver(), emailData2.getEmailSubject(), emailData2.getEmailMessage());
    }

    /**
     * Устанавливает статус "PROBLEMS" для обмена с указанным идентификатором, если прошло 30 или более дней с момента его создания.
     * Если условие не выполнено, выбрасывается исключение IllegalStateException.
     *
     * @param exchangeId Идентификатор обмена, для которого требуется установить статус "PROBLEMS".
     * @throws IllegalStateException если не удалось установить статус "PROBLEMS", потому что не прошло 30 дней с момента создания обмена.
     */
    public void setProblemsStatus(long exchangeId) {
        Exchange exchange = getExchangeById(exchangeId);
        LocalDate currentDate = LocalDate.now();
        LocalDate thirtyDaysAfterDate = exchange.getDate().plusDays(30);

        if (currentDate.isAfter(thirtyDaysAfterDate) || currentDate.equals(thirtyDaysAfterDate)) {
            exchange.setStatus(ExchangeStatus.PROBLEMS);
            exchangeRepository.save(exchange);
            log.info("Для обмена {} установлен статус PROBLEMS", exchange.getId());

            boolean enableShow1 = false;
            boolean enableShow2 = false;

            if (!exchange.getMember1().isShowContacts()) {
                userService.enableShowContacts(exchange.getMember1().getId());
                enableShow1 = true;
            }
            if (!exchange.getMember2().isShowContacts()) {
                userService.enableShowContacts(exchange.getMember2().getId());
                enableShow2 = true;
            }

            User user1 = userService.getUserById(exchange.getMember1().getId());
            User user2 = userService.getUserById(exchange.getMember2().getId());

            EmailData emailData1 = mailBuilder.buildProblemsMessage(exchange.getMember1().getEmail(), exchange.getId(), user2);
            emailService.sendEmail(emailData1.getEmailReceiver(), emailData1.getEmailSubject(), emailData1.getEmailMessage());

            EmailData emailData2 = mailBuilder.buildProblemsMessage(exchange.getMember2().getEmail(), exchange.getId(), user1);
            emailService.sendEmail(emailData2.getEmailReceiver(), emailData2.getEmailSubject(), emailData2.getEmailMessage());

            if (enableShow1)
                userService.disableShowContacts(exchange.getMember1().getId());
            if (enableShow2)
                userService.disableShowContacts(exchange.getMember2().getId());

        } else {
            throw new IllegalStateException("Не удалось установить статус PROBLEMS для обмена с id: " + exchangeId +
                    ", так как не прошло 30 дней с момента создания обмена.");
        }
    }
}