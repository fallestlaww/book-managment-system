package org.example.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.example.backend.dto.request.UserInformationRequest;
import org.example.backend.dto.response.BorrowedBooksStatisticResponse;
import org.example.backend.dto.response.BorrowedTitlesResponse;
import org.example.backend.model.Book;
import org.example.backend.model.Borrowing;
import org.example.backend.model.User;
import org.example.backend.service.impl.BorrowingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BorrowingController.class)
class BorrowingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowingServiceImpl borrowingService;

    @Autowired
    private ObjectMapper objectMapper;

    private Borrowing borrowing;
    private Book book;
    private User user;
    private UserInformationRequest userInfoRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("TestUser");
        user.setNumberOfBorrowedBooks(1);

        book = new Book();
        book.setId(1L);
        book.setTitle("TestTitle");
        book.setAuthor("Test Author");
        book.setAmount(1);
        book.setAmountOfBorrowedBooks(1);

        borrowing = new Borrowing();
        borrowing.setId(1L);
        borrowing.setUser(user);
        borrowing.setBook(book);

        userInfoRequest = new UserInformationRequest();
        userInfoRequest.setName("TestUser");
    }

    @Test
    void borrow_success() throws Exception {
        Mockito.when(borrowingService.borrowBook(1L, 1L)).thenReturn(borrowing);

        mockMvc.perform(post("/borrowing/user/1/book/1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user.name").value("TestUser"))
                .andExpect(jsonPath("$.book.title").value("TestTitle"));
    }

    @Test
    void borrow_notFound_returnsNotFound() throws Exception {
        Mockito.when(borrowingService.borrowBook(1L, 1L))
                .thenThrow(new EntityNotFoundException("Book is not available"));

        mockMvc.perform(post("/borrowing/user/1/book/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book is not available"));
    }

    @Test
    void getBorrowedBooksByName_success() throws Exception {
        List<Book> books = List.of(book);
        Mockito.when(borrowingService.getBorrowedBooksByUserName(any(UserInformationRequest.class))).thenReturn(books);

        mockMvc.perform(get("/borrowing/name")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userInfoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("TestTitle"));
    }

    @Test
    void getBorrowedBooksByName_userNotFound_returnsNotFound() throws Exception {
        Mockito.when(borrowingService.getBorrowedBooksByUserName(any(UserInformationRequest.class)))
                .thenThrow(new EntityNotFoundException("User not found"));

        mockMvc.perform(get("/borrowing/name")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userInfoRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void getBorrowedBooksByName_invalidRequest_returnsNotFound() throws Exception {
        Mockito.when(borrowingService.getBorrowedBooksByUserName(any(UserInformationRequest.class)))
                .thenThrow(new IllegalArgumentException("Request cannot be null"));

        mockMvc.perform(get("/borrowing/name")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userInfoRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Request cannot be null"));
    }

    @Test
    void getDistinctBorrowedBooksTitles_success() throws Exception {
        List<BorrowedTitlesResponse> titles = List.of(new BorrowedTitlesResponse("TestTitle"));
        Mockito.when(borrowingService.getDistinctBorrowedBooksTitles()).thenReturn(titles);

        mockMvc.perform(get("/borrowing/titles/distinct"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("TestTitle"));
    }

    @Test
    void getDistinctBorrowedBooksTitles_empty() throws Exception {
        Mockito.when(borrowingService.getDistinctBorrowedBooksTitles()).thenReturn(List.of());

        mockMvc.perform(get("/borrowing/titles/distinct"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getBorrowedBooksStatistics_success() throws Exception {
        List<BorrowedBooksStatisticResponse> stats = List.of(new BorrowedBooksStatisticResponse("TestTitle", 2L));
        Mockito.when(borrowingService.getDistinctBorrowedBooksTitlesAndCounts()).thenReturn(stats);

        mockMvc.perform(get("/borrowing/statistic"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("TestTitle"))
                .andExpect(jsonPath("$[0].amount_of_borrowed_books").value(2));
    }

    @Test
    void getBorrowedBooksStatistics_empty() throws Exception {
        Mockito.when(borrowingService.getDistinctBorrowedBooksTitlesAndCounts()).thenReturn(List.of());

        mockMvc.perform(get("/borrowing/statistic"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void returnBook_success() throws Exception {
        Mockito.doNothing().when(borrowingService).returnBook(1L, 1L);

        mockMvc.perform(delete("/borrowing/return/user/1/book/1"))
                .andExpect(status().isOk());
    }

    @Test
    void returnBook_notFound_returnsNotFound() throws Exception {
        Mockito.doThrow(new EntityNotFoundException("Borrowing is not available"))
                .when(borrowingService).returnBook(1L, 1L);

        mockMvc.perform(delete("/borrowing/return/user/1/book/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Borrowing is not available"));
    }
} 