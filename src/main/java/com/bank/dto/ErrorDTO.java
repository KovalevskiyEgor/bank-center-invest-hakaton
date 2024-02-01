package com.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Error dto")
public class ErrorDTO {

    @Schema(name = "Error cause", example = "404")
    String error;

    @Schema(name = "Error message", example = "Not found")
    String message;
}
