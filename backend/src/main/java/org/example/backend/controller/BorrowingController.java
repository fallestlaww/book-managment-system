package org.example.backend.controller;

import jakarta.validation.Valid;
import org.example.backend.dto.request.UserInformationRequest;
import org.example.backend.dto.response.BookInformationResponse;
import org.example.backend.dto.response.BorrowedBooksStatisticResponse;
import org.example.backend.dto.response.BorrowedTitlesResponse;
import org.example.backend.dto.response.BorrowingInformationResponse;
import org.example.backend.model.Borrowing;
import org.example.backend.service.impl.BorrowingServiceImpl;
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

import java.util.List;

/**
 * REST controller for borrowing management operations.
 * Provides endpoints for borrowing books, returning books, and retrieving borrowing statistics.
 */
@RestController
@RequestMapping("/borrowing")
@Tag(name = "Borrowing Management", description = "API for book borrowing operations")
public class BorrowingController {
    private final BorrowingServiceImpl borrowingService;

    /**
     * Constructs a new BorrowingController with the specified borrowing service.
     *
     * @param borrowingService the borrowing service implementation
     */
    public BorrowingController(BorrowingServiceImpl borrowingService) {
        this.borrowingService = borrowingService;
    }

    /**
     * Borrows a book for a user.
     *
     * @param user_id the ID of the user borrowing the book
     * @param book_id the ID of the book being borrowed
     * @return ResponseEntity containing the borrowing information
     */
    @PostMapping("/user/{user_id}/book/{book_id}")
    @Operation(
            summary = "Borrow a book",
            description = "Borrows a book for a specific user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book borrowed successfully",
                    content = @Content(schema = @Schema(implementation = BorrowingInformationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request or book already borrowed"),
            @ApiResponse(responseCode = "404", description = "User or book not found")
    })
    public ResponseEntity<?> borrow(@Parameter(description = "User ID", example = "1") @PathVariable() Long user_id,
                                    @Parameter(description = "Book ID", example = "1") @PathVariable() Long book_id) {
        Borrowing borrowing = borrowingService.borrowBook(user_id, book_id);
        return ResponseEntity.status(HttpStatus.CREATED).body(new BorrowingInformationResponse(borrowing));
    }

    /**
     * Gets all books borrowed by a specific user.
     *
     * @param request the user information request containing the user's name
     * @return ResponseEntity containing the list of borrowed books
     */
    @PostMapping("/name")
    @Operation(
            summary = "Get borrowed books by user name",
            description = "Retrieves all books borrowed by a user by their name"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Borrowed books found",
                    content = @Content(schema = @Schema(implementation = BookInformationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> getBorrowedBooksByName(@Parameter(description = "User information request") @RequestBody @Valid UserInformationRequest request) {
        List<BookInformationResponse> borrowedBooksResponses = borrowingService.getBorrowedBooksByUserName(request).stream()
                .map(BookInformationResponse::new).toList();
        return ResponseEntity.ok(borrowedBooksResponses);
    }

    /**
     * Gets all distinct book titles that have been borrowed.
     *
     * @return ResponseEntity containing the list of distinct borrowed book titles
     */
    @GetMapping("/titles/distinct")
    @Operation(
            summary = "Get distinct borrowed book titles",
            description = "Retrieves all unique book titles that have been borrowed"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Distinct titles found",
                    content = @Content(schema = @Schema(implementation = BorrowedTitlesResponse.class)))
    })
    public ResponseEntity<?> getDistinctBorrowedBooksTitles() {
        List<BorrowedTitlesResponse> distinctTitlesResponses = borrowingService.getDistinctBorrowedBooksTitles();
        return ResponseEntity.ok(distinctTitlesResponses);
    }

    /**
     * Gets statistics about borrowed books including title and count of borrowings.
     *
     * @return ResponseEntity containing the borrowing statistics
     */
    @GetMapping("/statistic")
    @Operation(
            summary = "Get borrowing statistics",
            description = "Retrieves statistics about borrowed books including title and borrowing count"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics retrieved",
                    content = @Content(schema = @Schema(implementation = BorrowedBooksStatisticResponse.class)))
    })
    public ResponseEntity<?> getBorrowedBooksStatistics() {
        List<BorrowedBooksStatisticResponse> borrowedBooksStatisticResponses = borrowingService.getDistinctBorrowedBooksTitlesAndCounts();
        return ResponseEntity.ok(borrowedBooksStatisticResponses);
    }

    /**
     * Returns a borrowed book.
     *
     * @param user_id the ID of the user returning the book
     * @param book_id the ID of the book being returned
     * @return ResponseEntity with no content
     */
    @DeleteMapping("/return/user/{user_id}/book/{book_id}")
    @Operation(
            summary = "Return a borrowed book",
            description = "Returns a book that was previously borrowed by a user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book returned successfully"),
            @ApiResponse(responseCode = "404", description = "User, book, or borrowing not found"),
            @ApiResponse(responseCode = "400", description = "Book is not currently borrowed by this user")
    })
    public ResponseEntity<?> returnBook(@Parameter(description = "User ID", example = "1") @PathVariable() Long user_id,
                                        @Parameter(description = "Book ID", example = "1") @PathVariable() Long book_id) {
        borrowingService.returnBook(user_id, book_id);
        return ResponseEntity.ok().build();
    }
}
