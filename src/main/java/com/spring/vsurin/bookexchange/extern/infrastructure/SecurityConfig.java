package com.spring.vsurin.bookexchange.extern.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Конфигурация Spring Security для профиля prod - c ограничениями доступа
 * только для аутентифицированных пользователей, а к некоторым эндпойнтам -
 * только для админов.
 */
@Slf4j
@Configuration
@EnableWebSecurity
@Profile("prod")
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/users/delete/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/books/delete/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/users/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/users/no-admin/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2Login(Customizer.withDefaults())
                .csrf((csrf) -> csrf.disable());
        SecurityFilterChain filterChain = http.build();
        log.info("Security filter chain configured successfully");

        return filterChain;
    }
}
