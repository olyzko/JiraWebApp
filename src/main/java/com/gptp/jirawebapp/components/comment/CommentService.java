package com.gptp.jirawebapp.components.comment;

import com.gptp.jirawebapp.components.issue.IssueRepository;
import com.gptp.jirawebapp.components.user.UserRepository;
import com.gptp.jirawebapp.data.Issue;
import com.gptp.jirawebapp.utilities.JWT;
import com.gptp.jirawebapp.utilities.JWTContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final IssueRepository issueRepository;
    private final JWT jwt;

    public CommentDto createComment(CommentCreateDto data) {
        JWTContent context = jwt.context();
        Long userId = context.getUserId();

        CommentDto comment = new CommentDto();
        comment.setCreator(userRepository.findById(userId).orElseThrow());
        comment.setCreationDate(Date.from(Instant.now()));
        comment.setText(data.text);
        comment.setIssue(issueRepository.findById(data.issueId).orElseThrow());

        return commentRepository.save(comment);
    }

    public CommentDto getComment(Long id) {
        return commentRepository.findById(id).orElseThrow();
    }

    public List<CommentDto> getCommentsByIssue(Long issueId) {
        Issue issue = issueRepository.findById(issueId).orElseThrow();
        return commentRepository.findByIssue(issue);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    public CommentDto updateComment(Long id, CommentCreateDto data) {
        CommentDto comment = commentRepository.findById(id).orElseThrow();
        comment.setText(data.text);
        return commentRepository.save(comment);
    }
}
