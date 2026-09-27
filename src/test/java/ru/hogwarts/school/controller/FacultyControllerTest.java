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
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FacultyService facultyService;

    @Test
    void createFacultyTest() throws Exception {
        Faculty faculty = new Faculty(1L, "Test Faculty", "red");
        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Faculty"))
                .andExpect(jsonPath("$.color").value("red"));
    }

    @Test
    void getFacultyTest() throws Exception {
        Faculty faculty = new Faculty(1L, "Gryffindor", "red");
        when(facultyService.getFaculty(1L)).thenReturn(faculty);

        mockMvc.perform(get("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Gryffindor"))
                .andExpect(jsonPath("$.color").value("red"));
    }

    @Test
    void getFacultyNotFoundTest() throws Exception {
        when(facultyService.getFaculty(999999L)).thenReturn(null);

        mockMvc.perform(get("/faculty/999999"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void getAllFacultiesTest() throws Exception {
        List<Faculty> faculties = List.of(
                new Faculty(1L, "Gryffindor", "red"),
                new Faculty(2L, "Slytherin", "green")
        );
        when(facultyService.getAllFaculties()).thenReturn(faculties);

        mockMvc.perform(get("/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Gryffindor"))
                .andExpect(jsonPath("$[1].name").value("Slytherin"));
    }

    @Test
    void updateFacultyTest() throws Exception {
        Faculty faculty = new Faculty(1L, "Updated", "blue");
        when(facultyService.updateFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(put("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"))
                .andExpect(jsonPath("$.color").value("blue"));
    }

    @Test
    void deleteFacultyTest() throws Exception {
        mockMvc.perform(delete("/faculty/1"))
                .andExpect(status().isOk());
    }

    @Test
    void searchFacultyTest() throws Exception {
        List<Faculty> faculties = List.of(new Faculty(1L, "Gryffindor", "silver"));
        when(facultyService.findByColorOrName(anyString())).thenReturn(faculties);

        mockMvc.perform(get("/faculty/search")
                        .param("value", "silver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].color").value("silver"));
    }

    @Test
    void getStudentsOfFacultyTest() throws Exception {
        List<Student> students = List.of(
                new Student(1L, "Harry", 17),
                new Student(2L, "Ron", 17)
        );
        when(facultyService.getStudentsOfFaculty(anyLong())).thenReturn(students);

        mockMvc.perform(get("/faculty/1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Harry"))
                .andExpect(jsonPath("$[1].name").value("Ron"));
    }
}