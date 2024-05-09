package com.spring.vsurin.bookexchange.extern.api;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateDTO {

    @Size(max = 1000)
    private String preferences;
}
