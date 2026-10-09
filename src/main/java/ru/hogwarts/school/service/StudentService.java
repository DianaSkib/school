package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student");
        logger.debug("Student details: name={}, age={}", student.getName(), student.getAge());
        return studentRepository.save(student);
    }

    public Student getStudent(Long id) {

        logger.info("Was invoked method for get student by id");
        logger.debug("Searching student with id = {}", id);
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            logger.error("There is no student with id = " + id);
        }
        return student;
    }

    public Collection<Student> getAllStudents() {
        logger.info("Was invoked method for get all students");
        return studentRepository.findAll();
    }

    public Collection<Student> getStudentsByAge(int age) {
        logger.info("Was invoked method for get students by age");
        logger.debug("Filtering students by age = {}", age);
        return studentRepository.findAll().stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }

    public Student updateStudent(Student student) {
        logger.info("Was invoked method for update student");
        if (studentRepository.existsById(student.getId())) {
            return studentRepository.save(student);
        }
        logger.error("There is no student with id = " + student.getId());
        return null;
    }

    public void deleteStudent(Long id) {
        logger.info("Was invoked method for delete student");
        logger.debug("Deleting student with id = {}", id);
        studentRepository.deleteById(id);
    }

    public Collection<Student> getStudentsByAgeBetween(int min, int max) {
        logger.info("Was invoked method for get students by age between");
        logger.debug("Age range: min={}, max={}", min, max);
        if (min > max) {
            logger.warn("Min age is greater than max age: min={}, max={}", min, max);
        }
        return studentRepository.findByAgeBetween(min, max);
    }

    public Faculty getFacultyOfStudent(Long studentId) {
        logger.info("Was invoked method for get faculty of student");
        logger.debug("Looking for faculty of student with id = {}", studentId);
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student != null) {
            return student.getFaculty();
        }
        logger.error("There is no student with id = " + studentId);
        return null;
    }

    public Integer getTotalCountOfStudents() {
        logger.info("Was invoked method for get total count of students");
        return studentRepository.getTotalCountOfStudents();
    }

    public Double getAverageAgeOfStudents() {
        logger.info("Was invoked method for get average age of students");
        Double avg = studentRepository.getAverageAgeOfStudents();
        return avg != null ? avg : 0.0;
    }

    public List<Student> getLastFiveStudents() {
        logger.info("Was invoked method for get last five students");
        return studentRepository.getLastFiveStudents();
    }
}
