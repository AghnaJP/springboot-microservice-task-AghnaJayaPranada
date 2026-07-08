package com.aghna.bookmanagement.dto;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record BookPatchRequest(

    String title,

    String author,

    @Pattern(
        regexp = "^(97(8|9))?\\d{9}(\\d|X)$",
        message = "isbn must be a valid ISBN-10 or ISBN-13 (digits only, optional trailing X)"
    )
    String isbn,

    @Past(message = "publishedDate must be in the past")
    LocalDate publishedDate
) {
}
