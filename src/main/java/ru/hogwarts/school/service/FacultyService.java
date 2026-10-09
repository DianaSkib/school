package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository,
                          StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.info("Was invoked method for create faculty");
        logger.debug("Faculty details: name={}, color={}", faculty.getName(), faculty.getColor());
        return facultyRepository.save(faculty);
    }

    public Faculty getFaculty(Long id) {
        logger.info("Was invoked method for get faculty by id");
        logger.debug("Searching faculty with id = {}", id);
        Faculty faculty = facultyRepository.findById(id).orElse(null);
        if (faculty == null) {
            logger.error("There is no faculty with id = " + id);
        }
        return faculty;
    }

    public Collection<Faculty> getAllFaculties() {
        logger.info("Was invoked method for get all faculties");
        return facultyRepository.findAll();
    }

    public Collection<Faculty> getFacultiesByColor(String color) {
        logger.info("Was invoked method for get faculties by color");
        logger.debug("Filtering faculties by color = {}", color);
        return facultyRepository.findAll().stream()
                .filter(faculty -> faculty.getColor().equals(color))
                .collect(Collectors.toList());
    }

    public Collection<Faculty> findByColorOrName(String colorOrName) {
        logger.info("Was invoked method for find faculties by color or name");
        logger.debug("Search value = {}", colorOrName);
        return facultyRepository
                .findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(
                        colorOrName, colorOrName);
    }

    public Faculty updateFaculty(Faculty faculty) {
        logger.info("Was invoked method for update faculty");
        if (faculty.getId() == null) {
            logger.warn("Faculty id is null, cannot update");
            return null;
        }
        if (facultyRepository.existsById(faculty.getId())) {
            return facultyRepository.save(faculty);
        }
        logger.error("There is no faculty with id = " + faculty.getId());
        return null;
    }

    public void deleteFaculty(Long id) {
        logger.info("Was invoked method for delete faculty");
        logger.debug("Deleting faculty with id = {}", id);
        facultyRepository.deleteById(id);
    }

    public Collection<Student> getStudentsOfFaculty(Long facultyId) {
        logger.info("Was invoked method for get students of faculty");
        logger.debug("Looking for students of faculty with id = {}", facultyId);
        return studentRepository.findByFacultyId(facultyId);
    }
}