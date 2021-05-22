package com.berk.zuulserver.controller;

import com.berk.zuulserver.model.AuthenticationRequest;
import com.berk.zuulserver.model.AuthenticationResponse;
import com.berk.zuulserver.model.RegisterUser;
import com.berk.zuulserver.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import javax.annotation.Resource;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Sql(value = "/insert_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@Sql(value = "/clean_database.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserControllerIT {

    @LocalServerPort
    private int serverPort;

    @Autowired
    private TestRestTemplate restTemplate;

    private URI createServerAddress(String endpoint) throws URISyntaxException {
        return new URI("http://localhost:" + serverPort + "/api/" + endpoint);
    }

    @Test
    void shouldRegisterUser() throws Exception {
        // given:
        RegisterUser registerUser = new RegisterUser("user123", "password");

        // when:
        RequestEntity<RegisterUser> request = RequestEntity
                .post(createServerAddress("register"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(registerUser);

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        // then:
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("User created successfully.", response.getBody());

        // given:
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("user123", "password");

        // when:
        RequestEntity<AuthenticationRequest> request2 = RequestEntity
                .post(createServerAddress("authenticate"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(authenticationRequest);

        ResponseEntity<AuthenticationResponse> response2 = restTemplate.exchange(request2, AuthenticationResponse.class);

        // then:
        assertTrue(response2.getStatusCode().is2xxSuccessful());
        assertNotNull(response2.getBody().getJwt());
    }
}
