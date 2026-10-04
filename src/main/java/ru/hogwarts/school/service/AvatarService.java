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

@Service
public class AvatarService {

    private final AvatarRepository avatarRepository;
    private final StudentService studentService;

    @Value("${avatar.dir.path}")
    private String avatarDirPath;

    public AvatarService(AvatarRepository avatarRepository, StudentService studentService) {
        this.avatarRepository = avatarRepository;
        this.studentService = studentService;
    }

    public Avatar findAvatar(Long studentId) {
        return avatarRepository.findByStudentId(studentId).orElse(null);
    }

    public void uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        Student student = studentService.getStudent(studentId);
        if (student == null) {
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
        return avatarRepository.findByStudentId(studentId).orElse(null);
    }

    public byte[] findAvatarFromDisk(Long studentId) throws IOException {
        Avatar avatar = avatarRepository.findByStudentId(studentId).orElse(null);
        if (avatar == null) {
            return new byte[0];
        }
        Path path = Path.of(avatar.getFilePath());
        return Files.readAllBytes(path);
    }
    public Page<Avatar> getAllAvatars(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return avatarRepository.findAll(pageRequest);
    }
}