package com.bank.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "locations")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {

    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "address")
    private String address;

    @Column(name = "coordinates")
    private String coordinates;

    @OneToMany(mappedBy = "location")
    private List<Post> post;
}
