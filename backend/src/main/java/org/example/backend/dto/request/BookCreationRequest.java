package org.example.backend.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

/**
 * Data Transfer Object for creating a new book.
 * Contains validation annotations to ensure data integrity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BookCreationRequest {
    /**
     * Title of the book. Must start with uppercase letter and contain at least 3 letters.
     * Validated to ensure it's not blank, not null, and matches the pattern.
     */
    @NotBlank(message = "Book's title is required")
    @NotNull(message = "Book's title is required")
    @Pattern(regexp = "^[A-Z][a-zA-Z]{2,}$", message = "Wrong book's title input")
    private String title;
    
    /**
     * Author of the book. Must be in format "FirstName LastName" with proper capitalization.
     * Validated to ensure it's not blank, not null, and matches the pattern.
     */
    @NotBlank(message = "Book author's name and surname is required")
    @NotNull(message = "Book author's name and surname is required")
    @Pattern(regexp = "^[A-Z][a-z]+ [A-Z][a-z]+$", message = "Wrong book author's name and surname input")
    private String author;
}
