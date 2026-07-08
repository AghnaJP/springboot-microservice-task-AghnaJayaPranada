package com.aghna.bookmanagement.controller;

import com.aghna.bookmanagement.dto.BookPatchRequest;
import com.aghna.bookmanagement.dto.BookRequest;
import com.aghna.bookmanagement.dto.BookResponse;
import com.aghna.bookmanagement.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Tag(name = "Books", description = "CRUD operations for Book Management")
public class BookController {

    private final BookService bookService;

    // POST /api/books -> Create a book
    @Operation(summary = "Create a new book")
    @PostMapping
    public ResponseEntity<BookResponse> create(@Valid @RequestBody BookRequest request) {
        BookResponse created = bookService.create(request);
        return ResponseEntity
                .created(URI.create("/api/books/" + created.id()))
                .body(created);
    }

    // GET /api/books -> List All Books
    @Operation(summary = "Get all books")
    @GetMapping
    public ResponseEntity<List<BookResponse>> findAll() {
        return ResponseEntity.ok(bookService.findAll());
    }

    // GET /api/books/{id} -> Get Book By ID
    @Operation(summary = "Get a book by id")
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    // PUT /api/books/{id} -> full update of a book
    @Operation(summary = "Full update of a book")
    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> update(@PathVariable Long id, @Valid @RequestBody BookRequest request) {
        return ResponseEntity.ok(bookService.update(id, request));
    }

    // PATCH /api/books/{id} -> partial update of a book
    @Operation(summary = "Partial update of a book (example: title only)")
    @PatchMapping("/{id}")
    public ResponseEntity<BookResponse> patch(@PathVariable Long id, @Valid @RequestBody BookPatchRequest request) {
        return ResponseEntity.ok(bookService.patch(id, request));
    }

    // DELETE /api/books/{id} -> delete a book
    @Operation(summary = "Delete a book")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bookService.delete(id);
    }
}
