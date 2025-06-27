package org.example.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.dto.request.UserCreationRequest;
import org.example.backend.dto.request.UserUpdateRequest;
import org.example.backend.model.User;
import org.example.backend.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.example.backend.exceptions.custom.EntityBorrowedException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private UserCreationRequest creationRequest;
    private UserUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John");
        user.setNumberOfBorrowedBooks(2);

        creationRequest = new UserCreationRequest();
        creationRequest.setName("John");

        updateRequest = new UserUpdateRequest();
        updateRequest.setName("John");
        updateRequest.setNumberOfBorrowedBooks(2);
    }

    @Test
    void createUser_success() throws Exception {
        Mockito.when(userService.createUser(any(UserCreationRequest.class))).thenReturn(user);

        mockMvc.perform(post("/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(creationRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    void readUser_success() throws Exception {
        Mockito.when(userService.readUser(1L)).thenReturn(user);

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    void updateUser_success() throws Exception {
        Mockito.when(userService.updateUser(eq(1L), any(UserUpdateRequest.class))).thenReturn(user);

        mockMvc.perform(put("/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.number_of_borrowed_books").value(2));
    }

    @Test
    void deleteUser_success() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isOk());
    }

    @Test
    void createUser_invalidRequest_returnsBadRequest() throws Exception {
        UserCreationRequest invalidRequest = new UserCreationRequest();

        mockMvc.perform(post("/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void readUser_userNotFound_returnsNotFound() throws Exception {
        Mockito.when(userService.readUser(1L)).thenThrow(new EntityNotFoundException("User not found"));

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void createUser_userExists_returnsBadRequest() throws Exception {
        Mockito.when(userService.createUser(any(UserCreationRequest.class)))
                .thenThrow(new IllegalArgumentException("User already exists"));

        mockMvc.perform(post("/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(creationRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User already exists"));
    }

    @Test
    void updateUser_userExists_returnsBadRequest() throws Exception {
        Mockito.when(userService.updateUser(eq(1L), any(UserUpdateRequest.class)))
                .thenThrow(new IllegalArgumentException("User already exists"));

        mockMvc.perform(put("/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User already exists"));
    }

    @Test
    void updateUser_userNotFound_returnsNotFound() throws Exception {
        Mockito.when(userService.updateUser(eq(1L), any(UserUpdateRequest.class)))
                .thenThrow(new EntityNotFoundException("User not found"));

        mockMvc.perform(put("/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void deleteUser_borrowedBooks_returnsConflict() throws Exception {
        Mockito.doThrow(new EntityBorrowedException("User has borrowed at least 1 copy of any book. Maybe you entered wrong id?"))
                .when(userService).deleteUser(1L);

        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("User has borrowed at least 1 copy of any book. Maybe you entered wrong id?"));
    }

    @Test
    void deleteUser_userNotFound_returnsNotFound() throws Exception {
        Mockito.doThrow(new EntityNotFoundException("User not found"))
                .when(userService).deleteUser(1L);

        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }
} 