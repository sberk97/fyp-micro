package com.berk.advertservice.controller;

import com.berk.advertservice.OtherApiHooks;
import com.berk.advertservice.model.AddAdvert;
import com.berk.advertservice.model.Advert;
import com.berk.advertservice.model.ReturnUserDetails;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(value = "/insert_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/clean_database.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AdvertControllerITest {

    @LocalServerPort
    private int serverPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private OtherApiHooks otherApiHooks;

    private URI createServerAddress(String endpoint) throws URISyntaxException {
        return new URI("http://localhost:" + serverPort + "/" + endpoint);
    }

    @Test
    void shouldReturnAdvertById() throws Exception {
        // given:
        int id = 101;

        // when:
        RequestEntity<Void> request = RequestEntity
                .get(createServerAddress("adverts/" + id))
                .build();

        ResponseEntity<Advert> response = restTemplate.exchange(request, Advert.class);
        Advert advert = response.getBody();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // then:
        assertEquals(id, advert.getId());
        assertEquals(101, advert.getUserId());
        assertEquals("admin1", advert.getUsername());
        assertEquals(1000, advert.getPrice());
        assertEquals(LocalDateTime.parse("2021-01-01 00:00:00", formatter), advert.getCreationDate());
        assertEquals("This is a title.", advert.getTitle());
        assertEquals("This is a description.", advert.getDescription());
        assertEquals("This is contact details.", advert.getContactDetails());
    }

    @Test
    void shouldReturn4xxWhenAdvertNotExists() throws Exception {
        // given:
        int id = 999;

        // when:
        RequestEntity<Void> request = RequestEntity
                .get(createServerAddress("adverts/" + id))
                .build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        // then:
        assertTrue(response.getStatusCode().is4xxClientError());
    }

    @Test
    void shouldReturn4xxWhenWrongAdvertId() throws Exception {
        // given:
        String id = "abc";

        // when:
        RequestEntity<Void> request = RequestEntity
                .get(createServerAddress("adverts/" + id))
                .build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        // then:
        assertTrue(response.getStatusCode().is4xxClientError());
    }

    @Test
    void shouldReturnAdvertByUserId() throws Exception {
        // given:
        int id = 101;

        // when:
        RequestEntity<Void> request = RequestEntity
                .get(createServerAddress("adverts/users/" + id))
                .build();

        ResponseEntity<List<Advert>> response = restTemplate.exchange(request, new ParameterizedTypeReference<List<Advert>>() {
        });
        Advert advert = response.getBody().get(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // then:
        assertEquals(id, advert.getId());
        assertEquals(101, advert.getUserId());
        assertEquals("admin1", advert.getUsername());
        assertEquals(1000, advert.getPrice());
        assertEquals(LocalDateTime.parse("2021-01-01 00:00:00", formatter), advert.getCreationDate());
        assertEquals("This is a title.", advert.getTitle());
        assertEquals("This is a description.", advert.getDescription());
        assertEquals("This is contact details.", advert.getContactDetails());
    }

    @Test
    void shouldReturn4xxWhenUserNotExists() throws Exception {
        // given:
        int id = 999;

        // when:
        RequestEntity<Void> request = RequestEntity
                .get(createServerAddress("adverts/users/" + id))
                .build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        // then:
        assertTrue(response.getStatusCode().is4xxClientError());
    }

    @Test
    void shouldReturn4xxWhenWrongUserId() throws Exception {
        // given:
        String id = "abc";

        // when:
        RequestEntity<Void> request = RequestEntity
                .get(createServerAddress("adverts/users/" + id))
                .build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        // then:
        assertTrue(response.getStatusCode().is4xxClientError());
    }

    @Test
    void shouldReturnLast2Adverts() throws Exception {
        // given:
        int howManyAdverts = 2;

        // when:
        RequestEntity<Void> request = RequestEntity
                .get(createServerAddress("adverts-latest/?last=" + howManyAdverts))
                .build();

        ResponseEntity<List<Advert>> response = restTemplate.exchange(request, new ParameterizedTypeReference<List<Advert>>() {
        });

        // then:
        assertEquals(102, response.getBody().get(0).getId());
        assertEquals(101, response.getBody().get(1).getId());
        assertEquals(howManyAdverts, response.getBody().size());
    }

    @Test
    void shouldFindByTitle() throws Exception {
        // given:
        String title = "tit";

        // when:
        RequestEntity<Void> request = RequestEntity
                .get(createServerAddress("adverts/?title=" + title))
                .build();

        ResponseEntity<List<Advert>> response = restTemplate.exchange(request, new ParameterizedTypeReference<List<Advert>>() {
        });

        // then:
        assertEquals(101, response.getBody().get(0).getId());
        assertEquals(102, response.getBody().get(1).getId());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void shouldNotFindAnything() throws Exception {
        // given:
        String title = "zzz";

        // when:
        RequestEntity<Void> request = RequestEntity
                .get(createServerAddress("adverts/?title=" + title))
                .build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        // then:
        assertTrue(response.getStatusCode().is4xxClientError());
    }

    @Test
    void shouldValidateTooLongTitle() throws Exception {
        // given:
        String title = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

        // when:
        RequestEntity<Void> request = RequestEntity
                .get(createServerAddress("adverts/?title=" + title))
                .build();

        ResponseEntity<HashMap> response = restTemplate.exchange(request, HashMap.class);

        // then:
        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("Title can be up to 60 characters", response.getBody().get("title"));
    }

    @Test
    void shouldAddAdvert() throws Exception {
        // given:
        AddAdvert addAdvert = new AddAdvert(1500, "New advert title", "New advert description", "New advert contact details");
        ReturnUserDetails returnUserDetails = new ReturnUserDetails(101, "admin1", new String[]{"ROLE_ADMIN"});

        Mockito.doReturn(ResponseEntity.ok(returnUserDetails))
                .when(otherApiHooks).handleGet();

        // when:
        RequestEntity<AddAdvert> request = RequestEntity
                .post(createServerAddress("adverts"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(addAdvert);

        ResponseEntity<Integer> response = restTemplate.exchange(request, Integer.class);

        // then:
        assertTrue(response.getStatusCode().is2xxSuccessful());
        Mockito.verify(otherApiHooks).handleGet();

        // given:
        int id = response.getBody();

        // when:
        RequestEntity<Void> request2 = RequestEntity
                .get(createServerAddress("adverts/" + id))
                .build();

        ResponseEntity<Advert> response2 = restTemplate.exchange(request2, Advert.class);
        Advert advert = response2.getBody();

        // then:
        assertEquals(id, advert.getId());
        assertEquals(101, advert.getUserId());
        assertEquals("admin1", advert.getUsername());
        assertEquals(addAdvert.getPrice(), advert.getPrice());
        assertEquals(addAdvert.getTitle(), advert.getTitle());
        assertEquals(addAdvert.getDescription(), advert.getDescription());
        assertEquals(addAdvert.getContactDetails(), advert.getContactDetails());
    }

    @Test
    void shouldUpdateOwnAdvert() throws Exception {
        // given:
        int id = 102;
        AddAdvert addAdvert = new AddAdvert(1500, "New advert title", "New advert description", "New advert contact details");
        ReturnUserDetails returnUserDetails = new ReturnUserDetails(102, "user12", new String[]{"ROLE_USER"});

        Mockito.doReturn(ResponseEntity.ok(returnUserDetails))
                .when(otherApiHooks).handleGet();

        // when:
        RequestEntity<AddAdvert> request = RequestEntity
                .put(createServerAddress("adverts/" + id))
                .contentType(MediaType.APPLICATION_JSON)
                .body(addAdvert);

        ResponseEntity<Integer> response = restTemplate.exchange(request, Integer.class);

        // then:
        assertTrue(response.getStatusCode().is2xxSuccessful());
        Mockito.verify(otherApiHooks).handleGet();

        // given:
        assertEquals(id, response.getBody());

        // when:
        RequestEntity<Void> request2 = RequestEntity
                .get(createServerAddress("adverts/" + id))
                .build();

        ResponseEntity<Advert> response2 = restTemplate.exchange(request2, Advert.class);
        Advert advert = response2.getBody();

        // then:
        assertEquals(id, advert.getId());
        assertEquals(102, advert.getUserId());
        assertEquals("user12", advert.getUsername());
        assertEquals(addAdvert.getPrice(), advert.getPrice());
        assertEquals(addAdvert.getTitle(), advert.getTitle());
        assertEquals(addAdvert.getDescription(), advert.getDescription());
        assertEquals(addAdvert.getContactDetails(), advert.getContactDetails());
    }

    @Test
    void shouldReturn4xxIfUpdatingSomeonesAdvert() throws Exception {
        // given:
        int id = 101;
        AddAdvert addAdvert = new AddAdvert(1500, "New advert title", "New advert description", "New advert contact details");
        ReturnUserDetails returnUserDetails = new ReturnUserDetails(102, "user12", new String[]{"ROLE_USER"});

        Mockito.doReturn(ResponseEntity.ok(returnUserDetails))
                .when(otherApiHooks).handleGet();

        // when:
        RequestEntity<AddAdvert> request = RequestEntity
                .put(createServerAddress("adverts/" + id))
                .contentType(MediaType.APPLICATION_JSON)
                .body(addAdvert);

        ResponseEntity<Integer> response = restTemplate.exchange(request, Integer.class);

        // then:
        assertTrue(response.getStatusCode().is4xxClientError());
        Mockito.verify(otherApiHooks).handleGet();
    }

    @Test
    void shouldUpdateAnyAdvertAsAdmin() throws Exception {
        // given:
        int id = 102;
        AddAdvert addAdvert = new AddAdvert(1500, "New advert title", "New advert description", "New advert contact details");
        ReturnUserDetails returnUserDetails = new ReturnUserDetails(101, "admin1", new String[]{"ROLE_ADMIN"});

        Mockito.doReturn(ResponseEntity.ok(returnUserDetails))
                .when(otherApiHooks).handleGet();

        // when:
        RequestEntity<AddAdvert> request = RequestEntity
                .put(createServerAddress("adverts/" + id))
                .contentType(MediaType.APPLICATION_JSON)
                .body(addAdvert);

        ResponseEntity<Integer> response = restTemplate.exchange(request, Integer.class);

        // then:
        assertTrue(response.getStatusCode().is2xxSuccessful());
        Mockito.verify(otherApiHooks).handleGet();

        // given:
        assertEquals(id, response.getBody());

        // when:
        RequestEntity<Void> request2 = RequestEntity
                .get(createServerAddress("adverts/" + id))
                .build();

        ResponseEntity<Advert> response2 = restTemplate.exchange(request2, Advert.class);
        Advert advert = response2.getBody();

        // then:
        assertEquals(id, advert.getId());
        assertEquals(102, advert.getUserId());
        assertEquals("user12", advert.getUsername());
        assertEquals(addAdvert.getPrice(), advert.getPrice());
        assertEquals(addAdvert.getTitle(), advert.getTitle());
        assertEquals(addAdvert.getDescription(), advert.getDescription());
        assertEquals(addAdvert.getContactDetails(), advert.getContactDetails());
    }

    @Test
    void shouldValidateAdvert() throws Exception {
        // given:
        AddAdvert addAdvert = new AddAdvert(-1, "a", "a", "a");

        // when:
        RequestEntity<AddAdvert> request = RequestEntity
                .post(createServerAddress("adverts"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(addAdvert);

        ResponseEntity<HashMap> response = restTemplate.exchange(request, HashMap.class);

        // then:
        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("Price can not be negative", response.getBody().get("price"));
        assertEquals("Title must be between 6 and 60 characters", response.getBody().get("title"));
        assertEquals("Description must be between 10 and 1024 characters", response.getBody().get("description"));
        assertEquals("Contact details must be between 10 and 60 characters", response.getBody().get("contactDetails"));
    }

    @Test
    void shouldDeleteHisOwnAdvert() throws Exception {
        // given:
        int id = 102;
        ReturnUserDetails returnUserDetails = new ReturnUserDetails(102, "user12", new String[]{"ROLE_USER"});

        Mockito.doReturn(ResponseEntity.ok(returnUserDetails))
                .when(otherApiHooks).handleGet();

        // when:
        RequestEntity<Void> request = RequestEntity
                .delete(createServerAddress("adverts/" + id))
                .build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        // then:
        assertTrue(response.getStatusCode().is2xxSuccessful());
        Mockito.verify(otherApiHooks).handleGet();

        // when:
        RequestEntity<Void> request2 = RequestEntity
                .get(createServerAddress("adverts/" + id))
                .build();

        ResponseEntity<String> response2 = restTemplate.exchange(request2, String.class);

        // then:
        assertTrue(response2.getStatusCode().is4xxClientError());
    }

    @Test
    void shouldReturn4xxWhenDeletingSomeonesAdvert() throws Exception {
        // given:
        int id = 101;
        ReturnUserDetails returnUserDetails = new ReturnUserDetails(102, "user12", new String[]{"ROLE_USER"});

        Mockito.doReturn(ResponseEntity.ok(returnUserDetails))
                .when(otherApiHooks).handleGet();

        // when:
        RequestEntity<Void> request = RequestEntity
                .delete(createServerAddress("adverts/" + id))
                .build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        // then:
        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("Advert not found.", response.getBody());
        Mockito.verify(otherApiHooks).handleGet();

        // when:
        RequestEntity<Void> request2 = RequestEntity
                .get(createServerAddress("adverts/" + id))
                .build();

        ResponseEntity<Advert> response2 = restTemplate.exchange(request2, Advert.class);
        Advert advert = response2.getBody();

        // then:
        assertTrue(response2.getStatusCode().is2xxSuccessful());
        assertEquals(id, advert.getId());
    }

    @Test
    void shouldDeleteAnyAdvertAsAdmin() throws Exception {
        // given:
        int id = 102;
        ReturnUserDetails returnUserDetails = new ReturnUserDetails(101, "admin1", new String[]{"ROLE_ADMIN"});

        Mockito.doReturn(ResponseEntity.ok(returnUserDetails))
                .when(otherApiHooks).handleGet();

        // when:
        RequestEntity<Void> request = RequestEntity
                .delete(createServerAddress("adverts/" + id))
                .build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        // then:
        assertTrue(response.getStatusCode().is2xxSuccessful());
        Mockito.verify(otherApiHooks).handleGet();

        // when:
        RequestEntity<Void> request2 = RequestEntity
                .get(createServerAddress("adverts/" + id))
                .build();

        ResponseEntity<String> response2 = restTemplate.exchange(request2, String.class);

        // then:
        assertTrue(response2.getStatusCode().is4xxClientError());
    }

    @Test
    void shouldDeleteAdvertsOfUser() throws Exception {
        // given:
        int id = 101;

        // when:
        RequestEntity<Void> request = RequestEntity
                .delete(createServerAddress("adverts/users/" + id))
                .build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        // then:
        assertTrue(response.getStatusCode().is2xxSuccessful());

        // when:
        RequestEntity<Void> request2 = RequestEntity
                .get(createServerAddress("adverts/" + id))
                .build();

        ResponseEntity<String> response2 = restTemplate.exchange(request2, String.class);

        // then:
        assertTrue(response2.getStatusCode().is4xxClientError());
    }
}
