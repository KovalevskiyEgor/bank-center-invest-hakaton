package com.bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "User DTO")
public class UserDTO {

    @Schema(name = "id", example = "1")
    private Long id;

    @Schema(name = "username", example = "mkr")
    private String username;

    @Schema(name = "email", example = "mrk@gmail.com")
    private String email;

    @Schema(name = "password", example = "12345")
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private String password;

    @Schema(name = "password_confirmation", example = "12345")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY,
            value = "password_confirmation")
    private String passwordConfirmation;

    @Schema(name = "points", example = "12345")
    private Integer points;

    @Schema(name = "rank", example = "New member")
    private String rank;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY,
            value = "confirmation_code")
    @Schema(name = "confirmation_code", example = "5328")
    private String confirmationCode;

    @Schema(name = "posts")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<PostDTO> posts;

    @Schema(name = "reviews")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<ReviewDTO> reviews;

}
