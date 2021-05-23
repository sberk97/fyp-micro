package com.berk.advertservice.controller;

import com.berk.advertservice.model.AddAdvert;
import com.berk.advertservice.model.Advert;
import com.berk.advertservice.service.AdvertService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@Validated
public class AdvertController {
    @Value("${eureka.instance.instance-id}")
    private String instance;

    @Resource(name = "advertService")
    private AdvertService advertService;

    @GetMapping(value = "/instance")
    public String getInstance() {
        return "Hello from: " + instance;
    }

    @GetMapping(value = "/adverts/{id}")
    public ResponseEntity<Advert> getAdvertById(@PathVariable int id) {
        return ResponseEntity.of(advertService.getAdvertById(id));
    }

    @GetMapping(value = "/adverts/users/{id}")
    public ResponseEntity<List<Advert>> getAdvertsByUserId(@PathVariable int id) {
        return ResponseEntity.of(advertService.getAdvertsByUserId(id));
    }

    @GetMapping(value = "/adverts-latest")
    public ResponseEntity<List<Advert>> getLastNAdverts(@RequestParam int last) {
        return ResponseEntity.of(advertService.getLastNAdverts(last));
    }

    @GetMapping(value = "/adverts")
    public ResponseEntity<List<Advert>> findAdvertsByTitle(@RequestParam
                                                           @Size(max = 60, message = "Title can be up to 60 characters")
                                                                   String title) {
        return ResponseEntity.of(advertService.findAllByTitle(title));
    }

    @PostMapping(value = "/adverts")
    public ResponseEntity<Integer> addAdvert(@Valid @RequestBody AddAdvert advert) {
        return new ResponseEntity<>(advertService.addAdvert(advert), HttpStatus.OK);
    }

    @PutMapping(value = "/adverts/{id}")
    public ResponseEntity<Integer> editAdvert(@Valid @RequestBody AddAdvert advert, @PathVariable int id) {
        return ResponseEntity.of(advertService.editAdvert(advert, id));
    }

    @DeleteMapping(value = "/adverts/{id}")
    public ResponseEntity<?> deleteAdvert(@PathVariable int id) {
        boolean isRemoved = advertService.deleteAdvert(id);

        if (!isRemoved) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Advert not found.");
        }

        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping(value = "/adverts/users/{id}")
    public void deleteAdvertsByUserId(@PathVariable int id) {
        advertService.deleteAdvertsByUserId(id);
    }
}
