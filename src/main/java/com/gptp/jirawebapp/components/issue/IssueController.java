package com.gptp.jirawebapp.components.issue;

import com.gptp.jirawebapp.utilities.JWT;
import com.gptp.jirawebapp.utilities.JWTContent;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/issue")
@RequiredArgsConstructor
public class IssueController {
    private final IssueService service;
    private final JWT jwt;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody IssueDto dto) {
        JWTContent context = jwt.context();
        Long userId = context.getUserId();
        try {
            return ResponseEntity.ok(service.create(userId, dto));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> read(@PathVariable(name = "id") Long id) {
        try {
            return ResponseEntity.ok(service.read(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable(name = "id") Long id,
            @RequestBody IssueDto dto
    ) {
        JWTContent context = jwt.context();
        Long userId = context.getUserId();
        try {
            return ResponseEntity.ok(service.update(userId, id, dto));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(service.delete(id));
    }

    @GetMapping("/{id}/issues/info")
    public ResponseEntity<?> readIssues(@PathVariable(name = "id") Long id) {
        try {
            return ResponseEntity.ok(service.readIssues(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
