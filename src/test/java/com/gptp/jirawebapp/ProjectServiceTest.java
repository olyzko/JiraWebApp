package com.gptp.jirawebapp;

import com.gptp.jirawebapp.components.project.*;
import com.gptp.jirawebapp.components.projectUser.ProjectUserDto;
import com.gptp.jirawebapp.components.projectUser.ProjectUserRepository;
import com.gptp.jirawebapp.components.role.RoleDto;
import com.gptp.jirawebapp.components.role.RoleRepository;
import com.gptp.jirawebapp.components.user.UserDto;
import com.gptp.jirawebapp.components.user.UserRepository;
import com.gptp.jirawebapp.data.Issue;
import com.gptp.jirawebapp.utilities.JWT;
import com.gptp.jirawebapp.utilities.JWTContent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {
    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JWT jwt;

    @Mock
    private ProjectUserRepository projectUserRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private ProjectService projectService;


    private UserDto mockUserDto() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("some@gmai.com");
        userDto.setPassword("password");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setCreatedProjects(new HashSet<>());
        userDto.setIssues(new HashSet<>());
        userDto.setAttachments(new HashSet<>());
        userDto.setComments(new HashSet<>());
        return userDto;
    }

    private UserDto mockAnotherUserDto() {
        UserDto userDto = new UserDto();
        userDto.setId(2L);
        userDto.setEmail("helloWorld@gmail.com");
        userDto.setPassword("password");
        userDto.setFirstName("Yoda");
        userDto.setLastName("Yumy");
        userDto.setCreatedProjects(new HashSet<>());
        userDto.setIssues(new HashSet<>());
        userDto.setAttachments(new HashSet<>());
        userDto.setComments(new HashSet<>());
        return userDto;
    }

    private ProjectDto mockProjectDto() {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(1L);
        projectDto.setName("Test Project");
        projectDto.setDescription("Test Description");
        projectDto.setCreationDate(new Date());
        projectDto.setStartDate(new Date());
        projectDto.setEndDate(new Date());
        projectDto.setCreator(new UserDto());
        Set<Issue> issues = new HashSet<>();
        projectDto.setIssues(issues);
        return projectDto;
    }

    private RoleDto mockRoleDto() {
        RoleDto roleDto = new RoleDto();
        roleDto.setId(1L);
        roleDto.setName("READER");
        return roleDto;
    }

    private ProjectUserDto mockProjectUserDto(UserDto userDto) {
        ProjectUserDto projectUserDto = new ProjectUserDto();
        projectUserDto.setProject(mockProjectDto());
        projectUserDto.setUser(userDto);
        projectUserDto.setRole(mockRoleDto());
        return projectUserDto;
    }

    @Test
    public void createProject_Success() {
        // Given
        Long userId = 1L;
        JWTContent jwtContent = mock(JWTContent.class);
        when(jwtContent.getUserId()).thenReturn(userId);
        when(jwt.context()).thenReturn(jwtContent);

        UserDto mockUser = mockUserDto();
        mockUser.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        ProjectDto newProject = mockProjectDto();

        ProjectDto savedProject = mockProjectDto();
        savedProject.setId(1L);
        when(projectRepository.save(any(ProjectDto.class))).thenReturn(savedProject);

        // When
        ProjectDto result = projectService.createProject(newProject);

        // Then
        assertNotNull(result);
        assertEquals("Test Project", result.getName());
        assertEquals("Test Description", result.getDescription());
        assertEquals(newProject.getCreationDate(), result.getCreationDate());
        assertEquals(newProject.getStartDate(), result.getStartDate());
        assertEquals(newProject.getEndDate(), result.getEndDate());
        assertEquals(mockUser, newProject.getCreator());

        verify(projectRepository).save(newProject);
    }

    @Test
    public void createProject_UserNotFound_ThrowsException() {
        // Given
        Long userId = 1L;
        JWTContent jwtContent = mock(JWTContent.class);
        when(jwtContent.getUserId()).thenReturn(userId);
        when(jwt.context()).thenReturn(jwtContent);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ProjectDto newProject = new ProjectDto();
        newProject.setName("Test Project");
        newProject.setDescription("Test Description");

        // When & Then
        assertThrows(RuntimeException.class, () -> projectService.createProject(newProject));
    }

    @Test
    public void updateProject_Success() {
        // Given
        Long projectId = 1L;
        ProjectDto existingProject = mockProjectDto();

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(existingProject));

        ProjectDto updateData = mockProjectDto();
        updateData.setName("Updated Name");
        updateData.setDescription("Updated Description");

        when(projectRepository.save(any(ProjectDto.class))).thenReturn(updateData);

        // When
        ProjectDto updatedProject = projectService.updateProject(projectId, updateData);

        // Then
        assertNotNull(updatedProject);
        assertEquals("Updated Name", updatedProject.getName());
        assertEquals("Updated Description", updatedProject.getDescription());

        verify(projectRepository).findById(projectId);
        verify(projectRepository).save(updatedProject);
    }

    @Test
    public void updateProject_NotFound_ThrowsException() {
        // Given
        Long projectId = 1L;
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        ProjectDto updateData = new ProjectDto();
        updateData.setName("Updated Name");

        // When & Then
        assertThrows(RuntimeException.class, () -> projectService.updateProject
                (projectId, updateData));
    }

    @Test
    public void getProjectUsers_Success() {
        // Given
        Long projectId = 1L;

        ProjectUserDto projectUserDto1 = mockProjectUserDto(mockUserDto());
        ProjectUserDto projectUserDto2 = mockProjectUserDto(mockAnotherUserDto());
        when(projectUserRepository.findByProjectId(projectId))
                .thenReturn(Arrays.asList(projectUserDto1, projectUserDto2));

        List<UserWithRole> result = projectService.getProjectUsers(projectId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(projectUserDto1.getRole().getName(), result.get(0).role.getName());
        assertEquals(projectUserDto1.getUser().getEmail(), result.get(0).getEmail());
        assertEquals(projectUserDto2.getRole().getName(), result.get(1).role.getName());
        assertEquals(projectUserDto2.getUser().getEmail(), result.get(1).getEmail());

        verify(projectUserRepository).findByProjectId(projectId);
    }

    @Test
    public void getProjectUsers_EmptyList() {
        // Given
        Long projectId = 1L;
        when(projectUserRepository.findByProjectId(projectId)).thenReturn(Collections.emptyList());

        // When
        List<UserWithRole> result = projectService.getProjectUsers(projectId);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void addUserToProjectTest() {
        // Given
        Long projectId = 1L;
        Long userId = 1L;
        Long roleId = 1L;
        ProjectAddUserData data = new ProjectAddUserData();
        data.setUserId(userId);
        data.setRoleId(roleId);

        UserDto user = mockUserDto();
        ProjectDto project = mockProjectDto();
        RoleDto role = mockRoleDto();
        ProjectUserDto projectUserDto = mockProjectUserDto(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(projectUserRepository.save(any(ProjectUserDto.class))).thenReturn(projectUserDto);

        // When
        ProjectUserDto result = projectService.addUserToProject(projectId, data);

        assertNotNull(result);
        assertEquals(projectUserDto.getProject(), result.getProject());
        assertEquals(projectUserDto.getUser(), result.getUser());
        assertEquals(projectUserDto.getRole(), result.getRole());

        // Then
        verify(userRepository).findById(userId);
        verify(projectRepository).findById(projectId);
        verify(roleRepository).findById(roleId);
        verify(projectUserRepository).save(any(ProjectUserDto.class));
    }
}
