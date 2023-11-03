package com.gptp.jirawebapp.components.project;

import com.gptp.jirawebapp.utilities.JWT;
import com.gptp.jirawebapp.utilities.JWTContent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@org.springframework.stereotype.Controller
@RequestMapping("/api/project")
public class ProjectController {
    private final ProjectRepository repository;
    private final JWT jwt;

    public ProjectController(ProjectRepository repository, JWT jwt) {
        this.repository = repository;
        this.jwt = jwt;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> get(@PathVariable Long id) {
        Optional<ProjectDto> user = repository.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/")
    public Long post(@RequestBody ProjectDto project) {
        JWTContent context = jwt.context();
        return context.getUserId();
    }
}