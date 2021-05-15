package com.berk.advertservice.controller;

import com.berk.advertservice.model.AddAdvert;
import com.berk.advertservice.model.Advert;
import com.berk.advertservice.service.AdvertService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@Validated
public class AdvertController {

    @Resource(name = "advertService")
    private AdvertService advertService;

    @PostMapping(value = "/addAdvert")
    public ResponseEntity<?> addAdvert(@Valid @RequestBody AddAdvert advert) {
        advertService.addAdvert(advert);
        return ResponseEntity.ok("Advert has been added");
    }

    @GetMapping(value = "/getAll")
    public List<Advert> getAll() {
        return advertService.getAll();
    }

    @GetMapping(value = "/findByTitle")
    public List<Advert> findByTitle(@RequestParam
                                    @NotBlank(message = "Title should not be empty.")
                                    @Size(min = 6, max = 60, message = "Title must be between 6 and 60 characters")
                                            String title) {
        return advertService.findAllByTitle(title);
    }

    @DeleteMapping(value = "/deleteAdvert/{id}")
    public ResponseEntity<?> deleteAdvert(@PathVariable int id) {
        boolean isRemoved = advertService.deleteAdvert(id);

        if (!isRemoved) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Advert not found.");
        }

        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @GetMapping(value = "/hasupdated")
    public String getUpdate() {
        return "version 1";
    }
}
