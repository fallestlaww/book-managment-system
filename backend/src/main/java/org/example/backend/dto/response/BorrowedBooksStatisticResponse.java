package org.example.backend.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * Data Transfer Object for borrowed books statistics responses.
 * Contains book title and count of borrowings for statistical purposes.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "Borrowed books' statistic response")
public class BorrowedBooksStatisticResponse {
    /**
     * Title of the book.
     */
    String title;
    
    /**
     * Number of times this book has been borrowed.
     */
    Long amountOfBorrowedBooks;

    /**
     * Constructs a BorrowedBooksStatisticResponse with the specified title and count.
     * 
     * @param title the title of the book
     * @param amountOfBorrowedBooks the number of times the book has been borrowed
     */
    public BorrowedBooksStatisticResponse(String title, Long amountOfBorrowedBooks) {
        this.title = title;
        this.amountOfBorrowedBooks = amountOfBorrowedBooks;
    }
}
