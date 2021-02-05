package com.berk.advertservice.service;

import com.berk.advertservice.model.ReturnUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserDetailsService {
    private WebClient webClient;

    private HttpServletRequest request;

    @Value("${common.zuul.url}")
    private String requestUrl;

    @Autowired
    public UserDetailsService(WebClient.Builder webClientBuilder, HttpServletRequest request) {
        this.webClient = webClientBuilder.build();
        this.request = request;
    }

    public ReturnUserDetails getCurrentUser() {
        return webClient.get()
                .uri(requestUrl)
                .header("Authorization", request.getHeader("Authorization"))
                .retrieve()
                .bodyToMono(ReturnUserDetails.class)
                .block();
    }
}
