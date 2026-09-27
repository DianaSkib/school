package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl() {
        return "http://localhost:" + port + "/faculty";
    }

    @Test
    void createFacultyTest() {
        Faculty faculty = new Faculty(null, "Test Faculty", "red");
        ResponseEntity<Faculty> response = restTemplate.postForEntity(
                baseUrl(), faculty, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Test Faculty");
        assertThat(response.getBody().getColor()).isEqualTo("red");
        assertThat(response.getBody().getId()).isNotNull();
    }

    @Test
    void getFacultyTest() {
        Faculty faculty = restTemplate.postForEntity(
                baseUrl(), new Faculty(null, "Test Get", "blue"), Faculty.class).getBody();
        assertThat(faculty).isNotNull();

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                baseUrl() + "/" + faculty.getId(), Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(faculty.getId());
    }

    @Test
    void getFacultyNotFoundTest() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                baseUrl() + "/999999", Faculty.class);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void getAllFacultiesTest() {
        restTemplate.postForEntity(baseUrl(),
                new Faculty(null, "Test All1", "green"), Faculty.class);
        restTemplate.postForEntity(baseUrl(),
                new Faculty(null, "Test All2", "yellow"), Faculty.class);

        ResponseEntity<Faculty[]> response = restTemplate.getForEntity(
                baseUrl(), Faculty[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isGreaterThanOrEqualTo(2);
    }

    @Test
    void updateFacultyTest() {
        Faculty faculty = restTemplate.postForEntity(
                baseUrl(), new Faculty(null, "Test Update", "purple"), Faculty.class).getBody();
        assertThat(faculty).isNotNull();

        faculty.setName("Updated Name");
        faculty.setColor("black");
        ResponseEntity<Faculty> response = restTemplate.exchange(
                baseUrl(), HttpMethod.PUT, new HttpEntity<>(faculty), Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Updated Name");
        assertThat(response.getBody().getColor()).isEqualTo("black");
    }

    @Test
    void deleteFacultyTest() {
        Faculty faculty = restTemplate.postForEntity(
                baseUrl(), new Faculty(null, "Test Delete", "white"), Faculty.class).getBody();
        assertThat(faculty).isNotNull();

        restTemplate.delete(baseUrl() + "/" + faculty.getId());

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                baseUrl() + "/" + faculty.getId(), Faculty.class);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void searchFacultyTest() {
        restTemplate.postForEntity(baseUrl(),
                new Faculty(null, "Test Search", "silver"), Faculty.class);

        ResponseEntity<Faculty[]> response = restTemplate.getForEntity(
                baseUrl() + "/search?value=silver", Faculty[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isGreaterThanOrEqualTo(1);
    }
}