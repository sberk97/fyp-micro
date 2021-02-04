package com.berk.advertservice.controller;

import com.berk.advertservice.model.AddAdvert;
import com.berk.advertservice.model.Advert;
import com.berk.advertservice.service.AdvertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AdvertController {

    @Resource(name = "advertService")
    private AdvertService advertServiceService;

    @PostMapping(value = "/addAdvert")
    public ResponseEntity<?> addAdvert(@RequestBody AddAdvert advert) {
        advertServiceService.addAdvert(advert);
        return ResponseEntity.ok("Advert has been added");
    }

    @GetMapping(value = "/getAll")
    public List<Advert> getAll() {
        return advertServiceService.getAll();
    }
}
