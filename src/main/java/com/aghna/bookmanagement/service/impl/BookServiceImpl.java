package com.aghna.bookmanagement.service.impl;

import com.aghna.bookmanagement.dto.BookPatchRequest;
import com.aghna.bookmanagement.dto.BookRequest;
import com.aghna.bookmanagement.dto.BookResponse;
import com.aghna.bookmanagement.exception.DuplicateResourceException;
import com.aghna.bookmanagement.exception.ResourceNotFoundException;
import com.aghna.bookmanagement.mapper.BookMapper;
import com.aghna.bookmanagement.model.Book;
import com.aghna.bookmanagement.repository.BookRepository;
import com.aghna.bookmanagement.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookResponse create(BookRequest request) {
        if (bookRepository.existsByIsbn(request.isbn())) {
            throw new DuplicateResourceException("A book with isbn '%s' already exists".formatted(request.isbn()));
        }

        Book saved = bookRepository.save(bookMapper.toEntity(request));
        return bookMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponse findById(Long id) {
        return bookMapper.toResponse(getBookOrThrow(id));
    }

    @Override
    public BookResponse update(Long id, BookRequest request) {
        Book book = getBookOrThrow(id);
        assertIsbnAvailableForOtherBook(id, request.isbn());

        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setIsbn(request.isbn());
        book.setPublishedDate(request.publishedDate());

        return bookMapper.toResponse(bookRepository.save(book));
    }

    @Override
    public BookResponse patch(Long id, BookPatchRequest request) {
        Book book = getBookOrThrow(id);

        if (request.title() != null) {
            book.setTitle(request.title());
        }
        if (request.author() != null) {
            book.setAuthor(request.author());
        }
        if (request.isbn() != null) {
            assertIsbnAvailableForOtherBook(id, request.isbn());
            book.setIsbn(request.isbn());
        }
        if (request.publishedDate() != null) {
            book.setPublishedDate(request.publishedDate());
        }

        return bookMapper.toResponse(bookRepository.save(book));
    }

    @Override
    public void delete(Long id) {
        Book book = getBookOrThrow(id);
        bookRepository.delete(book);
    }

    private Book getBookOrThrow(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id %d not found".formatted(id)));
    }

    private void assertIsbnAvailableForOtherBook(Long id, String isbn) {
        bookRepository.findByIsbn(isbn).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new DuplicateResourceException("A book with isbn '%s' already exists".formatted(isbn));
            }
        });
    }
}
