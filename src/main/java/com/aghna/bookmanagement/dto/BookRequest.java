package com.aghna.bookmanagement.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

public record BookRequest(

    @NotBlank(message = "title must not be blank")
    String title,

    @NotBlank(message = "title must not be blank")
    String author,

    @NotBlank(message = "isbn must not be blank")
    @Pattern(
        regexp = "^(97(8|9))?\\d{9}(\\d|X)$",
        message = "isbn must be a valid ISBN-10 or ISBN-13 (digits only, optional trailing X)"
    )
    String isbn,

    @NotNull(message = "publishedDate must not be null")
    @Past(message = "publishedDate must be in the past")
    LocalDate publishedDate
) {
}