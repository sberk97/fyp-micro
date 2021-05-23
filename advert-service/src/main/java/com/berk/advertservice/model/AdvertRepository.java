package com.berk.advertservice.model;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdvertRepository extends JpaRepository<Advert, Integer> {
    Optional<Advert> findById(int id);

    Optional<List<Advert>> findAllByUserId(int id);

    Optional<List<Advert>> findByOrderByCreationDateDesc(Pageable pageable);

    void deleteByUserId(int id);
}

