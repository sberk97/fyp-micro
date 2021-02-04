package com.berk.advertismentservice.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdvertismentRepository extends JpaRepository<Advertisment, Integer> {
    Optional<Advertisment> findById(int id);
    Optional<Advertisment> findByUserId(int userId);
    Optional<Advertisment> findByTitle(String title);
    List<Advertisment> findAll();
}

