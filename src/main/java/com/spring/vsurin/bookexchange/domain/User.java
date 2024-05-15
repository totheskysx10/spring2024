package com.spring.vsurin.bookexchange.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;

/**
 * Сущность пользователя.
 * Каждый пользователь имеет id, имя, email,
 * библиотеку, список книг для обмена в данный момент, список адресов и номер телефона.
 */
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@Slf4j
public class User {
    /**
     * Уникальный идентификатор пользователя в базе.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @Getter
    @EqualsAndHashCode.Include
    private long id;

    /**
     * Имя пользователя.
     */
    @Column(name = "user_name")
    @Getter
    private String username;

    /**
     * Пол пользователя.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "user_gender")
    @Getter
    @Setter
    private UserGender gender;

    /**
     * Адрес электронной почты пользователя.
     */
    @Column(name = "user_email")
    @Getter
    private String email;

    /**
     * Библиотека пользователя (книги, которые у него есть).
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_books",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    @Getter
    private List<Book> library;

    /**
     * Книги, которые пользователь готов обменять.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_offered_books",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    @Getter
    private List<Book> offeredBooks;

    /**
     * Список адресов проживания пользователя, куда можно доставлять книги при обмене.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_addresses", joinColumns = @JoinColumn(name = "user_id"))
    private List<String> addressList;

    /**
     * Номер телефона пользователя.
     */
    @Column(name = "user_phone")
    @Setter
    private String phoneNumber;

    /**
     * Список обменов пользователя, как member 1.
     */
    @OneToMany(mappedBy = "member1", fetch = FetchType.EAGER)
    @Getter
    private List<Exchange> exchangesAsMember1;

    /**
     * Список обменов пользователя, как member 2.
     */
    @OneToMany(mappedBy = "member2", fetch = FetchType.EAGER)
    @Getter
    private List<Exchange> exchangesAsMember2;

    /**
     * Основной адрес доставки пользователя, используемый в данный момент.
     */
    @Column(name = "main_address")
    @Setter
    private String mainAddress;

    /**
     * Роль пользователя.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    @Getter
    @Setter
    @NonNull
    private UserRole role;

    /**
     * Разрешение на получение контактных данных пользователя.
     */
    @Column(name = "user_show_contacts")
    @Getter
    @Setter
    private boolean showContacts;

    /**
     * Ссылка на аватар пользователя.
     */
    @Column(name = "user_avatar")
    @Getter
    @Setter
    private String avatarLink;

    /**
     * Предпочтения пользователя.
     */
    @Column(name = "user_preferences")
    @Getter
    @Setter
    private String preferences;

    /**
     * Id пользователей, имеющих доступ к главному адресу доставки.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "users_with_access_to_mainAddress", joinColumns = @JoinColumn(name = "user_id"))
    private List<Long> usersWithAccessToMainAddress;

    /**
     * Возвращает телефон пользователя, если разрешено
     */
    public String getPhoneNumber() {
        if (showContacts) {
            return phoneNumber;
        } else {
            log.warn("Пользователь с id {} скрыл контакты!", id);
            return null;
        }
    }

    /**
     * Возвращает список адресов пользователя, если это текущий пользователь
     */
    public List<String> getAddressList() {
            if (emailEqualsWithAuth())
                return addressList;

            return null;
    }

    /**
     * Возвращает главный адрес пользователя, если это текущий пользователь или участник обмена с этим пользователем
     */
    public String getMainAddress(long userId) {
        if (emailEqualsWithAuth() || usersWithAccessToMainAddress.contains(userId))
            return mainAddress;

        return null;
    }

    /**
     * Возвращает итератор для Id пользователей, имеющих доступ к главному адресу доставки.
     *
     * @return итератор для Id пользователей, имеющих доступ к главному адресу доставки
     */
    public Iterable<Long> getUsersWithAccessToMainAddress() {
        return usersWithAccessToMainAddress;
    }

    /**
     * Добавляет Id пользователя, получающего доступ к главному адресу доставки
     *
     * @param userId   Id пользователя, получающего доступ к главному адресу доставки
     */
    public void addUserWithAccessToMainAddress(long userId) {
        usersWithAccessToMainAddress.add(userId);
    }

    /**
     * Удаляет Id пользователя, имеющего доступ к главному адресу доставки
     *
     * @param userId   Id пользователя, имеющего доступ к главному адресу доставки
     */
    public void removeUserWithAccessToMainAddress(long userId) {
        usersWithAccessToMainAddress.remove(userId);
    }

    /**
     * Проверяет, совпадает ли email текущего аутентифицированного пользователя с email объекта.
     *
     * @return true, если email текущего аутентифицированного пользователя совпадает с email объекта; в противном случае false.
     */
    private boolean emailEqualsWithAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            String name = oauth2User.getName();
            if (name == null)
                return false;

            return name.equals(this.email);
        }
        return false;
    }
}