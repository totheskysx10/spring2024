package com.spring.vsurin.bookexchange.extern.infrastructure;

import com.spring.vsurin.bookexchange.app.UserRepository;
import com.spring.vsurin.bookexchange.app.UserService;
import com.spring.vsurin.bookexchange.domain.User;
import com.spring.vsurin.bookexchange.domain.UserGender;
import com.spring.vsurin.bookexchange.domain.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public CustomOAuth2UserService(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    /**
     * Загружает данные пользователя из OAuth2UserRequest,
     * создаёт нового пользователя, если он заходит впервые.
     * Если пользователь в аккаунте яндекса поменял номер телефона или аватар,
     * то они обновятся и при авторизации в приложении.
     *
     * @param userRequest Запрос OAuth2User, содержащий данные пользователя.
     * @return Объект OAuth2User, представляющий аутентифицированного пользователя.
     * @throws OAuth2AuthenticationException если происходит ошибка во время процесса аутентификации.
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = (String) oAuth2User.getAttributes().get("default_email");
        LinkedHashMap<String, Object> defaultPhoneAttributes = (LinkedHashMap<String, Object>) oAuth2User.getAttribute("default_phone");
        String phoneNumber = (String) defaultPhoneAttributes.get("number");
        String avatarLink = getAvatarLinkFromYandex(oAuth2User);

        User user = userRepository.findByEmail(email);

        if (user == null) {
            try {
                user = buildUserFromOAuth2User(oAuth2User);
                userRepository.save(user);
                log.info("Создан пользователь с id {}", user.getId());
            } catch (Exception e) {
                throw new RuntimeException("Ошибка при создании пользователя", e);
            }
        } else {
            if (!user.isShowContacts()) {
                userService.enableShowContacts(user.getId());
                User userToUpdate = userRepository.findById(user.getId());
                if (!Objects.equals(userToUpdate.getPhoneNumber(), phoneNumber))
                    userService.updateUserPhone(user.getId(), phoneNumber);
                userService.disableShowContacts(user.getId());
            }

            if (!Objects.equals(user.getAvatarLink(), avatarLink))
                userService.updateUserAvatarLink(user.getId(), avatarLink);
        }

        oAuth2User = setRole(oAuth2User, user.getRole().toString());

        return oAuth2User;
    }

    /**
     * Строит объект нового пользователя
     *
     * @param oAuth2User объект, представляющий аутентифицированного пользователя.
     */
    private User buildUserFromOAuth2User(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("default_email");
        String name = oAuth2User.getAttribute("real_name");
        LinkedHashMap<String, Object> defaultPhoneAttributes = (LinkedHashMap<String, Object>) oAuth2User.getAttribute("default_phone");
        String phoneNumber = (String) defaultPhoneAttributes.get("number");
        String gender = oAuth2User.getAttribute("sex");
        String avatarLink = getAvatarLinkFromYandex(oAuth2User);

        UserGender genderValue = null;

        switch (gender) {
            case "female":
                genderValue = UserGender.FEMALE;
                break;
            case "male":
                genderValue = UserGender.MALE;
                break;
        }

        User newUser = User.builder()
                .username(name)
                .gender(genderValue)
                .email(email)
                .phoneNumber(phoneNumber)
                .addressList(new ArrayList<>())
                .exchangesAsMember1(new ArrayList<>())
                .exchangesAsMember2(new ArrayList<>())
                .library(new ArrayList<>())
                .offeredBooks(new ArrayList<>())
                .role(UserRole.ROLE_USER)
                .showContacts(false)
                .avatarLink(avatarLink)
                .build();

        return newUser;
    }

    private OAuth2User setRole(OAuth2User user, String role) {
        Map<String, Object> attributes = new HashMap<>(user.getAttributes());
        attributes.put("authorities", role);
        Collection<? extends GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(role));
        return new DefaultOAuth2User(authorities, attributes, "default_email");
    }

    private String getAvatarLinkFromYandex(OAuth2User oAuth2User) {
        boolean isAvatarEmpty = (boolean) oAuth2User.getAttributes().get("is_avatar_empty");
        String avatarLink;

        if (!isAvatarEmpty) {
            String avatarId = (String) oAuth2User.getAttributes().get("default_avatar_id");
            avatarLink = "https://avatars.yandex.net/get-yapic/" + avatarId + "/islands-200";
        }
        else
            avatarLink = "DEFAULT BOOKEXCHANGE AVATAR LINK";

        return avatarLink;
    }
}
