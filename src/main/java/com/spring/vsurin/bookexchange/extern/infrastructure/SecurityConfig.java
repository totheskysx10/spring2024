package com.spring.vsurin.bookexchange.extern.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Конфигурация Spring Security для профиля prod - c ограничениями доступа
 * только для аутентифицированных пользователей, к некоторым эндпойнтам -
 * только для админов, к некоторым эндпойнтам - только для текущего пользователя.
 */
@Slf4j
@Configuration
@EnableWebSecurity
@Profile("prod")
public class SecurityConfig {

    private final WebSecurityConditions webSecurityConditions;

    public SecurityConfig(WebSecurityConditions webSecurityConditions) {
        this.webSecurityConditions = webSecurityConditions;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/users/delete/**", "/books/delete/**", "/users/admin/**", "/users/no-admin/**")
                        .hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/users/{userId}/addresses", "/users/{userId}/mainAddress/**", "/users/enable-show-contacts/{userId}", "/users/disable-show-contacts/{userId}", "/users/{userId}/preferences")
                        .access((authentication, context) -> {
                            long userId = Long.parseLong(context.getVariables().get("userId"));
                            return new AuthorizationDecision(webSecurityConditions.isCurrentUser(userId));
                        })
                        .anyRequest().authenticated()
                )
                .oauth2Login(Customizer.withDefaults())
                .csrf((csrf) -> csrf.disable());
        SecurityFilterChain filterChain = http.build();
        log.info("Security filter chain configured successfully");

        return filterChain;
    }
}
