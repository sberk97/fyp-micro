package com.berk.advertservice.controller;

import com.berk.advertservice.model.AddAdvert;
import com.berk.advertservice.model.Advert;
import com.berk.advertservice.service.AdvertService;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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

//    @GetMapping(value = "/adverts")
//    public ResponseEntity<List<Advert>> getAdverts(
//            @And({
//                    @Spec(path = "firstName", spec = Equal.class),
//                    @Spec(path = "lastName", spec = Equal.class),
//                    @Spec(path = "status", spec = Equal.class)
//            }) Specification<Advert> advertSpec) {
//
//        return ResponseEntity.of(advertService.getAdverts(advertSpec));
//    }

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
