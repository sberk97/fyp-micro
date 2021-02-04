package com.berk.advertservice.controller;

import com.berk.advertservice.model.AddAdvert;
import com.berk.advertservice.model.Advert;
import com.berk.advertservice.service.AdvertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class AdvertController {

    @Resource(name = "advertService")
    private AdvertService advertService;

    @PostMapping(value = "/addAdvert")
    public ResponseEntity<?> addAdvert(@RequestBody AddAdvert advert) {
        advertService.addAdvert(advert);
        return ResponseEntity.ok("Advert has been added");
    }

    @GetMapping(value = "/getAll")
    public List<Advert> getAll() {
        return advertService.getAll();
    }

    @GetMapping(value = "/findByTitle")
    public List<Advert> findByTitle(@RequestParam String title) {
        List<Advert> adverts = advertService.findAllByTitle(title);

        if (adverts.isEmpty()) {
            //return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Adverts not found.")
        }

        return adverts;
    }
}
