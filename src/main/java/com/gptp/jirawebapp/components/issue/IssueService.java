package com.gptp.jirawebapp.components.issue;

import com.gptp.jirawebapp.components.project.ProjectDto;
import com.gptp.jirawebapp.components.project.ProjectRepository;
import com.gptp.jirawebapp.components.user.UserDto;
import com.gptp.jirawebapp.components.user.UserRepository;
import com.gptp.jirawebapp.data.Issue;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IssueService {
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public SavedIssueDto create(Long userId, IssueDto dto) throws EntityNotFoundException {
        Issue issue = toIssue(userId, dto);
        Issue savedIssue = issueRepository.save(issue);
        return toSavedIssueDto(savedIssue);
    }

    public SavedIssueDto read(Long id) throws EntityNotFoundException {
        Issue issue = issueRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return toSavedIssueDto(issue);
    }

    public SavedIssueDto update(Long userId, Long id, IssueDto dto) throws EntityNotFoundException {
        Issue newIssue = toIssue(userId, dto);
        Issue issue = issueRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        issue.setName(newIssue.getName());
        issue.setDescription(newIssue.getDescription());
        issue.setProject(newIssue.getProject());
        issue.setDueDate(newIssue.getDueDate());
        issue.setPriority(newIssue.getPriority());
        issue.setAssignee(newIssue.getAssignee());
        issue.setStatus(newIssue.getStatus());
        return toSavedIssueDto(issue);
    }

    public String delete(Long id) {
        issueRepository.deleteById(id);
        return "OK";
    }

    private Issue toIssue(Long userId, IssueDto dto) throws EntityNotFoundException {
        ProjectDto project = projectRepository.findById(dto.getProjectId()).orElseThrow(EntityNotFoundException::new);
        UserDto assignee = userRepository.findById(dto.getAssigneeId()).orElse(null);
        return Issue.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .project(project)
                .creatorId(userId)
                .creationDate(dto.getCreationDate())
                .dueDate(dto.getDueDate())
                .priority(dto.getPriority())
                .assignee(assignee)
                .status(dto.getStatus())
                .build();
    }

    private SavedIssueDto toSavedIssueDto(Issue issue) {
        return SavedIssueDto.builder()
                .id(issue.getId())
                .name(issue.getName())
                .description(issue.getDescription())
                .project(issue.getProject())
                .creatorId(issue.getCreatorId())
                .creationDate(issue.getCreationDate())
                .dueDate(issue.getDueDate())
                .priority(issue.getPriority())
                .assignee(issue.getAssignee())
                .status(issue.getStatus())
                .attachments(issue.getAttachments())
                .comments(issue.getComments())
                .build();
    }
}
