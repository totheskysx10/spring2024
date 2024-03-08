package com.spring.vsurin.bookexchange.app;

import com.spring.vsurin.bookexchange.domain.Book;
import com.spring.vsurin.bookexchange.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Сервисный класс для работы с пользователями.
 */
@Slf4j
@Component
public class UserService {
    private final UserRepository userRepository;
    private final BookService bookService;

    @Autowired
    public UserService(UserRepository userRepository, BookService bookService) {
        this.userRepository = userRepository;
        this.bookService = bookService;
    }

    /**
     * Создает нового пользователя и сохраняет его в базе данных.
     * @param user объект пользователя для создания
     * @return сохраненный пользователь
     */
    public User createUser(User user) {
        userRepository.save(user);
        log.info("Создан пользователь с id {}", user.getId());
        return user;
    }

    /**
     * Получает пользователя по его идентификатору.
     * @param userId идентификатор пользователя
     * @return найденный пользователь
     */
    public User getUserById(long userId) {
        User foundUser = userRepository.findById(userId);
        if (foundUser == null)
            log.error("Не найден пользователь с id {}", userId);
        else {
            log.info("Найден пользователь с id {}", userId);
            return foundUser;
        }
        return null;
    }

    /**
     * Удаляет пользователя из базы данных по его идентификатору.
     * @param userId идентификатор пользователя для удаления
     */
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
        log.info("Удалён пользователь с id {}", userId);
    }

    /**
     * Добавляет книгу к библиотеке пользователя.
     * @param userId идентификатор пользователя
     * @param bookId идентификатор книги для добавления
     */
    public void addBookToUserLibrary(long userId, long bookId) {
        User user = getUserById(userId);

        Book book = bookService.getBookById(bookId);

        if (!user.getLibrary().contains(book)) {
            user.getLibrary().add(book);
            book.getOwners().add(user);
            userRepository.save(user);
            log.info("Книга с id {} добавлена в библиотеку пользователя с id {}", bookId, userId);
        }
    }

    /**
     * Добавляет книгу в библиотеке к тем, которые пользователь готов обменять.
     * @param userId идентификатор пользователя
     * @param bookId идентификатор книги для добавления
     */
    public void addBookToOfferedByUser(long userId, long bookId) {
        User user = getUserById(userId);

        Book book = bookService.getBookById(bookId);

        if (user.getLibrary().contains(book)) {
            user.getOfferedBooks().add(book);
            book.getUsersOfferingForExchange().add(user);
            userRepository.save(user);
            log.info("Книга с id {} в библиотеке пользователя с id {} доступна для обмена", bookId, userId);
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
            book.getUsersOfferingForExchange().remove(user);
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

        if (user.getLibrary().contains(book)) {
            user.getLibrary().remove(book);
            book.getOwners().remove(user);
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
            List<String> addressList = user.getAddressList();
            addressList.add(address);
            user.setAddressList(addressList);
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
            List<String> addressList = user.getAddressList();
            addressList.remove(address);
            user.setAddressList(addressList);
            userRepository.save(user);
            log.info("Адрес {} удалён из списка адресов пользователя с id {}", address, userId);
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
}