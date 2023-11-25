package com.gptp.jirawebapp.components.attachment;

import com.gptp.jirawebapp.utilities.JWT;
import com.gptp.jirawebapp.utilities.JWTContent;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/attachment")
@RequiredArgsConstructor
public class AttachmentController {
    private final AttachmentService service;
    private final JWT jwt;

    @PostMapping
    public ResponseEntity<?> create(@ModelAttribute AttachmentDto dto,
                                    @RequestPart("file") MultipartFile file) {
        JWTContent context = jwt.context();
        Long userId = context.getUserId();
        try {
            return ResponseEntity.ok(service.create(userId, dto, file));
        } catch (EntityNotFoundException | IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}/info")
    public ResponseEntity<?> readInfo(@PathVariable(name = "id") Long id) {
        try {
            return ResponseEntity.ok(service.readInfo(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}/file")
    public ResponseEntity<?> readFile(@PathVariable(name = "id") Long id) {
        try {
            byte[] data = service.readFile(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            return new ResponseEntity<>(data, headers, org.springframework.http.HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/issue/{id}")
    public ResponseEntity<?> readByIssue(@PathVariable(name = "id") Long id) {
        try {
            return ResponseEntity.ok(service.readByIssue(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(service.delete(id));
    }
}
