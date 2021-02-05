package com.berk.advertservice.service;

import com.berk.advertservice.model.AddAdvert;
import com.berk.advertservice.model.Advert;
import com.berk.advertservice.model.AdvertRepository;
import com.berk.advertservice.model.ReturnUserDetails;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AdvertService {

    final AdvertRepository advertRepository;

    @Resource(name = "userDetailsService")
    private UserDetailsService userDetailsService;

    public AdvertService(AdvertRepository advertRepository) {
        this.advertRepository = advertRepository;
    }

    public void addAdvert(AddAdvert advert) {
        ReturnUserDetails user = userDetailsService.getCurrentUser();
        Advert newAdvert = new Advert();
        newAdvert.setTitle(advert.getTitle());
        newAdvert.setDescription(advert.getDescription());
        newAdvert.setPrice(advert.getPrice());
        newAdvert.setUserId(user.getId());
        newAdvert.setUsername(user.getUsername());
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

    public boolean deleteAdvert(int id) {
        Optional<Advert> advert = advertRepository.findById(id);

        if (advert.isPresent()) {
            ReturnUserDetails user = userDetailsService.getCurrentUser();
            if (user.getId() == advert.get().getUserId()) {
                advertRepository.deleteById(id);
                return true;
            }
        }

        return false;
    }
}
