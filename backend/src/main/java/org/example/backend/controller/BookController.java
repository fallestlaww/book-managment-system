package org.example.backend.controller;

import jakarta.validation.Valid;
import org.example.backend.dto.request.BookCreationRequest;
import org.example.backend.dto.request.BookUpdateRequest;
import org.example.backend.dto.response.BookInformationResponse;
import org.example.backend.model.Book;
import org.example.backend.service.BookService;
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
 * REST controller for book management operations.
 * Provides endpoints for creating, reading, updating, and deleting books.
 */
@RestController
@RequestMapping("/book")
@Tag(name = "Book Management", description = "API for book management operations")
public class BookController {
    private final BookService bookService;

    /**
     * Constructs a new BookController with the specified book service.
     *
     * @param bookService the book service implementation
     */
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Retrieves a book by its ID.
     *
     * @param id the book ID
     * @return ResponseEntity containing the book information
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Get book by ID",
            description = "Returns book information by its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book found",
                    content = @Content(schema = @Schema(implementation = BookInformationResponse.class))),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<?> readBook(@Parameter(description = "Book ID", example = "1")
                                      @PathVariable Long id) {
        Book book = bookService.readBook(id);
        return ResponseEntity.ok(new BookInformationResponse(book));
    }

    /**
     * Creates a new book.
     *
     * @param request the book creation request
     * @return ResponseEntity containing the created book information
     */
    @PostMapping("/create")
    @Operation(
            summary = "Create new book",
            description = "Creates a new book with provided data"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book created",
                    content = @Content(schema = @Schema(implementation = BookInformationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<?> createBook(@Parameter(description = "Book creation data")
                                        @RequestBody @Valid BookCreationRequest request) {
        Book book = bookService.createBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new BookInformationResponse(book));
    }

    /**
     * Updates an existing book.
     *
     * @param id      the book ID
     * @param request the book update request
     * @return ResponseEntity containing the updated book information
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Update book",
            description = "Updates an existing book by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated",
                    content = @Content(schema = @Schema(implementation = BookInformationResponse.class))),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<?> updateBook(@Parameter(description = "Book ID to update", example = "1")
                                        @PathVariable Long id, @Parameter(description = "Book update data") @RequestBody @Valid BookUpdateRequest request) {
        Book book = bookService.updateBook(id, request);
        return ResponseEntity.ok(new BookInformationResponse(book));
    }

    /**
     * Deletes a book.
     *
     * @param id the book ID
     * @return ResponseEntity with no content
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete book",
            description = "Deletes a book by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book deleted"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<?> deleteBook(@Parameter(description = "Book ID to delete", example = "1")
                                        @PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok().build();
    }
}
