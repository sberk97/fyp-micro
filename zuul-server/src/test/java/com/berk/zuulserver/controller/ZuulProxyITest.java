package com.berk.zuulserver.controller;

import com.berk.zuulserver.OtherApiHooks;
import com.berk.zuulserver.model.AuthenticationRequest;
import com.berk.zuulserver.model.AuthenticationResponse;
import com.berk.zuulserver.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import javax.annotation.Resource;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(value = "/insert_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/clean_database.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ZuulProxyITest {

    @LocalServerPort
    private int serverPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Resource(name = "jwtUtlService")
    private JwtUtil jwtTokenUtil;

    @MockBean
    private OtherApiHooks otherApiHooks;

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

    @ParameterizedTest
    @ValueSource(strings = {"advert-service/adverts/1", "advert-service/adverts/users/1", "advert-service/adverts-latest", "advert-service/adverts"})
    void shouldRouteToGetAsUnauthorized(String path) throws Exception {
        // given:
        Mockito.doReturn(new ResponseEntity<String>("Route worked", HttpStatus.ACCEPTED))
                .when(otherApiHooks).handleGet();
        // when:
        RequestEntity<Void> request = RequestEntity
                .get(createServerAddress(path))
                .build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        System.out.println(response.getStatusCode());
        // then:
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("Route worked", response.getBody());
        Mockito.verify(otherApiHooks).handleGet();
    }


    @Test
    void shouldNotRouteToPostAsUnauthenticated() throws Exception {
        Mockito.doReturn(new ResponseEntity<String>("Route worked", HttpStatus.ACCEPTED))
                .when(otherApiHooks).handlePost();
        // when:
        RequestEntity<Void> request = RequestEntity
                .post(createServerAddress("advert-service/adverts/1"))
                .build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        // then:
        assertTrue(response.getStatusCode().is4xxClientError());
        Mockito.verify(otherApiHooks, never()).handlePost();
    }

    @Test
    void shouldRouteToPostAsUser() throws Exception {
        // given:
        String jwtToken = getJwt("user12", "password");

        Mockito.doReturn(new ResponseEntity<String>("Route worked", HttpStatus.ACCEPTED))
                .when(otherApiHooks).handlePost();
        // when:
        RequestEntity<Void> request = RequestEntity
                .post(createServerAddress("advert-service/adverts"))
                .header("Authorization", "Bearer " + jwtToken)
                .build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        // then:
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("Route worked", response.getBody());
        Mockito.verify(otherApiHooks).handlePost();
    }

    @Test
    void shouldRouteToPostAsAdmin() throws Exception {
        // given:
        String jwtToken = getJwt("admin1", "password");

        Mockito.doReturn(new ResponseEntity<String>("Route worked", HttpStatus.ACCEPTED))
                .when(otherApiHooks).handlePost();

        // when:
        RequestEntity<Void> request = RequestEntity
                .post(createServerAddress("advert-service/adverts"))
                .header("Authorization", "Bearer " + jwtToken)
                .build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        // then:
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("Route worked", response.getBody());
        Mockito.verify(otherApiHooks).handlePost();
    }

    @Test
    void shouldNotRouteToPutAsUnauthenticated() throws Exception {
        Mockito.doReturn(new ResponseEntity<String>("Route worked", HttpStatus.ACCEPTED))
                .when(otherApiHooks).handlePut();
        // when:
        RequestEntity<Void> request = RequestEntity
                .put(createServerAddress("advert-service/adverts/1"))
                .build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        // then:
        assertTrue(response.getStatusCode().is4xxClientError());
        Mockito.verify(otherApiHooks, never()).handlePut();
    }

    @Test
    void shouldRouteToPutAsUser() throws Exception {
        // given:
        String jwtToken = getJwt("user12", "password");

        Mockito.doReturn(new ResponseEntity<String>("Route worked", HttpStatus.ACCEPTED))
                .when(otherApiHooks).handlePut();
        // when:
        RequestEntity<Void> request = RequestEntity
                .put(createServerAddress("advert-service/adverts/1"))
                .header("Authorization", "Bearer " + jwtToken)
                .build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        // then:
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("Route worked", response.getBody());
        Mockito.verify(otherApiHooks).handlePut();
    }

    @Test
    void shouldRouteToPutAsAdmin() throws Exception {
        // given:
        String jwtToken = getJwt("admin1", "password");

        Mockito.doReturn(new ResponseEntity<String>("Route worked", HttpStatus.ACCEPTED))
                .when(otherApiHooks).handlePut();

        // when:
        RequestEntity<Void> request = RequestEntity
                .put(createServerAddress("advert-service/adverts/1"))
                .header("Authorization", "Bearer " + jwtToken)
                .build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        // then:
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("Route worked", response.getBody());
        Mockito.verify(otherApiHooks).handlePut();
    }

    @Test
    void shouldNotRouteToDeleteAsUnauthenticated() throws Exception {
        Mockito.doReturn(new ResponseEntity<String>("Route worked", HttpStatus.ACCEPTED))
                .when(otherApiHooks).handleDelete();
        // when:
        RequestEntity<Void> request = RequestEntity
                .delete(createServerAddress("advert-service/adverts/1"))
                .build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        // then:
        assertTrue(response.getStatusCode().is4xxClientError());
        Mockito.verify(otherApiHooks, never()).handleDelete();
    }

    @Test
    void shouldRouteToDeleteAsUser() throws Exception {
        // given:
        String jwtToken = getJwt("user12", "password");

        Mockito.doReturn(new ResponseEntity<String>("Route worked", HttpStatus.ACCEPTED))
                .when(otherApiHooks).handleDelete();
        // when:
        RequestEntity<Void> request = RequestEntity
                .delete(createServerAddress("advert-service/adverts/1"))
                .header("Authorization", "Bearer " + jwtToken)
                .build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        // then:
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("Route worked", response.getBody());
        Mockito.verify(otherApiHooks).handleDelete();
    }

    @Test
    void shouldRouteToDeleteAsAdmin() throws Exception {
        // given:
        String jwtToken = getJwt("admin1", "password");

        Mockito.doReturn(new ResponseEntity<String>("Route worked", HttpStatus.ACCEPTED))
                .when(otherApiHooks).handleDelete();

        // when:
        RequestEntity<Void> request = RequestEntity
                .delete(createServerAddress("advert-service/adverts/1"))
                .header("Authorization", "Bearer " + jwtToken)
                .build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        // then:
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("Route worked", response.getBody());
        Mockito.verify(otherApiHooks).handleDelete();
    }

    @Test
    void shouldNotRouteToDeleteByUserAsUnauthenticated() throws Exception {
        // given:
        Mockito.doReturn(new ResponseEntity<String>("Route worked", HttpStatus.ACCEPTED))
                .when(otherApiHooks).handleDelete();

        // when:
        RequestEntity<Void> request = RequestEntity
                .delete(createServerAddress("advert-service/adverts/users/1"))
                .build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        // then:
        assertTrue(response.getStatusCode().is4xxClientError());
        Mockito.verify(otherApiHooks, never()).handleDelete();
        System.out.println(response);
    }

    @Test
    void shouldNotRouteToDeleteByUserAsUser() throws Exception {
        // given:
        String jwtToken = getJwt("user12", "password");

        Mockito.doReturn(new ResponseEntity<String>("Route worked", HttpStatus.ACCEPTED))
                .when(otherApiHooks).handleDelete();
        // when:
        RequestEntity<Void> request = RequestEntity
                .delete(createServerAddress("advert-service/adverts/users/1"))
                .header("Authorization", "Bearer " + jwtToken)
                .build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        assertTrue(response.getStatusCode().is4xxClientError());
        Mockito.verify(otherApiHooks, never()).handleDelete();
    }

    @Test
    void shouldRouteToDeleteByUserAsAdmin() throws Exception {
        // given:
        String jwtToken = getJwt("admin1", "password");

        Mockito.doReturn(new ResponseEntity<String>("Route worked", HttpStatus.ACCEPTED))
                .when(otherApiHooks).handleDelete();

        // when:
        RequestEntity<Void> request = RequestEntity
                .delete(createServerAddress("advert-service/adverts/users/1"))
                .header("Authorization", "Bearer " + jwtToken)
                .build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        // then:
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("Route worked", response.getBody());
        Mockito.verify(otherApiHooks).handleDelete();
    }
}
