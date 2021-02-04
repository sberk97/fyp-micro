package com.berk.advertismentservice.controller;

import com.berk.advertismentservice.model.AddAdvertisment;
import com.berk.advertismentservice.model.Advertisment;
import com.berk.advertismentservice.service.AdvertismentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AdvertismentController {

    @Resource(name = "advertismentService")
    private AdvertismentService advertismentServiceService;

    @PostMapping(value = "/addAdvertisment")
    public ResponseEntity<?> addAdvertisment(@RequestBody AddAdvertisment advertisment) {
        advertismentServiceService.addAdvertisment(advertisment);
        return ResponseEntity.ok("Advertisment has been added");
    }

    @GetMapping(value = "/getAll")
    public List<Advertisment> getAll() {
        return advertismentServiceService.getAll();
    }
}
