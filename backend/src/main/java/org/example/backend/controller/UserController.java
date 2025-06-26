package org.example.backend.controller;

import jakarta.validation.Valid;
import org.example.backend.dto.request.UserCreationRequest;
import org.example.backend.dto.request.UserUpdateRequest;
import org.example.backend.dto.response.UserInformationResponse;
import org.example.backend.model.User;
import org.example.backend.service.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * REST controller for user management operations.
 * Provides endpoints for creating, reading, updating, and deleting users.
 */
@RestController
@RequestMapping("/user")
@Tag(name = "User Management", description = "API for user management operations")
public class UserController {
    private final UserServiceImpl userService;

    /**
     * Constructs a new UserController with the specified user service.
     *
     * @param userService the user service implementation
     */
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    /**
     * Creates a new user.
     *
     * @param request the user creation request
     * @return ResponseEntity containing the created user information
     */
    @PostMapping("/create")
    @Operation(
            summary = "Create new user",
            description = "Creates a new user with provided data"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created",
                    content = @Content(schema = @Schema(implementation = UserInformationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<?> createUser(@Parameter(description = "User creation data") @RequestBody @Valid UserCreationRequest request) {
        User user = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserInformationResponse(user));
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the user ID
     * @return ResponseEntity containing the user information
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Get user by ID",
            description = "Returns user information by their unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = UserInformationResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> readUser(@Parameter(description = "User ID", example = "1") @PathVariable Long id) {
        User user = userService.readUser(id);
        return ResponseEntity.ok(new UserInformationResponse(user));
    }

    /**
     * Updates an existing user.
     *
     * @param id      the user ID
     * @param request the user update request
     * @return ResponseEntity containing the updated user information
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Update user",
            description = "Updates an existing user by their ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated",
                    content = @Content(schema = @Schema(implementation = UserInformationResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<?> updateUser(@Parameter(description = "User ID to update", example = "1") @PathVariable Long id,
                                        @Parameter(description = "User update data") @RequestBody @Valid UserUpdateRequest request) {
        User user = userService.updateUser(id, request);
        return ResponseEntity.ok(new UserInformationResponse(user));
    }

    /**
     * Deletes a user.
     *
     * @param id the user ID
     * @return ResponseEntity with no content
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete user",
            description = "Deletes a user by their ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> deleteUser(@Parameter(description = "User ID to delete", example = "1") @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
