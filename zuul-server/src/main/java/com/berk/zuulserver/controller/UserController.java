package com.berk.zuulserver.controller;

import com.berk.zuulserver.model.RegisterUser;
import com.berk.zuulserver.model.ReturnUserDetails;
import com.berk.zuulserver.model.User;
import com.berk.zuulserver.service.MyUserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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

    @GetMapping(value = "/user")
    public ResponseEntity<ReturnUserDetails> getLoggedInUserDetails() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userService.getUserByUsername(auth.getName());
        if (user.isPresent()) {
            var userReturnData = new ReturnUserDetails();
            userReturnData.setUsername(user.get().getUsername());
            userReturnData.setId(user.get().getId());
            userReturnData.setRoles(user.get().getRoles());

            return ResponseEntity.of(Optional.of(userReturnData));
        }
        return ResponseEntity.of(Optional.empty());
    }

    @GetMapping(value = {"/users","/users/{id}"})
    public ResponseEntity<List<User>> getUser(@PathVariable(required = false) Integer id) {
        if (id != null) {
            return ResponseEntity.of(userService.getUserById(id));
        } else {
            return ResponseEntity.of(userService.getUsers());
        }
    }

    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}