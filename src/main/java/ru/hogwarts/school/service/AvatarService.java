package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AvatarService {

    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);

    private final AvatarRepository avatarRepository;
    private final StudentService studentService;

    @Value("${avatar.dir.path}")
    private String avatarDirPath;

    public AvatarService(AvatarRepository avatarRepository, StudentService studentService) {
        this.avatarRepository = avatarRepository;
        this.studentService = studentService;
    }

    public Avatar findAvatar(Long studentId) {
        logger.info("Was invoked method for find avatar");
        logger.debug("Searching avatar for student with id = {}", studentId);
        return avatarRepository.findByStudentId(studentId).orElse(null);
    }

    public void uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        logger.info("Was invoked method for upload avatar");
        logger.debug("Uploading avatar: studentId={}, fileName={}, size={}",
                studentId, file.getOriginalFilename(), file.getSize());

        Student student = studentService.getStudent(studentId);
        if (student == null) {
            logger.error("There is no student with id = " + studentId);
            return;
        }

        Path filePath = Path.of(avatarDirPath, studentId + "_" + file.getOriginalFilename());
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());

        Avatar avatar = avatarRepository.findByStudentId(studentId).orElse(new Avatar());
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());
        avatar.setStudent(student);
        avatarRepository.save(avatar);
    }

    public Avatar findAvatarFromDb(Long studentId) {
        logger.info("Was invoked method for find avatar from db");
        return avatarRepository.findByStudentId(studentId).orElse(null);
    }

    public byte[] findAvatarFromDisk(Long studentId) throws IOException {
        logger.info("Was invoked method for find avatar from disk");
        Avatar avatar = avatarRepository.findByStudentId(studentId).orElse(null);
        if (avatar == null) {
            logger.error("There is no avatar for student with id = " + studentId);
            return new byte[0];
        }
        Path path = Path.of(avatar.getFilePath());
        return Files.readAllBytes(path);
    }

    public Page<Avatar> getAllAvatars(int page, int size) {
        logger.info("Was invoked method for get all avatars with pagination");
        logger.debug("Page: {}, size: {}", page, size);
        if (page < 0) {
            logger.warn("Page number is negative: {}, using 0 instead", page);
        }
        PageRequest pageRequest = PageRequest.of(page, size);
        return avatarRepository.findAll(pageRequest);
    }
}