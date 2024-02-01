package com.bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Post DTO")
public class PostDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(name = "id", example = "1")
    private Long id;

    @Schema(name = "title", example = "Title for post")
    @Size(max = 100, message = "Title size should be under 100 characters")
    @NotNull
    private String title;

    @Schema(name = "description", example = "Description for post")
    @NotNull
    private String description;

    @Schema(name = "location")
    private LocationDTO location;

    @Schema(name = "type", example = "LANDMARK")
    private String type;

    @Schema(name = "rating", example = "1.2")
    private Double rating;

    @Schema(name = "date_of_publish", example = "timestamp")
    @JsonProperty(value = "date_of_publish",
            access = JsonProperty.Access.READ_ONLY)
    private Instant dateOfPublish;

    @Schema(name = "date_of_begin", example = "timestamp")
    @JsonProperty(value = "date_of_begin")
    private Instant dateOfBegin;

    @Schema(name = "date_of_end", example = "timestamp")
    @JsonProperty(value = "date_of_end")
    private Instant dateOfEnd;
}
