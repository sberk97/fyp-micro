package com.berk.zuulserver.controller;

import com.berk.zuulserver.service.MyUserDetailsService;
import com.berk.zuulserver.model.RegisterUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api")
public class RegistrationController {

    @Resource(name = "userService")
    private MyUserDetailsService userService;

    @PostMapping(value = "/register")
    public ResponseEntity<String> createNewUser(@RequestBody RegisterUser user) {
        boolean userExists = userService.userExists(user.getUsername());
        if (userExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(user.getUsername() + " is already used.");
        }
        userService.saveUser(user);
        return ResponseEntity.ok("User created succesfully.");
    }

}
