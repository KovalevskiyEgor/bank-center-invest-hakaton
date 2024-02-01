package com.bank.models;


import com.bank.utils.enums.PostType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "posts")
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    @Column(name = "total_stars")
    @Builder.Default
    private Double totalStars=0.0;

    @Column(name = "review_counter")
    @Builder.Default
    private Integer reviewCounter=0;

    @Column(name = "rating")
    @Builder.Default
    private Double rating=0.0;

    @Column(name = "date_of_publishment")
    private Instant dateOfPublish;

    @Column(name = "date_of_begin")
    private Instant dateOfBegin;

    @Column(name = "date_of_end")
    private Instant dateOfEnd;

    private PostType type;

    @Column(name = "image")
    @CollectionTable(name = "posts_images")
    @ElementCollection
    private List<String> images;

    @OneToMany(mappedBy = "post")
    private List<Review> reviews;


    public double getRating(){
        if (this.totalStars != null && this.reviewCounter != null && this.reviewCounter != 0) {
            rating = this.totalStars / this.reviewCounter;
        }
        return rating;
    }

}

