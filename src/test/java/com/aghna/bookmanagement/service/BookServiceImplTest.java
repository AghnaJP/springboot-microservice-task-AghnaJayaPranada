package com.aghna.bookmanagement.service;

import com.aghna.bookmanagement.dto.BookPatchRequest;
import com.aghna.bookmanagement.dto.BookRequest;
import com.aghna.bookmanagement.dto.BookResponse;
import com.aghna.bookmanagement.exception.DuplicateResourceException;
import com.aghna.bookmanagement.exception.ResourceNotFoundException;
import com.aghna.bookmanagement.mapper.BookMapper;
import com.aghna.bookmanagement.model.Book;
import com.aghna.bookmanagement.repository.BookRepository;
import com.aghna.bookmanagement.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    private final BookMapper bookMapper = new BookMapper();

    @InjectMocks
    private BookServiceImpl bookService;

    private Book existingBook;

    @BeforeEach
    void setUp() {
        bookService = new BookServiceImpl(bookRepository, bookMapper);

        existingBook = Book.builder()
                .id(1L)
                .title("Harry Potter")
                .author("J.K. Rowling")
                .isbn("1234567891")
                .publishedDate(LocalDate.of(1997, 6, 26))
                .build();
    }

    @Test
    void create_savesBook_whenIsbnIsUnique() {

        // GIVEN
        BookRequest request = new BookRequest(
                "Harry Potter",
                "J.K. Rowling",
                "1234567891",
                LocalDate.of(1997, 6, 26));

        when(bookRepository.existsByIsbn(request.isbn())).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(existingBook);

        // WHEN
        BookResponse response = bookService.create(request);

        // THEN
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.title()).isEqualTo("Harry Potter");
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void create_throwsDuplicateResourceException_whenIsbnAlreadyExists() {

        // GIVEN
        BookRequest request = new BookRequest(
                "Harry Potter",
                "J.K. Rowling",
                "1234567891",
                LocalDate.of(1997, 6, 26));

        when(bookRepository.existsByIsbn(request.isbn())).thenReturn(true);

        // WHEN & THEN
        assertThatThrownBy(() -> bookService.create(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("1234567891");

        verify(bookRepository, never()).save(any());
    }

    @Test
    void findById_returnsBook_whenItExists() {

        // GIVEN
        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));

        // WHEN
        BookResponse response = bookService.findById(1L);

        // THEN
        assertThat(response.title()).isEqualTo("Harry Potter");
    }

    @Test
    void findById_throwsResourceNotFoundException_whenItDoesNotExist() {

        // GIVEN
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThatThrownBy(() -> bookService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void findAll_returnsAllBooksMappedToResponses() {

        // GIVEN
        when(bookRepository.findAll()).thenReturn(List.of(existingBook));

        // WHEN
        List<BookResponse> responses = bookService.findAll();

        // THEN
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).title()).isEqualTo("Harry Potter");
        assertThat(responses.get(0).isbn()).isEqualTo("1234567891");
    }

    @Test
    void patch_updatesOnlyProvidedFields() {

        // GIVEN
        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        BookPatchRequest patch = new BookPatchRequest(
                "Batman",
                null,
                null,
                null);

        // WHEN
        BookResponse response = bookService.patch(1L, patch);

        // THEN
        assertThat(response.title()).isEqualTo("Batman");
        assertThat(response.author()).isEqualTo("J.K. Rowling");
        assertThat(response.isbn()).isEqualTo("1234567891");
    }

    @Test
    void delete_throwsResourceNotFoundException_whenBookDoesNotExist() {

        // GIVEN
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThatThrownBy(() -> bookService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(bookRepository, never()).delete(any());
    }
}