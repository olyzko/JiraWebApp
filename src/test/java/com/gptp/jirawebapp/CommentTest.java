package com.gptp.jirawebapp;

import com.gptp.jirawebapp.components.comment.CommentCreateDto;
import com.gptp.jirawebapp.components.comment.CommentDto;
import com.gptp.jirawebapp.components.comment.CommentRepository;
import com.gptp.jirawebapp.components.comment.CommentService;
import com.gptp.jirawebapp.components.issue.IssueRepository;
import com.gptp.jirawebapp.components.project.ProjectDto;
import com.gptp.jirawebapp.components.user.UserDto;
import com.gptp.jirawebapp.components.user.UserRepository;
import com.gptp.jirawebapp.data.Issue;
import com.gptp.jirawebapp.data.IssueStatus;
import com.gptp.jirawebapp.utilities.JWT;
import com.gptp.jirawebapp.utilities.JWTContent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentTest {
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private IssueRepository issueRepository;

    @Mock
    private JWT jwt;

    @InjectMocks
    private CommentService commentService;

    private CommentCreateDto mockCommentCreateDto() {
        CommentCreateDto commentCreateDto = new CommentCreateDto();
        commentCreateDto.issueId = 1L;
        commentCreateDto.text = "Test Comment";
        return commentCreateDto;
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

    private CommentDto mockCommentDto() {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setCreationDate(new Date());
        commentDto.setCreator(mockUserDto());
        commentDto.setIssue(mockIssue());
        commentDto.setText("Test Comment");
        return commentDto;
    }

    private JWTContent mockJwtContent() {
        return new JWTContent(1L);
    }

    @Test
    void createComment_ShouldCreateComment() {
        CommentDto comment = mockCommentDto();
        CommentCreateDto commentCreateDto = mockCommentCreateDto();

        UserDto userDto = mockUserDto();
        Issue issue = mockIssue();

        when(jwt.context()).thenReturn(mockJwtContent());
        when(userRepository.findById(1L)).thenReturn(Optional.of(userDto));
        when(issueRepository.findById(1L)).thenReturn(Optional.of(issue));
        when(commentRepository.save(any(CommentDto.class))).thenReturn(comment);

        CommentDto createdComment = commentService.createComment(commentCreateDto);

        assertEquals(commentCreateDto.text, createdComment.getText());
        assertEquals(userDto.getEmail(), createdComment.getCreator().getEmail());
        assertEquals(issue.getName(), createdComment.getIssue().getName());

        verify(jwt, times(1)).context();
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(issueRepository, times(1)).findById(any(Long.class));
        verify(commentRepository, times(1)).save(any(CommentDto.class));
    }

    @Test
    void getCommentsByIssue_ShouldReturnComments() {
        Issue issue = mockIssue();
        when(issueRepository.findById(issue.getId())).thenReturn(Optional.of(issue));

        CommentDto comment1 = mockCommentDto();
        comment1.setId(1L);
        CommentDto comment2 = mockCommentDto();
        comment2.setId(2L);
        List<CommentDto> mockComments = Arrays.asList(comment1, comment2);

        when(commentRepository.findByIssue(issue)).thenReturn(mockComments);

        List<CommentDto> comments = commentService.getCommentsByIssue(issue.getId());

        assertEquals(2, comments.size());
        assertEquals(comment1.getId(), comments.get(0).getId());
        assertEquals(comment2.getId(), comments.get(1).getId());

        verify(issueRepository, times(1)).findById(issue.getId());
        verify(commentRepository, times(1)).findByIssue(issue);
    }

    @Test
    void getComment_ShouldReturnComment() {
        Long commentId = 1L;
        CommentDto expectedComment = mockCommentDto();

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(expectedComment));

        CommentDto actualComment = commentService.getComment(commentId);

        assertEquals(expectedComment.getId(), actualComment.getId());
        assertEquals(expectedComment.getText(), actualComment.getText());
        assertEquals(expectedComment.getCreator().getId(), actualComment.getCreator().getId());
        assertEquals(expectedComment.getIssue().getId(), actualComment.getIssue().getId());

        verify(commentRepository, times(1)).findById(commentId);
    }

    @Test
    void updateComment_ShouldUpdateComment() {
        Long commentId = 1L;
        CommentCreateDto updateData = new CommentCreateDto();
        updateData.text = "Updated Comment";
        CommentDto existingComment = mockCommentDto();

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));
        when(commentRepository.save(existingComment)).thenAnswer(invocation -> invocation.getArgument(0));

        CommentDto updatedComment = commentService.updateComment(commentId, updateData);

        assertEquals(updateData.text, updatedComment.getText());
        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, times(1)).save(existingComment);
    }
}
