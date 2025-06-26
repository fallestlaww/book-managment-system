package org.example.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.example.backend.dto.request.BookCreationRequest;
import org.example.backend.dto.request.BookUpdateRequest;
import org.example.backend.model.Book;
import org.example.backend.service.BookService;
import org.example.backend.exceptions.custom.EntityBorrowedException;
import org.example.backend.exceptions.custom.EntityNullException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.validation.ConstraintViolationException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private Book book;
    private BookCreationRequest creationRequest;
    private BookUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("TestTitle");
        book.setAuthor("Test Author");
        book.setAmount(5);
        book.setAmountOfBorrowedBooks(0);

        creationRequest = new BookCreationRequest();
        creationRequest.setTitle("TestTitle");
        creationRequest.setAuthor("Test Author");

        updateRequest = new BookUpdateRequest();
        updateRequest.setTitle("UpdatedTitle");
        updateRequest.setAuthor("Updated Author");
        updateRequest.setAmount(10);
        updateRequest.setAmountOfBorrowedBooks(2);
    }

    @Test
    void readBook_success() throws Exception {
        Mockito.when(bookService.readBook(1L)).thenReturn(book);

        mockMvc.perform(get("/book/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("TestTitle"));
    }

    @Test
    void readBook_notFound_returnsNotFound() throws Exception {
        Mockito.when(bookService.readBook(1L)).thenThrow(new EntityNotFoundException("Book not found"));

        mockMvc.perform(get("/book/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book not found"));
    }

    @Test
    void createBook_success() throws Exception {
        Mockito.when(bookService.createBook(any(BookCreationRequest.class))).thenReturn(book);

        mockMvc.perform(post("/book/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(creationRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("TestTitle"));
    }

    @Test
    void createBook_invalidRequest_returnsBadRequest() throws Exception {
        BookCreationRequest invalidRequest = new BookCreationRequest();
        invalidRequest.setTitle("bad");
        invalidRequest.setAuthor("badname");

        Mockito.when(bookService.createBook(any(BookCreationRequest.class)))
                .thenThrow(new ConstraintViolationException("Invalid data", null));

        mockMvc.perform(post("/book/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createBook_nullRequest_returnsNotFound() throws Exception {
        Mockito.when(bookService.createBook(any(BookCreationRequest.class)))
                .thenThrow(new EntityNullException("Request cannot be null"));

        mockMvc.perform(post("/book/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(creationRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Request cannot be null"));
    }

    @Test
    void updateBook_success() throws Exception {
        Mockito.when(bookService.updateBook(eq(1L), any(BookUpdateRequest.class))).thenReturn(book);

        mockMvc.perform(put("/book/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("TestTitle"));
    }

    @Test
    void updateBook_invalidRequest_returnsBadRequest() throws Exception {
        BookUpdateRequest invalidRequest = new BookUpdateRequest();
        invalidRequest.setTitle("bad");
        invalidRequest.setAuthor("badname");

        Mockito.when(bookService.updateBook(eq(1L), any(BookUpdateRequest.class)))
                .thenThrow(new ConstraintViolationException("Invalid data", null));

        mockMvc.perform(put("/book/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBook_nullId_returnsNotFound() throws Exception {
        Mockito.when(bookService.updateBook(eq(1L), any(BookUpdateRequest.class)))
                .thenThrow(new EntityNullException("Id can not be null"));

        mockMvc.perform(put("/book/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Id can not be null"));
    }

    @Test
    void updateBook_notFound_returnsNotFound() throws Exception {
        Mockito.when(bookService.updateBook(eq(1L), any(BookUpdateRequest.class)))
                .thenThrow(new EntityNotFoundException("Book not found"));

        mockMvc.perform(put("/book/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book not found"));
    }

    @Test
    void deleteBook_success() throws Exception {
        Mockito.doNothing().when(bookService).deleteBook(1L);

        mockMvc.perform(delete("/book/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBook_borrowed_returnsConflict() throws Exception {
        Mockito.doThrow(new EntityBorrowedException("At least 1 copy of this book is borrowed. Maybe you entered wrong id?"))
                .when(bookService).deleteBook(1L);

        mockMvc.perform(delete("/book/1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("At least 1 copy of this book is borrowed. Maybe you entered wrong id?"));
    }

    @Test
    void deleteBook_notFound_returnsNotFound() throws Exception {
        Mockito.doThrow(new EntityNotFoundException("Book not found"))
                .when(bookService).deleteBook(1L);

        mockMvc.perform(delete("/book/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book not found"));
    }
} 