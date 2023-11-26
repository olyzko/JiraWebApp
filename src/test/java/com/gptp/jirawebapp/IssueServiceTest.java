package com.gptp.jirawebapp;

import com.gptp.jirawebapp.components.issue.IssueDto;
import com.gptp.jirawebapp.components.issue.IssueRepository;
import com.gptp.jirawebapp.components.issue.IssueService;
import com.gptp.jirawebapp.components.issue.SavedIssueDto;
import com.gptp.jirawebapp.components.project.ProjectDto;
import com.gptp.jirawebapp.components.project.ProjectRepository;
import com.gptp.jirawebapp.components.user.UserDto;
import com.gptp.jirawebapp.components.user.UserRepository;
import com.gptp.jirawebapp.data.Issue;
import com.gptp.jirawebapp.data.IssueStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IssueServiceTest {
    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private IssueRepository issueRepository;

    @InjectMocks
    private IssueService issueService;

    private ProjectDto mockProjectDto() {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(1L);
        projectDto.setCreator(mockUserDto());
        projectDto.setName("Project name");
        projectDto.setDescription("Project description");
        projectDto.setCreationDate(new Date());
        projectDto.setStartDate(new Date());
        projectDto.setEndDate(new Date());
        projectDto.setIssues(new HashSet<>());
        return projectDto;
    }

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

    private Issue mockIssue() {
        Issue issue = new Issue();
        issue.setId(2L);
        issue.setName("Issue name 2");
        issue.setDescription("Issue description 2");
        issue.setProject(mockProjectDto());
        issue.setCreatorId(2L);
        issue.setCreationDate(new Date());
        issue.setDueDate(new Date());
        issue.setPriority(2);
        issue.setAssignee(mockUserDto());
        issue.setStatus(IssueStatus.DONE);
        issue.setAttachments(new HashSet<>());
        issue.setComments(new HashSet<>());
        return issue;
    }

    private Issue mockMapToIssue() {
        Issue issue = new Issue();
        issue.setId(1L);
        issue.setName("Issue name");
        issue.setDescription("Issue description");
        issue.setProject(mockProjectDto());
        issue.setCreatorId(1L);
        issue.setCreationDate(new Date());
        issue.setDueDate(new Date());
        issue.setPriority(1);
        issue.setAssignee(mockUserDto());
        issue.setStatus(IssueStatus.IN_PROGRESS);
        issue.setAttachments(new HashSet<>());
        issue.setComments(new HashSet<>());
        return issue;
    }

    @Test
    void testCreateIssue() {
        // Prepare
        IssueDto issueDto = IssueDto.builder()
                .name("Issue name")
                .description("Issue description")
                .projectId(1L)
                .creationDate(new Date())
                .dueDate(new Date())
                .priority(1)
                .assigneeId(1L)
                .status(IssueStatus.IN_PROGRESS)
                .build();
        Long userId = 1L;

        when(projectRepository.findById(1L)).thenReturn(Optional.of(mockProjectDto()));
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUserDto()));
        when(issueRepository.save(any(Issue.class))).thenReturn(mockMapToIssue());

        // Test
        SavedIssueDto savedIssueDto = issueService.create(userId, issueDto);

        // Assert
        assertNotNull(savedIssueDto);
        assertEquals("Issue name", savedIssueDto.getName());
        assertEquals("Issue description", savedIssueDto.getDescription());
        assertEquals(1L, savedIssueDto.getProject().getId());
        assertEquals(1L, savedIssueDto.getCreatorId());
        assertEquals(1, savedIssueDto.getPriority());
        assertEquals(1L, savedIssueDto.getAssignee().getId());
        assertEquals(IssueStatus.IN_PROGRESS, savedIssueDto.getStatus());

        // Verify interactions
        verify(projectRepository).findById(1L);
        verify(userRepository).findById(1L);
        verify(issueRepository).save(any(Issue.class));
    }

    @Test
    void testUpdateIssue() {
        // Prepare
        IssueDto issueDto = IssueDto.builder()
                .name("Issue name")
                .description("Issue description")
                .projectId(1L)
                .creationDate(new Date())
                .dueDate(new Date())
                .priority(1)
                .assigneeId(1L)
                .status(IssueStatus.IN_PROGRESS)
                .build();
        Long id = 2L;
        Long userId = 1L;

        when(projectRepository.findById(1L)).thenReturn(Optional.of(mockProjectDto()));
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUserDto()));
        when(issueRepository.findById(id)).thenReturn(Optional.of(mockIssue()));

        // Test
        SavedIssueDto savedIssueDto = issueService.update(userId, id, issueDto);

        // Assert
        assertEquals("Issue name", savedIssueDto.getName());
        assertEquals("Issue description", savedIssueDto.getDescription());
        assertEquals(1L, savedIssueDto.getProject().getId());
        assertEquals(2L, savedIssueDto.getCreatorId());
        assertEquals(1, savedIssueDto.getPriority());
        assertEquals(1L, savedIssueDto.getAssignee().getId());
        assertEquals(IssueStatus.IN_PROGRESS, savedIssueDto.getStatus());

        // Verify interactions
        verify(projectRepository).findById(1L);
        verify(userRepository).findById(1L);
        verify(issueRepository).findById(id);
    }

    @Test
    void testRead() {
        // Prepare
        Long id = 1L;
        when(issueRepository.findById(id)).thenReturn(Optional.of(mockIssue()));

        // Test
        SavedIssueDto readIssue = issueService.read(id);

        // Assert
        assertEquals("Issue name 2", readIssue.getName());
        assertEquals("Issue description 2", readIssue.getDescription());
        assertEquals(1L, readIssue.getProject().getId());
        assertEquals(2L, readIssue.getCreatorId());
        assertEquals(2, readIssue.getPriority());
        assertEquals(1L, readIssue.getAssignee().getId());
        assertEquals(IssueStatus.DONE, readIssue.getStatus());

        // Verify interactions
        verify(issueRepository).findById(id);
    }

    @Test
    void testByProject() {
        // Prepare
        IssueDto issueDto1 = IssueDto.builder()
                .name("Issue name 1")
                .description("Issue description 1")
                .projectId(1L)
                .creationDate(new Date())
                .dueDate(new Date())
                .priority(2)
                .assigneeId(1L)
                .status(IssueStatus.DONE)
                .build();
        IssueDto issueDto2 = IssueDto.builder()
                .name("Issue name 2")
                .description("Issue description 2")
                .projectId(1L)
                .creationDate(new Date())
                .dueDate(new Date())
                .priority(2)
                .assigneeId(1L)
                .status(IssueStatus.DONE)
                .build();

        Long projectId = 1L;
        when(issueRepository.findAllByProject(projectId))
                .thenReturn(List.of(issueDto1, issueDto2));

        // Test
        List<IssueDto> issueDtoList = issueService.listByProject(projectId);

        // Assert
        assertEquals(2, issueDtoList.size());
        assertEquals("Issue name 1", issueDtoList.get(0).getName());
        assertEquals("Issue description 1", issueDtoList.get(0).getDescription());
        assertEquals(1L, issueDtoList.get(0).getProjectId());
        assertEquals(2, issueDtoList.get(0).getPriority());
        assertEquals(1L, issueDtoList.get(0).getAssigneeId());
        assertEquals(IssueStatus.DONE, issueDtoList.get(0).getStatus());

        assertEquals("Issue name 2", issueDtoList.get(1).getName());
        assertEquals("Issue description 2", issueDtoList.get(1).getDescription());
        assertEquals(1L, issueDtoList.get(1).getProjectId());
        assertEquals(2, issueDtoList.get(1).getPriority());
        assertEquals(1L, issueDtoList.get(1).getAssigneeId());
        assertEquals(IssueStatus.DONE, issueDtoList.get(1).getStatus());

        // Verify interactions
        verify(issueRepository).findAllByProject(projectId);
    }

    @Test
    void testByAssignee() {
        // Prepare
        IssueDto issueDto1 = IssueDto.builder()
                .name("Issue name 1")
                .description("Issue description 1")
                .projectId(1L)
                .creationDate(new Date())
                .dueDate(new Date())
                .priority(2)
                .assigneeId(1L)
                .status(IssueStatus.DONE)
                .build();
        IssueDto issueDto2 = IssueDto.builder()
                .name("Issue name 2")
                .description("Issue description 2")
                .projectId(1L)
                .creationDate(new Date())
                .dueDate(new Date())
                .priority(2)
                .assigneeId(1L)
                .status(IssueStatus.DONE)
                .build();

        Long userId = 1L;
        when(issueRepository.findAllByAssignee(userId))
                .thenReturn(List.of(issueDto1, issueDto2));

        // Test
        List<IssueDto> issueDtoList = issueService.listByAssignee(userId);

        // Assert
        assertEquals(2, issueDtoList.size());
        assertEquals("Issue name 1", issueDtoList.get(0).getName());
        assertEquals("Issue description 1", issueDtoList.get(0).getDescription());
        assertEquals(1L, issueDtoList.get(0).getProjectId());
        assertEquals(2, issueDtoList.get(0).getPriority());
        assertEquals(1L, issueDtoList.get(0).getAssigneeId());
        assertEquals(IssueStatus.DONE, issueDtoList.get(0).getStatus());

        assertEquals("Issue name 2", issueDtoList.get(1).getName());
        assertEquals("Issue description 2", issueDtoList.get(1).getDescription());
        assertEquals(1L, issueDtoList.get(1).getProjectId());
        assertEquals(2, issueDtoList.get(1).getPriority());
        assertEquals(1L, issueDtoList.get(1).getAssigneeId());
        assertEquals(IssueStatus.DONE, issueDtoList.get(1).getStatus());

        // Verify interactions
        verify(issueRepository).findAllByAssignee(userId);
    }
}
