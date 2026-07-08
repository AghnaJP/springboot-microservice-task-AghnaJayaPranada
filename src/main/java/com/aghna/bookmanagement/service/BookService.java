package com.aghna.bookmanagement.service;

import com.aghna.bookmanagement.dto.BookPatchRequest;
import com.aghna.bookmanagement.dto.BookRequest;
import com.aghna.bookmanagement.dto.BookResponse;

import java.util.List;

public interface BookService {

    BookResponse create(BookRequest request);

    List<BookResponse> findAll();

    BookResponse findById(Long id);

    BookResponse update(Long id, BookRequest request);

    BookResponse patch(Long id, BookPatchRequest request);

    void delete(Long id);
}
