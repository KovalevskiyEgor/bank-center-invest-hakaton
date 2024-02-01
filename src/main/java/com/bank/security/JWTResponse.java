package com.bank.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "Authorization response")
public final class JWTResponse {
    @Schema(name = "User id", example = "1")
    private Long id;
    @Schema(name = "Username", example = "user123")
    private String username;
    @Schema(name = "JWT access token", example = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJyb21hMyIsImlkIjoxLCJyb2xlcyI6WyJVU0VSIiwiQURNSU4iXSwiaWF0IjoxNjk2MTA4ODUxLCJleHAiOjE2OTg3MDA4NTF9.PM8hcus9h8rEhRvx30y8UxDMcYzR9tQG6xc_7rbiGt3EMjnJfo9rucDjYYxKCroc")
    private String accessToken;
    @Schema(name = "JWT refresh token", example = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJyb21hMyIsImlkIjoxLCJyb2xlcyI6WyJVU0VSIiwiQURNSU4iXSwiaWF0IjoxNjk2MTA4ODUxLCJleHAiOjE2OTg3MDA4NTF9.PM8hcus9h8rEhRvx30y8UxDMcYzR9tQG6xc_7rbiGt3EMjnJfo9rucDjYYxKCroc")
    private String refreshToken;
}
