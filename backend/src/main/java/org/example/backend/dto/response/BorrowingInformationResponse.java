package org.example.backend.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;
import org.example.backend.model.Borrowing;

/**
 * Data Transfer Object for borrowing information responses.
 * Contains borrowing transaction data formatted for API responses.
 */
@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "Borrowing information response")
public class BorrowingInformationResponse {
    /**
     * Name of the user who borrowed the book.
     */
    String userName;
    
    /**
     * Title of the book that was borrowed.
     */
    String bookTitle;

    /**
     * Constructs a BorrowingInformationResponse from a Borrowing entity.
     * 
     * @param borrowing the borrowing entity to convert
     */
    public BorrowingInformationResponse(Borrowing borrowing) {
        this.userName = borrowing.getUser().getName();
        this.bookTitle = borrowing.getBook().getTitle();
    }
}
