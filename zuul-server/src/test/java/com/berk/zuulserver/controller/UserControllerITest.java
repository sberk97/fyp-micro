package com.berk.zuulserver.controller;

import com.berk.zuulserver.model.*;
import com.berk.zuulserver.util.JwtUtil;
import io.jsonwebtoken.Claims;
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
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "/insert_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/clean_database.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserControllerITest {

    @LocalServerPort
    private int serverPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Resource(name = "jwtUtlService")
    private JwtUtil jwtTokenUtil;

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

        String jwtToken = response2.getBody().getJwt();

        // then:
        assertTrue(response2.getStatusCode().is2xxSuccessful());
        assertNotNull(jwtToken);

        User user = new User();
        user.setUsername("user123");
        user.setPassword("password");
        user.setId(1);
        user.setRoles("ROLE_USER");

        Claims claims = jwtTokenUtil.extractAllClaims(jwtToken);
        assertTrue(jwtTokenUtil.validateToken(jwtToken, new MyUserDetails(user)));
        assertEquals(1, claims.get("userId"));
        assertEquals("ROLE_USER", claims.get("roles"));
    }

    @Test
    void shouldNotRegisterUserTooShortCredentials() throws Exception {
        // given:
        RegisterUser registerUser = new RegisterUser("user", "pass");

        // when:
        RequestEntity<RegisterUser> request = RequestEntity
                .post(createServerAddress("register"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(registerUser);

        ResponseEntity<HashMap> response = restTemplate.exchange(request, HashMap.class);

        // then:
        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("Username must be between 6 and 30 characters", response.getBody().get("username"));
        assertEquals("Password must be between 6 and 100 characters", response.getBody().get("password"));
    }

    @Test
    void shouldNotRegisterUserTooLongCredentials() throws Exception {
        // given:
        RegisterUser registerUser = new RegisterUser("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa1", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa1");

        // when:
        RequestEntity<RegisterUser> request = RequestEntity
                .post(createServerAddress("register"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(registerUser);

        ResponseEntity<HashMap> response = restTemplate.exchange(request, HashMap.class);

        // then:
        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("Username must be between 6 and 30 characters", response.getBody().get("username"));
        assertEquals("Password must be between 6 and 100 characters", response.getBody().get("password"));
    }


}
