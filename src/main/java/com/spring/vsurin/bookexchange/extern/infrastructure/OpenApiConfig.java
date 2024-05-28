package com.spring.vsurin.bookexchange.extern.infrastructure;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "BookExchange",
                description = "Приложенние для обмена книгами", version = "1.0.0",
                contact = @Contact(
                        name = "totheskysx10",
                        email = "crazycat87654@gmail.com"
                )
        )
)
public class OpenApiConfig {
}