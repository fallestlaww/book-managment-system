package org.example.backend.service;

import org.example.backend.dto.request.UserCreationRequest;
import org.example.backend.dto.request.UserUpdateRequest;
import org.example.backend.exceptions.custom.EntityBorrowedException;
import org.example.backend.exceptions.custom.EntityNullException;
import org.example.backend.model.User;
import org.example.backend.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.example.backend.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserCreationRequest creationRequest;
    private UserUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("John");
        user.setNumberOfBorrowedBooks(0);

        creationRequest = new UserCreationRequest();
        creationRequest.setName("John");

        updateRequest = new UserUpdateRequest();
        updateRequest.setName("John");
        updateRequest.setNumberOfBorrowedBooks(2);
    }

    @Test
    void createUser_success() {
        when(userRepository.findByName("John")).thenReturn(null);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.createUser(creationRequest);
        assertEquals("John", result.getName());
        assertEquals(0, result.getNumberOfBorrowedBooks());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_nullRequest_throwsException() {
        assertThrows(EntityNullException.class, () -> userService.createUser(null));
    }

    @Test
    void createUser_userExists_throwsException() {
        when(userRepository.findByName("John")).thenReturn(new User());
        assertThrows(EntityNullException.class, () -> userService.createUser(creationRequest));
    }

    @Test
    void readUser_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User result = userService.readUser(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void readUser_notFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.readUser(1L));
    }

    @Test
    void updateUser_success() {
        when(userRepository.findByName("John")).thenReturn(null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updated = userService.updateUser(1L, updateRequest);
        assertEquals("John", updated.getName());
        assertEquals(2, updated.getNumberOfBorrowedBooks());
    }

    @Test
    void updateUser_nullId_throwsException() {
        assertThrows(EntityNullException.class, () -> userService.updateUser(null, updateRequest));
    }

    @Test
    void updateUser_nullRequest_throwsException() {
        assertThrows(EntityNullException.class, () -> userService.updateUser(1L, null));
    }

    @Test
    void updateUser_userExists_throwsException() {
        when(userRepository.findByName("John")).thenReturn(new User());
        assertThrows(EntityExistsException.class, () -> userService.updateUser(1L, updateRequest));
    }

    @Test
    void deleteUser_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);
        assertDoesNotThrow(() -> userService.deleteUser(1L));
        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_borrowedBooks_throwsException() {
        user.setNumberOfBorrowedBooks(2);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        assertThrows(EntityBorrowedException.class, () -> userService.deleteUser(1L));
    }
}
