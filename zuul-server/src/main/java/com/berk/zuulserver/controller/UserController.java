package com.berk.zuulserver.controller;

import com.berk.zuulserver.model.RegisterUser;
import com.berk.zuulserver.model.ReturnUserDetails;
import com.berk.zuulserver.service.MyUserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserController {

    @Resource(name = "userService")
    private MyUserDetailsService userService;

    @PostMapping(value = "/register")
    public ResponseEntity<String> createNewUser(@Valid @RequestBody RegisterUser user) {
        boolean userExists = userService.userExists(user.getUsername());
        if (userExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(user.getUsername() + " is already used.");
        }
        userService.saveUser(user);
        return ResponseEntity.ok("User created successfully.");
    }

    @GetMapping(value = "/getUser")
    public ReturnUserDetails fetchUserDetails() {
        ReturnUserDetails userReturnData = new ReturnUserDetails();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userReturnData.setUsername(auth.getName());
        int id = userService.getIdByUsername(auth.getName());
        userReturnData.setId(id);
        return userReturnData;
    }

    @DeleteMapping(value = "/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
