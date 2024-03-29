package com.berk.advertservice.service;

import com.berk.advertservice.model.AddAdvert;
import com.berk.advertservice.model.Advert;
import com.berk.advertservice.model.AdvertRepository;
import com.berk.advertservice.model.ReturnUserDetails;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service("advertService")
public class AdvertService {

    final AdvertRepository advertRepository;

    @Resource(name = "userDetailsService")
    private UserDetailsService userDetailsService;

    public AdvertService(AdvertRepository advertRepository) {
        this.advertRepository = advertRepository;
    }

    public Optional<Advert> getAdvertById(int id) {
        return advertRepository.findById(id);
    }

    public Optional<List<Advert>> getAdvertsByUserId(int id) {
        return advertRepository.findAllByUserId(id);
    }

    public Optional<List<Advert>> getLastNAdverts(int numberOfAdvertsRequested) {
        Pageable pageable = PageRequest.of(0, numberOfAdvertsRequested, Sort.Direction.DESC, "creationDate");
        return advertRepository.findByOrderByCreationDateDesc(pageable);
    }

    public int addAdvert(AddAdvert advert) {
        ReturnUserDetails user = userDetailsService.getCurrentUser();
        var newAdvert = new Advert();
        newAdvert.setTitle(advert.getTitle());
        newAdvert.setDescription(advert.getDescription());
        newAdvert.setPrice(advert.getPrice());
        newAdvert.setUserId(user.getId());
        newAdvert.setUsername(user.getUsername());
        newAdvert.setCreationDate(LocalDateTime.now());
        newAdvert.setContactDetails(advert.getContactDetails());
        return advertRepository.saveAndFlush(newAdvert).getId();
    }

    public Optional<Integer> editAdvert(AddAdvert advert, int id) {
        var advertOptional = advertRepository.findById(id);
        if (advertOptional.isPresent()) {
            ReturnUserDetails user = userDetailsService.getCurrentUser();
            if (Arrays.asList(user.getRoles()).contains("ROLE_ADMIN") || user.getId() == advertOptional.get().getUserId()) {
                var updatedAdvert = advertOptional.get();
                updatedAdvert.setTitle(advert.getTitle());
                updatedAdvert.setDescription(advert.getDescription());
                updatedAdvert.setPrice(advert.getPrice());
                updatedAdvert.setContactDetails(advert.getContactDetails());
                return Optional.of(advertRepository.saveAndFlush(updatedAdvert).getId());
            }
        }
        return Optional.empty();
    }

    public Optional<List<Advert>> findAllByTitle(String title) {
        var caseInsensitiveExampleMatcher = ExampleMatcher.matchingAny().withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        var advert = new Advert();
        advert.setTitle(title);
        Example<Advert> example = Example.of(advert, caseInsensitiveExampleMatcher);
        List<Advert> advertList = advertRepository.findAll(example);
        return advertList.isEmpty() ? Optional.empty() : Optional.of(advertList);
    }

    public boolean deleteAdvert(int id) {
        Optional<Advert> advert = advertRepository.findById(id);

        if (advert.isPresent()) {
            ReturnUserDetails user = userDetailsService.getCurrentUser();
            if (Arrays.asList(user.getRoles()).contains("ROLE_ADMIN") || user.getId() == advert.get().getUserId()) {
                advertRepository.deleteById(id);
                return true;
            }
        }

        return false;
    }

    public void deleteAdvertsByUserId(int id) {
        advertRepository.deleteByUserId(id);
    }
}
