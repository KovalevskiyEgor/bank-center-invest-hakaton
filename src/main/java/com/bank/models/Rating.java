package com.bank.models;

import com.bank.utils.enums.UserRank;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "users_rating")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rating {

    @Id
    @Column(name = "user_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "rank")
    @Enumerated(EnumType.STRING)
    private UserRank rank;

    @Column(name = "points")
    private Integer points;
}