package com.berk.advertismentservice.service;

import com.berk.advertismentservice.model.AddAdvertisment;
import com.berk.advertismentservice.model.Advertisment;
import com.berk.advertismentservice.model.AdvertismentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdvertismentService {

    final AdvertismentRepository advertismentRepository;

    public AdvertismentService(AdvertismentRepository advertismentRepository) {
        this.advertismentRepository = advertismentRepository;
    }

    public void addAdvertisment(AddAdvertisment advertisment) {
        Advertisment newAdvertisment = new Advertisment();
        newAdvertisment.setTitle(advertisment.getTitle());
        newAdvertisment.setDescription(advertisment.getDescription());
        newAdvertisment.setPrice(advertisment.getPrice());
        newAdvertisment.setUserId(1);
        newAdvertisment.setCreationDate(LocalDateTime.now());
        advertismentRepository.save(newAdvertisment);
    }

    public List<Advertisment> getAll() {
        return advertismentRepository.findAll();
    }
}
