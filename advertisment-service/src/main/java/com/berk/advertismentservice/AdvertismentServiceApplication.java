package com.berk.advertismentservice;

import com.berk.advertismentservice.model.AdvertismentRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaRepositories(basePackageClasses = AdvertismentRepository.class)
public class AdvertismentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdvertismentServiceApplication.class, args);
	}

}
