package com.berk.advertservice.controller;

import com.berk.advertservice.model.ReturnUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OtherApiHooks {

    @GetMapping("/api/user")
    public ResponseEntity<ReturnUserDetails> handleGet() {
        assert (false); // this function is meant to be mocked, not called
        return new ResponseEntity<ReturnUserDetails>(HttpStatus.NOT_IMPLEMENTED);
    }
}
