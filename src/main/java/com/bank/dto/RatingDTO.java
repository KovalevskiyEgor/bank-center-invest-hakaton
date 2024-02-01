package com.bank.dto;

import com.bank.utils.enums.UserRank;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class RatingDTO {
    @Enumerated(EnumType.STRING)
    private UserRank rank;

    private Integer points;
}
