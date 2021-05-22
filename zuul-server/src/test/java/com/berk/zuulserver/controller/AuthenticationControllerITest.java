package com.berk.zuulserver.controller;

import com.berk.zuulserver.model.AuthenticationRequest;
import com.berk.zuulserver.model.AuthenticationResponse;
import com.berk.zuulserver.model.MyUserDetails;
import com.berk.zuulserver.model.User;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "/insert_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/clean_database.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AuthenticationControllerITest {

    @LocalServerPort
    private int serverPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Resource(name = "jwtUtlService")
    private JwtUtil jwtTokenUtil;

    private URI createServerAddress() throws URISyntaxException {
        return new URI("http://localhost:" + serverPort + "/api/authenticate");
    }

    @Test
    void shouldReturnJwtToken() throws Exception {
        // given:
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("admin1", "password");

        // when:
        RequestEntity<AuthenticationRequest> request = RequestEntity
                .post(createServerAddress())
                .contentType(MediaType.APPLICATION_JSON)
                .body(authenticationRequest);

        ResponseEntity<AuthenticationResponse> response = restTemplate.exchange(request, AuthenticationResponse.class);

        String jwtToken = response.getBody().getJwt();

        // then:
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(jwtToken);

        User user = new User();
        user.setUsername("admin1");
        user.setPassword("password");
        user.setId(101);
        user.setRoles("ROLE_ADMIN");

        Claims claims = jwtTokenUtil.extractAllClaims(jwtToken);
        assertTrue(jwtTokenUtil.validateToken(jwtToken, new MyUserDetails(user)));
        assertEquals(101, claims.get("userId"));
        assertEquals("ROLE_ADMIN", claims.get("roles"));
    }

    @Test
    void shouldReturn4xxWhenWrongPassword() throws Exception {
        // given:
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("admin1", "wrong");

        // when:
        RequestEntity<AuthenticationRequest> request = RequestEntity
                .post(createServerAddress())
                .contentType(MediaType.APPLICATION_JSON)
                .body(authenticationRequest);

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        // then:
        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("Wrong username or password.", response.getBody());
    }

    @Test
    void shouldReturn4xxWhenWrongUsername() throws Exception {
        // given:
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("wrong", "password");

        // when:
        RequestEntity<AuthenticationRequest> request = RequestEntity
                .post(createServerAddress())
                .contentType(MediaType.APPLICATION_JSON)
                .body(authenticationRequest);

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        // then:
        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("Wrong username or password.", response.getBody());
    }
}
