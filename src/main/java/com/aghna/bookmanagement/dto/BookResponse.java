package com.aghna.bookmanagement.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record BookResponse(
        Long id,
        String title,
        String author,
        String isbn,
        LocalDate publishedDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
