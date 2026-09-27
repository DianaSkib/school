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
import ru.hogwarts.school.model.Student;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl() {
        return "http://localhost:" + port + "/student";
    }

    @Test
    void createStudentTest() {
        Student student = new Student(null, "Test Student", 20);
        ResponseEntity<Student> response = restTemplate.postForEntity(
                baseUrl(), student, Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Test Student");
        assertThat(response.getBody().getId()).isNotNull();
    }

    @Test
    void getStudentTest() {
        Student student = restTemplate.postForEntity(
                baseUrl(), new Student(null, "Test Get", 22), Student.class).getBody();
        assertThat(student).isNotNull();

        ResponseEntity<Student> response = restTemplate.getForEntity(
                baseUrl() + "/" + student.getId(), Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(student.getId());
        assertThat(response.getBody().getName()).isEqualTo("Test Get");
    }

    @Test
    void getStudentNotFoundTest() {
        ResponseEntity<Student> response = restTemplate.getForEntity(
                baseUrl() + "/999999", Student.class);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void getAllStudentsTest() {
        restTemplate.postForEntity(baseUrl(),
                new Student(null, "Test All1", 20), Student.class);
        restTemplate.postForEntity(baseUrl(),
                new Student(null, "Test All2", 21), Student.class);

        ResponseEntity<Student[]> response = restTemplate.getForEntity(
                baseUrl(), Student[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isGreaterThanOrEqualTo(2);
    }

    @Test
    void updateStudentTest() {
        Student student = restTemplate.postForEntity(
                baseUrl(), new Student(null, "Test Update", 20), Student.class).getBody();
        assertThat(student).isNotNull();

        student.setName("Updated Name");
        student.setAge(25);
        ResponseEntity<Student> response = restTemplate.exchange(
                baseUrl(), HttpMethod.PUT, new HttpEntity<>(student), Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Updated Name");
        assertThat(response.getBody().getAge()).isEqualTo(25);
    }

    @Test
    void deleteStudentTest() {
        Student student = restTemplate.postForEntity(
                baseUrl(), new Student(null, "Test Delete", 20), Student.class).getBody();
        assertThat(student).isNotNull();

        restTemplate.delete(baseUrl() + "/" + student.getId());

        ResponseEntity<Student> response = restTemplate.getForEntity(
                baseUrl() + "/" + student.getId(), Student.class);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void getStudentsByAgeBetweenTest() {
        Student student = new Student(null, "Between Test", 15);
        restTemplate.postForEntity(baseUrl(), student, Student.class);

        ResponseEntity<Student[]> response = restTemplate.getForEntity(
                baseUrl() + "/age-between?min=10&max=20", Student[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isGreaterThanOrEqualTo(1);
    }
}