package com.bank.repositories;

import com.bank.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location,Long> {
    Optional<Location> findByAddress(String address);
    Optional<Location> findByCoordinates(String coordinates);
}
