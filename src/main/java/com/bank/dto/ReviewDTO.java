package com.bank.dto;

import com.bank.models.Post;
import com.bank.models.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Review DTO")
public class ReviewDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(name = "id", example = "1")
    private Long id;

    @Size(max = 100, message = "Title size should be under 100 characters")
    @Schema(name = "title", example = "Title for post")
    private String title;

    @Schema(name = "message", example = "Message for review")
    private String message;

    @Schema(name = "stars", example = "1")
    @Min(value = 1, message = "Minimum count of stars is 1")
    @Max(value = 5, message = "Maximum count of stars is 5")
    private Long stars;

    @Schema(name = "likes", example = "100")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long likes;

    @JsonProperty(value = "creation_time", access = JsonProperty.Access.READ_ONLY)
    private Instant creationTime;
}
