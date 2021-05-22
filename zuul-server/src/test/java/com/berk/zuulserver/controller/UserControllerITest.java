package com.berk.zuulserver.controller;

import com.berk.zuulserver.model.*;
import com.berk.zuulserver.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import javax.annotation.Resource;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

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

    private String getJwt(String username, String password) throws URISyntaxException {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(username, password);

        // when:
        RequestEntity<AuthenticationRequest> request = RequestEntity
                .post(createServerAddress("authenticate"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(authenticationRequest);

        ResponseEntity<AuthenticationResponse> response = restTemplate.exchange(request, AuthenticationResponse.class);

        return response.getBody().getJwt();
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
        String jwtToken = getJwt("user123", "password");

        // then:
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
        // given: 31 and 101
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

    @Test
    void shouldNotRegisterTakenUsername() throws Exception {
        // given:
        RegisterUser registerUser = new RegisterUser("user12", "password");

        // when:
        RequestEntity<RegisterUser> request = RequestEntity
                .post(createServerAddress("register"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(registerUser);

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        // then:
        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("user12 is already used.", response.getBody());
    }

    @Test
    void shouldReturnUserDetailsForAdmin() throws Exception {
        // given:
        String jwtToken = getJwt("admin1", "password");

        // when:
        RequestEntity<Void> request = RequestEntity
                .get(createServerAddress("user"))
                .header("Authorization", "Bearer " + jwtToken)
                .build();

        ResponseEntity<ReturnUserDetails> response = restTemplate.exchange(request, ReturnUserDetails.class);
        ReturnUserDetails userDetails = response.getBody();

        // then:
        assertEquals(101, userDetails.getId());
        assertEquals("admin1", userDetails.getUsername());
        assertEquals("ROLE_ADMIN", userDetails.getRoles()[0]);
    }

    @Test
    void shouldReturnUserDetailsForUser() throws Exception {
        // given:
        String jwtToken = getJwt("user12", "password");

        // when:
        RequestEntity<Void> request = RequestEntity
                .get(createServerAddress("user"))
                .header("Authorization", "Bearer " + jwtToken)
                .build();

        ResponseEntity<ReturnUserDetails> response = restTemplate.exchange(request, ReturnUserDetails.class);
        ReturnUserDetails userDetails = response.getBody();

        // then:
        assertEquals(102, userDetails.getId());
        assertEquals("user12", userDetails.getUsername());
        assertEquals("ROLE_USER", userDetails.getRoles()[0]);
    }

    @Test
    void shouldReturn4xxWhenNoJwtTryingToGetUserDetails() throws Exception {
        // when:
        RequestEntity<Void> request = RequestEntity
                .get(createServerAddress("user"))
                .header("Authorization", "Bearer ")
                .build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        // then:
        assertTrue(response.getStatusCode().is4xxClientError());
    }

    @Test
    void shouldReturnUserDetailsByIdForNotLoggedIn() throws Exception {
        // when:
        RequestEntity<Void> request = RequestEntity
                .get(createServerAddress("users/101"))
                .build();

        ResponseEntity<ReturnUserDetails> response = restTemplate.exchange(request, ReturnUserDetails.class);
        ReturnUserDetails userDetails = response.getBody();

        // then:
        assertEquals(101, userDetails.getId());
        assertEquals("admin1", userDetails.getUsername());
        assertEquals("ROLE_ADMIN", userDetails.getRoles()[0]);
    }

    @Test
    void shouldReturn4xxWhenNoUserWithGivenId() throws Exception {
        // when:
        RequestEntity<Void> request = RequestEntity
                .get(createServerAddress("users/999"))
                .build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        // then:
        assertTrue(response.getStatusCode().is4xxClientError());
    }

    @Test
    void shouldReturnAllUsersForAdmin() throws Exception {
        // given:
        String jwtToken = getJwt("admin1", "password");

        // when:
        RequestEntity<Void> request = RequestEntity
                .get(createServerAddress("users"))
                .header("Authorization", "Bearer " + jwtToken)
                .build();

        ResponseEntity<List<ReturnUserDetails>> response = restTemplate.exchange(request, new ParameterizedTypeReference<List<ReturnUserDetails>>() {});
        List<ReturnUserDetails> userDetails = response.getBody();

        // then:
        assertEquals("admin1", userDetails.get(0).getUsername());
        assertEquals("user12", userDetails.get(1).getUsername());
    }

    @Test
    void shouldReturn4xxForUserAndNotLoggedInWhenGettingAllUsers() throws Exception {
        // given:
        String jwtToken = getJwt("user12", "password");

        // when:
        RequestEntity<Void> request = RequestEntity
                .get(createServerAddress("users"))
                .header("Authorization", "Bearer " + jwtToken)
                .build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        // then:
        assertTrue(response.getStatusCode().is4xxClientError());

        // when:
        request = RequestEntity
                .get(createServerAddress("users"))
                .build();

        response = restTemplate.exchange(request, String.class);

        // then:
        assertTrue(response.getStatusCode().is4xxClientError());
    }

    @Test
    void shouldDeleteUser() throws Exception {
        // given:
        String jwtToken = getJwt("admin1", "password");

        // when:
        RequestEntity<Void> request = RequestEntity
                .delete(createServerAddress("users/102"))
                .header("Authorization", "Bearer " + jwtToken)
                .build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        // then:
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("102", response.getBody());

        // when:
        request = RequestEntity
                .get(createServerAddress("users/102"))
                .build();

        response = restTemplate.exchange(request, String.class);

        // then:
        assertTrue(response.getStatusCode().is4xxClientError());
    }

    @Test
    void shouldReturn4xxForUserAndNotLoggedInWhenDeletingUser() throws Exception {
        // given:
        String jwtToken = getJwt("user12", "password");

        // when:
        RequestEntity<Void> request = RequestEntity
                .delete(createServerAddress("users/101"))
                .header("Authorization", "Bearer " + jwtToken)
                .build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        // then:
        assertTrue(response.getStatusCode().is4xxClientError());

        // when:
        request = RequestEntity
                .delete(createServerAddress("users/101"))
                .build();

        response = restTemplate.exchange(request, String.class);

        // then:
        assertTrue(response.getStatusCode().is4xxClientError());
    }
}
