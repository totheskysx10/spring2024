package com.spring.vsurin.bookexchange.app;

import com.spring.vsurin.bookexchange.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервисный класс для работы с пользователями.
 */
@Slf4j
@Component
public class UserService {
    private final UserRepository userRepository;
    private final BookService bookService;

    private final EmailService emailService;


    public UserService(UserRepository userRepository, BookService bookService, EmailService emailService) {
        this.userRepository = userRepository;
        this.bookService = bookService;
        this.emailService = emailService;
    }

    /**
     * Получает пользователя по его идентификатору.
     * @param userId идентификатор пользователя
     * @return найденный пользователь
     */
    public User getUserById(long userId) {
        User foundUser = userRepository.findById(userId);
        if (foundUser == null) {
            throw new IllegalArgumentException("Пользователь с id " + userId + " не найден");
        }
        else {
            log.info("Найден пользователь с id {}", userId);
            return foundUser;
        }
    }

    /**
     * Удаляет пользователя из базы данных по его идентификатору, отправляет уведомление.
     * @param userId идентификатор пользователя для удаления
     */
    public void deleteUser(long userId) {
        User foundUser = userRepository.findById(userId);
        if (foundUser == null) {
            throw new IllegalArgumentException("Пользователь с id " + userId + " не найден");
        } else {
            String emailSubject = "Аккаунт удалён";
            String emailMessage = "Добрый день! Удалён аккаунт пользователя с id " + userId;
            String emailReceiver = foundUser.getEmail();
            emailService.sendEmail(emailReceiver, emailSubject, emailMessage);

            userRepository.deleteById(userId);
            log.info("Удалён пользователь с id {}", userId);
        }
    }

    /**
     * Отправляет администраторам запрос на удаление пользователя.
     * @param userId идентификатор пользователя для удаления
     * @param reason причина удаления
     */
    public void sendRequestToDeleteUser(long userId, String reason) {
        User foundUser = userRepository.findById(userId);
        if (foundUser == null) {
            throw new IllegalArgumentException("Пользователь с id " + userId + " не найден");
        } else {
            List<User> adminList = userRepository.findByRole(UserRole.ROLE_ADMIN);

            String emailSubject = "Просьба удалить аккаунт";
            String emailMessage = "Добрый день! Прошу удалить мой аккаунт пользователя с id " + userId + ". Причина удаления: " + reason;

            adminList.stream()
                    .map(User::getEmail)
                    .forEach(emailReceiver -> emailService.sendEmail(emailReceiver, emailSubject, emailMessage));

        }
    }

    /**
     * Добавляет книгу к библиотеке пользователя.
     * @param userId идентификатор пользователя
     * @param bookId идентификатор книги для добавления
     */
    public void addBookToUserLibrary(long userId, long bookId) {
        User user = getUserById(userId);

        Book book = bookService.getBookById(bookId);

        if (book != null) {
            if (!user.getLibrary().contains(book)) {
                user.getLibrary().add(book);
                userRepository.save(user);
                log.info("Книга с id {} добавлена в библиотеку пользователя с id {}", bookId, userId);
            } else
                log.error("Книга уже в библиотеке!");
        } else
            log.error("Книги не существует!");
    }

    /**
     * Добавляет книгу в библиотеке к тем, которые пользователь готов обменять.
     * Проверяет, не принимает ли эта книга участие в обмене в данный момент.
     * @param userId идентификатор пользователя
     * @param bookId идентификатор книги для добавления
     */
    public void addBookToOfferedByUser(long userId, long bookId) {
        User user = getUserById(userId);
        Book book = bookService.getBookById(bookId);

        if (user.getMainAddress() == null) {
            log.error("Нельзя предлагать книги для обмена, если не указан основной адрес доставки!");
            return;
        }

        if (book != null) {
            if (user.getLibrary().contains(book)) {
                List<Exchange> allUserExchanges = getAllUserExchanges(userId);

                boolean bookInExchange = allUserExchanges.stream()
                        .anyMatch(ex -> ex.getStatus() == ExchangeStatus.CONFIRMED ||
                                ex.getStatus() == ExchangeStatus.IN_PROGRESS ||
                                ex.getStatus() == ExchangeStatus.PROBLEMS &&
                                        (ex.getExchangedBook1().getId() == bookId || ex.getExchangedBook2().getId() == bookId));

                if (!bookInExchange) {
                    user.getOfferedBooks().add(book);
                    book.getUsersOfferingForExchange().add(user);
                    userRepository.save(user);
                    log.info("Книга с id {} в библиотеке пользователя с id {} доступна для обмена", bookId, userId);
                } else {
                    log.error("Книга с id {} в библиотеке пользователя с id {} не доступна для обмена! Она принимает участие в другом обмене!", bookId, userId);
                }
            } else {
                log.error("Книга с id {} в библиотеке пользователя с id {} не доступна для обмена, т.к. отсутствует в библиотеке!", bookId, userId);
            }
        } else {
            log.error("Книги не существует!");
        }
    }


    /**
     * Удаляет книгу в библиотеке из тех, которые пользователь готов обменять.
     * @param userId идентификатор пользователя
     * @param bookId идентификатор книги для добавления
     */
    public void removeBookFromOfferedByUser(long userId, long bookId) {
        User user = getUserById(userId);

        Book book = bookService.getBookById(bookId);

        if (user.getLibrary().contains(book)) {
            user.getOfferedBooks().remove(book);
            userRepository.save(user);
            log.info("Книга с id {} в библиотеке пользователя с id {} больше не доступна для обмена", bookId, userId);
        }
    }

    /**
     * Удаляет книгу из библиотеки пользователя.
     * @param userId идентификатор пользователя
     * @param bookId идентификатор книги для удаления
     */
    public void removeBookFromUserLibrary(long userId, long bookId) {
        User user = getUserById(userId);
        Book book = bookService.getBookById(bookId);
        List<Book> l = user.getLibrary();

        if (user.getLibrary().contains(book)) {
            user.getLibrary().remove(book);
            userRepository.save(user);
            log.info("Книга с id {} удалена из библиотеки пользователя с id {}", bookId, userId);
        }
    }

    /**
     * Добавляет адрес пользователя в список адресов.
     * @param userId идентификатор пользователя
     * @param address адрес для добавления
     */
    public void addAddressToUser(long userId, String address) {
        User user = getUserById(userId);
        if (user != null) {
            user.getAddressList().add(address);
            userRepository.save(user);
            log.info("Адрес {} добавлен в список адресов пользователя с id {}", address, userId);
        }
    }

    /**
     * Удаляет адрес пользователя из списка.
     * @param userId идентификатор пользователя
     * @param address адрес для удаления
     */
    public void removeAddressFromUser(long userId, String address) {
        User user = getUserById(userId);
        if (user != null) {
            if (user.getAddressList().contains(address)) {
                user.getAddressList().remove(address);
                userRepository.save(user);
                log.info("Адрес {} удалён из списка адресов пользователя с id {}", address, userId);
            }
        }
    }

    /**
     * Обновляет номер телефона пользователя.
     * @param userId идентификатор пользователя
     * @param newPhone новый номер телефона
     */
    public void updateUserPhone(long userId, String newPhone) {
        User user = getUserById(userId);
        if (user != null) {
            user.setPhoneNumber(newPhone);
            userRepository.save(user);
            log.info("Телефонный номер пользователя с id {} изменён на {}", userId, newPhone);
        }
    }

    /**
     * Обновляет основной адрес доставки пользователя.
     * @param userId идентификатор пользователя
     * @param index новый адрес доставки из списка адресов
     */
    public void updateMainAddress(long userId, int index) {
        User user = getUserById(userId);
        user.setMainAddress(user.getAddressList().get(index));
        userRepository.save(user);
        log.info("Основной адрес доставки пользователя с id {} изменён на {}", userId, user.getAddressList().get(index));
    }

    /**
     * Объединяет в один список все обмены пользователя.
     * @param userId идентификатор пользователя
     * @returns все обмены пользователя
     */
    public List<Exchange> getAllUserExchanges(long userId) {
        User user = getUserById(userId);
        List<Exchange> ex1 = user.getExchangesAsMember1();
        List<Exchange> ex2 = user.getExchangesAsMember2();
        List<Exchange> combinedList = new ArrayList<>();
        combinedList.addAll(ex1);
        combinedList.addAll(ex2);

        return combinedList;
    }

    /**
     * Назначает пользователя администратором.
     * @param userId идентификатор пользователя
     */
    public void setAdminStatus(long userId) {
        User user = getUserById(userId);
        if (user != null) {
            if (user.getRole() != UserRole.ROLE_ADMIN) {
                user.setRole(UserRole.ROLE_ADMIN);
                userRepository.save(user);
                log.info("Пользователь с id {} назначен администратором", userId);
            } else
                log.warn("Пользователь с id {} уже администратор!", userId);
        }
    }

    /**
     * Удаляет у пользователя права администратора.
     * @param userId идентификатор пользователя
     */
    public void removeAdminStatus(long userId) {
        User user = getUserById(userId);
        if (user != null) {
            if (user.getRole() == UserRole.ROLE_ADMIN) {
                user.setRole(UserRole.ROLE_USER);
                userRepository.save(user);
                log.info("Пользователь с id {} больше не администратор", userId);
            } else
                log.warn("Пользователь с id {} не администратор!", userId);
        }
    }
}