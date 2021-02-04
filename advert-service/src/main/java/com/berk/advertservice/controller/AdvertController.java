package com.berk.advertservice.controller;

import com.berk.advertservice.model.AddAdvert;
import com.berk.advertservice.model.Advert;
import com.berk.advertservice.service.AdvertService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
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
    public List<Advert> findByTitle(@RequestParam String title) {
        return advertService.findAllByTitle(title);
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
