package com.berk.advertservice.service;

import com.berk.advertservice.model.AddAdvert;
import com.berk.advertservice.model.Advert;
import com.berk.advertservice.model.AdvertRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdvertService {

    final AdvertRepository advertRepository;

    public AdvertService(AdvertRepository advertRepository) {
        this.advertRepository = advertRepository;
    }

    public void addAdvert(AddAdvert advert) {
        Advert newAdvert = new Advert();
        newAdvert.setTitle(advert.getTitle());
        newAdvert.setDescription(advert.getDescription());
        newAdvert.setPrice(advert.getPrice());
        newAdvert.setUserId(1);
        newAdvert.setCreationDate(LocalDateTime.now());
        advertRepository.save(newAdvert);
    }

    public List<Advert> getAll() {
        return advertRepository.findAll();
    }

    public List<Advert> findAllByTitle(String title) {
        ExampleMatcher caseInsensitiveExampleMatcher = ExampleMatcher.matchingAny().withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Advert advert = new Advert();
        advert.setTitle(title);
        Example<Advert> example = Example.of(advert, caseInsensitiveExampleMatcher);

        return advertRepository.findAll(example);
    }
}
