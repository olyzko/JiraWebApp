package com.gptp.jirawebapp.components.role;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@org.springframework.stereotype.Controller
@RequestMapping("/api/role")
@AllArgsConstructor
public class RoleController {
    private final RoleRepository repository;

    @GetMapping
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(repository.findAll());
    }
}
