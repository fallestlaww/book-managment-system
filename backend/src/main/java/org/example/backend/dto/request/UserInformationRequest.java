package org.example.backend.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

/**
 * Data Transfer Object for user information requests.
 * Used when operations require user identification by name.
 * Contains validation annotations to ensure data integrity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "User information request")
public class UserInformationRequest {
    /**
     * User's name. Must be a single word without spaces.
     * Validated to ensure it's not blank, not null, and matches the pattern.
     */
    @NotBlank(message = "User's name is required")
    @NotNull(message = "User's name is required")
    @Pattern(regexp = "^[^\\s]+$", message = "User's name must be a single word without spaces")
    @Schema(description = "User name", example = "John")
    private String name;
}
