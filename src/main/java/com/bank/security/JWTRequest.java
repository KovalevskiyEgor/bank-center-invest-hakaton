package com.bank.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "Authorization request")
public class JWTRequest {
    @Schema(name = "Username", example = "user123")
    private String username;
    @Schema(name = "User password", example = "12345")
    private String password;
}
