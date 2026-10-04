package ru.hogwarts.school.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import java.io.IOException;

@RestController
@RequestMapping("/avatar")
public class AvatarController {

    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(value = "/{studentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> uploadAvatar(@PathVariable Long studentId,
                                             @RequestParam MultipartFile file) throws IOException {
        avatarService.uploadAvatar(studentId, file);
        return ResponseEntity.ok(studentId);
    }

    @GetMapping(value = "/{studentId}/preview")
    public ResponseEntity<byte[]> findAvatarFromDb(@PathVariable Long studentId) {
        Avatar avatar = avatarService.findAvatarFromDb(studentId);
        if (avatar == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(avatar.getData());
    }

    @GetMapping(value = "/{studentId}")
    public ResponseEntity<byte[]> findAvatarFromDisk(@PathVariable Long studentId) throws IOException {
        byte[] data = avatarService.findAvatarFromDisk(studentId);
        if (data.length == 0) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(data);
    }
    @GetMapping
    public Page<Avatar> getAllAvatars(@RequestParam int page,
                                      @RequestParam int size) {
        return avatarService.getAllAvatars(page, size);
    }
}