package com.berk.advertservice.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdvertRepository extends JpaRepository<Advert, Integer> {
    Optional<Advert> findById(int id);
    Optional<Advert> findByUserId(int userId);
    Optional<Advert> findAllByTitle(String title);
    List<Advert> findAll();
}

