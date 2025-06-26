package org.example.backend.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;
import org.example.backend.model.User;

import java.sql.Date;

/**
 * Data Transfer Object for user information responses.
 * Contains user data formatted for API responses.
 */
@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "User information response")
public class UserInformationResponse {
    /**
     * User's name.
     */
    @Schema(description = "User name", example = "John")
    String name;
    
    /**
     * Date when the user became a member of the library.
     */
    @Schema(description = "Membership date", example = "2024-01-15")
    Date membershipDate;
    
    /**
     * Number of books currently borrowed by the user.
     */
    @Schema(description = "Number of borrowed books", example = "2")
    int numberOfBorrowedBooks;

    /**
     * Constructs a UserInformationResponse from a User entity.
     * 
     * @param user the user entity to convert
     */
    public UserInformationResponse(User user) {
        this.name = user.getName();
        this.membershipDate = user.getMembershipDate();
        this.numberOfBorrowedBooks = user.getNumberOfBorrowedBooks();
    }
}
