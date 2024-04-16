package com.spring.vsurin.bookexchange.extern.api;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BookUpdateDTO {

    @Size(max = 1000)
    private String description;

    @Min(1)
    @Max(10)
    private int addedMark;
}
