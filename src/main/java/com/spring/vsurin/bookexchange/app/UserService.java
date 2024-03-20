package com.spring.vsurin.bookexchange.app;

import com.spring.vsurin.bookexchange.domain.Book;
import com.spring.vsurin.bookexchange.domain.Exchange;
import com.spring.vsurin.bookexchange.domain.ExchangeStatus;
import com.spring.vsurin.bookexchange.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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


    public UserService(UserRepository userRepository, BookService bookService) {
        this.userRepository = userRepository;
        this.bookService = bookService;
    }

    /**
     * Создает нового пользователя и, если это не null, сохраняет его в базе данных.
     * @param user объект пользователя для создания
     * @return сохраненный пользователь
     * @throws IllegalStateException если пользователь равен null
     */
    public User createUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Пользователь не может быть null");
        }

        try {
            User savedUser = userRepository.save(user);
            log.info("Создан пользователь с id {}", savedUser.getId());
            return savedUser;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании пользователя", e);
        }
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
     * Удаляет пользователя из базы данных по его идентификатору.
     * @param userId идентификатор пользователя для удаления
     */
    public void deleteUser(long userId) {
        User foundUser = userRepository.findById(userId);
        if (foundUser == null) {
            throw new IllegalArgumentException("Пользователь с id " + userId + " не найден");
        } else {
            userRepository.deleteById(userId);
            log.info("Удалён пользователь с id {}", userId);
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
                log.error("Книги не существует!");
        }
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

        if (book != null) {
            if (user.getLibrary().contains(book)) {
                List<Exchange> allUserExchanges = getAllUserExchanges(userId);

                boolean bookInExchange = false;
                for (Exchange ex : allUserExchanges) {
                    if (ex.getStatus() == ExchangeStatus.CONFIRMED || ex.getStatus() == ExchangeStatus.IN_PROGRESS || ex.getStatus() == ExchangeStatus.PROBLEMS)
                        if (ex.getExchangedBook1().getId() == bookId || ex.getExchangedBook2().getId() == bookId) {
                            bookInExchange = true;
                            break;
                        }
                }

                if (!bookInExchange) {
                    user.getOfferedBooks().add(book);
                    book.getUsersOfferingForExchange().add(user);
                    userRepository.save(user);
                    log.info("Книга с id {} в библиотеке пользователя с id {} доступна для обмена", bookId, userId);
                } else
                    log.error("Книга с id {} в библиотеке пользователя с id {} не доступна для обмена! Она принимает участие в другом обмене!", bookId, userId);
            } else
                log.error("Книга с id {} в библиотеке пользователя с id {} не доступна для обмена, т.к. отсутствует в библиотеке!", bookId, userId);
        } else
            log.error("Книги не существует!");
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
     * Обновляет адрес электронной почты пользователя.
     * @param userId идентификатор пользователя
     * @param newMail новый адрес электронной почты
     */
    public void updateUserMail(long userId, String newMail) {
        User user = getUserById(userId);
        if (user != null) {
            user.setEmail(newMail);
            userRepository.save(user);
            log.info("Email пользователя с id {} изменён на {}", userId, newMail);
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
}