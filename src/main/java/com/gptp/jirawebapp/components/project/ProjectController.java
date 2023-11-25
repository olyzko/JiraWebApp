package com.gptp.jirawebapp.components.project;

import com.gptp.jirawebapp.components.projectUser.ProjectUserDto;
import com.gptp.jirawebapp.components.projectUser.ProjectUserId;
import com.gptp.jirawebapp.components.projectUser.ProjectUserRepository;
import com.gptp.jirawebapp.components.role.RoleDto;
import com.gptp.jirawebapp.components.role.RoleRepository;
import com.gptp.jirawebapp.components.user.UserDto;
import com.gptp.jirawebapp.components.user.UserRepository;
import com.gptp.jirawebapp.utilities.JWT;
import com.gptp.jirawebapp.utilities.JWTContent;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@org.springframework.stereotype.Controller
@RequestMapping("/api/project")
@AllArgsConstructor
public class ProjectController {
    private final ProjectRepository repository;
    private final UserRepository userRepository;
    private final ProjectUserRepository projectUserRepository;
    private final ProjectRepository projectRepository;
    private final RoleRepository roleRepository;
    private final JWT jwt;

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        Optional<ProjectDto> user = repository.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProjectDto project) {
        JWTContent context = jwt.context();
        Long userId = context.getUserId();
        UserDto user = userRepository.findById(userId).orElseThrow();
        project.setCreator(user);
        ProjectDto saved = repository.save(project);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ProjectDto data) {
        ProjectDto project = repository.findById(id).orElseThrow();
        project.setName(data.getName());
        project.setDescription(data.getDescription());
        project.setStartDate(data.getStartDate());
        project.setEndDate(data.getEndDate());
        project.setCreationDate(data.getCreationDate());
        ProjectDto saved = repository.save(project);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<?> getUsers(@PathVariable Long id) {
        List<ProjectUserDto> projectUserRoles = projectUserRepository.findByProjectId(id);

        List<UserWithRole> users = projectUserRoles.stream()
                .map((dto) -> new UserWithRole(dto.getUser(), dto.getRole()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    @PostMapping("/{id}/user")
    public ResponseEntity<?> addUser(@PathVariable Long id, @RequestBody ProjectAddUserData data) {
        ProjectUserDto dto = new ProjectUserDto();
        UserDto user = userRepository.findById(data.userId).orElseThrow();
        ProjectDto project = projectRepository.findById(id).orElseThrow();
        RoleDto role = roleRepository.findById(data.roleId).orElseThrow();
        dto.setProject(project);
        dto.setUser(user);
        dto.setRole(role);
        projectUserRepository.save(dto);
        return ResponseEntity.ok("OK");
    }

    @DeleteMapping("/{projectId}/user/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long projectId, @PathVariable Long userId) {
        projectUserRepository.deleteById(new ProjectUserId(projectId, userId));
        return ResponseEntity.ok("OK");
    }
}