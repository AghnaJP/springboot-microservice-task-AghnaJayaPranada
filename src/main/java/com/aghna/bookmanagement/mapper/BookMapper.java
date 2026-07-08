package com.aghna.bookmanagement.mapper;

import com.aghna.bookmanagement.dto.BookRequest;
import com.aghna.bookmanagement.dto.BookResponse;
import com.aghna.bookmanagement.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public Book toEntity(BookRequest request) {
        return Book.builder()
                .title(request.title())
                .author(request.author())
                .isbn(request.isbn())
                .publishedDate(request.publishedDate())
                .build();
    }

    public BookResponse toResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPublishedDate(),
                book.getCreatedAt(),
                book.getUpdatedAt()
        );
    }
}
