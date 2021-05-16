package com.berk.advertservice.model;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdvertRepository extends JpaRepository<Advert, Integer> {
    Optional<Advert> findById(int id);
    Optional<List<Advert>> findAllByUserId(int id);
//    Optional<List<Advert>> findAll(Example<S> example);
//    Optional<List<Advert>> findAll(Specification<Advert> advertSpec);
}

