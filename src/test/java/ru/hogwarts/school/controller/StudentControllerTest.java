package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    @Test
    void createStudentTest() throws Exception {
        Student student = new Student(1L, "Test Student", 20);
        when(studentService.createStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Student"))
                .andExpect(jsonPath("$.age").value(20));
    }

    @Test
    void getStudentTest() throws Exception {
        Student student = new Student(1L, "Harry", 17);
        when(studentService.getStudent(1L)).thenReturn(student);

        mockMvc.perform(get("/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Harry"))
                .andExpect(jsonPath("$.age").value(17));
    }

    @Test
    void getStudentNotFoundTest() throws Exception {
        when(studentService.getStudent(999999L)).thenReturn(null);

        mockMvc.perform(get("/student/999999"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void getAllStudentsTest() throws Exception {
        List<Student> students = List.of(
                new Student(1L, "Harry", 17),
                new Student(2L, "Ron", 17)
        );
        when(studentService.getAllStudents()).thenReturn(students);

        mockMvc.perform(get("/student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Harry"))
                .andExpect(jsonPath("$[1].name").value("Ron"));
    }

    @Test
    void updateStudentTest() throws Exception {
        Student student = new Student(1L, "Updated", 25);
        when(studentService.updateStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"))
                .andExpect(jsonPath("$.age").value(25));
    }

    @Test
    void deleteStudentTest() throws Exception {
        mockMvc.perform(delete("/student/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getStudentsByAgeBetweenTest() throws Exception {
        List<Student> students = List.of(new Student(1L, "Between", 15));
        when(studentService.getStudentsByAgeBetween(anyInt(), anyInt())).thenReturn(students);

        mockMvc.perform(get("/student/age-between")
                        .param("min", "10")
                        .param("max", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Between"));
    }

    @Test
    void getFacultyOfStudentTest() throws Exception {
        Faculty faculty = new Faculty(1L, "Gryffindor", "red");
        when(studentService.getFacultyOfStudent(anyLong())).thenReturn(faculty);

        mockMvc.perform(get("/student/1/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gryffindor"))
                .andExpect(jsonPath("$.color").value("red"));
    }
}