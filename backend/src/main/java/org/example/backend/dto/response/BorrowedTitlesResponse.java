package org.example.backend.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Value;

/**
 * Data Transfer Object for borrowed book titles responses.
 * Contains only the title of books that have been borrowed.
 */
@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BorrowedTitlesResponse {
    /**
     * Title of the book that has been borrowed.
     */
    String title;

    /**
     * Constructs a BorrowedTitlesResponse with the specified title.
     * 
     * @param title the title of the borrowed book
     */
    public BorrowedTitlesResponse(String title) {
        this.title = title;
    }
}
