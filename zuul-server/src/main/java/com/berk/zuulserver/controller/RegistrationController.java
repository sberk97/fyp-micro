package com.berk.zuulserver.controller;

import com.berk.zuulserver.service.MyUserDetailsService;
import com.berk.zuulserver.model.RegisterUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RegistrationController {

    @Resource(name = "userService")
    private MyUserDetailsService userService;

    @PostMapping(value = "/register")
    public ResponseEntity<String> createNewUser(@Valid @RequestBody RegisterUser user) {
        boolean userExists = userService.userExists(user.getUsername());
        if (userExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(user.getUsername() + " is already used.");
        }
        userService.saveUser(user);
        return ResponseEntity.ok("User created succesfully.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}
