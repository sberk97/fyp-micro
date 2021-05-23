package com.berk.zuulserver;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class OtherApiHooks {

    @GetMapping(value = {"/adverts/{id}", "/adverts/users/{id}", "/adverts-latest", "/adverts"})
    public ResponseEntity<Void> handleGet() {
        assert (false); // this function is meant to be mocked, not called
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PostMapping(value = "/adverts")
    public ResponseEntity<Void> handlePost() {
        assert (false); // this function is meant to be mocked, not called
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PutMapping(value = "/adverts/{id}")
    public ResponseEntity<Void> handlePut() {
        assert (false); // this function is meant to be mocked, not called
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    @DeleteMapping(value = {"/adverts/{id}", "/adverts/users/{id}"})
    public ResponseEntity<Void> handleDelete() {
        assert (false); // this function is meant to be mocked, not called
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }
}
