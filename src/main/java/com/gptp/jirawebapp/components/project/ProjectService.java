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
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private final ProjectRepository repository;
    private final UserRepository userRepository;
    private final ProjectUserRepository projectUserRepository;
    private final RoleRepository roleRepository;
    private final JWT jwt;

    public ProjectService(ProjectRepository repository,
                          UserRepository userRepository,
                          ProjectUserRepository projectUserRepository,
                          RoleRepository roleRepository,
                          JWT jwt) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.projectUserRepository = projectUserRepository;
        this.roleRepository = roleRepository;
        this.jwt = jwt;
    }

    public Optional<ProjectDto> getProjectById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public ProjectDto createProject(ProjectDto project) {
        JWTContent context = jwt.context();
        Long userId = context.getUserId();
        UserDto user = userRepository.findById(userId).orElseThrow();
        project.setCreator(user);
        ProjectDto savedProject = repository.save(project);
        Long roleId = roleRepository.findOwnerRoleId().orElseThrow();
        ProjectAddUserData data = ProjectAddUserData.builder()
                .userId(userId)
                .roleId(roleId)
                .build();
        addUserToProject(savedProject.getId(), data);
        return savedProject;
    }

    public ProjectDto updateProject(Long id, ProjectDto data) {
        ProjectDto project = repository.findById(id).orElseThrow();
        project.setName(data.getName());
        project.setDescription(data.getDescription());
        project.setStartDate(data.getStartDate());
        project.setEndDate(data.getEndDate());
        project.setCreationDate(data.getCreationDate());
        return repository.save(project);
    }

    public void deleteProject(Long id) {
        repository.deleteById(id);
    }

    public List<UserWithRole> getProjectUsers(Long id) {
        List<ProjectUserDto> projectUserRoles = projectUserRepository.findByProjectId(id);
        return projectUserRoles.stream()
                .map((dto) -> new UserWithRole(dto.getUser(), dto.getRole()))
                .collect(Collectors.toList());
    }

    public ProjectUserDto addUserToProject(Long id, ProjectAddUserData data) {
        ProjectUserDto dto = new ProjectUserDto();
        UserDto user = userRepository.findById(data.userId).orElseThrow();
        ProjectDto project = repository.findById(id).orElseThrow();
        RoleDto role = roleRepository.findById(data.roleId).orElseThrow();
        dto.setProject(project);
        dto.setUser(user);
        dto.setRole(role);
        return projectUserRepository.save(dto);
    }

    public String removeUserFromProject(Long projectId, Long userId) {
        projectUserRepository.deleteById(new ProjectUserId(projectId, userId));
        return "OK";
    }
}