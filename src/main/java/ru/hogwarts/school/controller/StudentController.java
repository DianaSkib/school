package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentService.getStudent(id);
    }

    @GetMapping
    public Collection<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/age-between")
    public Collection<Student> getStudentsByAgeBetween(@RequestParam int min,
                                                       @RequestParam int max) {
        return studentService.getStudentsByAgeBetween(min, max);
    }

    @GetMapping("/{id}/faculty")
    public Faculty getFacultyOfStudent(@PathVariable Long id) {
        return studentService.getFacultyOfStudent(id);
    }

    @PutMapping
    public Student updateStudent(@RequestBody Student student) {
        return studentService.updateStudent(student);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }
    @GetMapping("/count")
    public Integer getTotalCountOfStudents() {
        return studentService.getTotalCountOfStudents();
    }

    @GetMapping("/average-age")
    public Double getAverageAgeOfStudents() {
        return studentService.getAverageAgeOfStudents();
    }

    @GetMapping("/last-five")
    public List<Student> getLastFiveStudents() {
        return studentService.getLastFiveStudents();
    }

    @GetMapping("/names-starting-with-a")
    public Collection<String> getStudentNamesStartingWithA() {
        return studentService.getStudentNamesStartingWithA();
    }

    @GetMapping("/average-age-stream")
    public double getAverageAgeOfStudentsByStream() {
        return studentService.getAverageAgeOfStudentsByStream();
    }

    @GetMapping("/students/print-parallel")
    public void printStudentsParallel() {
        List<Student> students = (List<Student>) studentService.getAllStudents();

        System.out.println(students.get(0).getName());
        System.out.println(students.get(1).getName());

        new Thread(() -> {
            System.out.println(students.get(2).getName());
            System.out.println(students.get(3).getName());
        }).start();

        new Thread(() -> {
            System.out.println(students.get(4).getName());
            System.out.println(students.get(5).getName());
        }).start();
    }

    @GetMapping("/students/print-synchronized")
    public void printStudentsSynchronized() {
        List<Student> students = studentService.getAllStudentsList();

        printName(students.get(0).getName());
        printName(students.get(1).getName());

        new Thread(() -> {
            printName(students.get(2).getName());
            printName(students.get(3).getName());
        }).start();

        new Thread(() -> {
            printName(students.get(4).getName());
            printName(students.get(5).getName());
        }).start();
    }

    private synchronized void printName(String name) {
        System.out.println(name);
    }
}