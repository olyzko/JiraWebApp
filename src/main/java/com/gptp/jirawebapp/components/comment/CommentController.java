package com.gptp.jirawebapp.components.comment;

import com.gptp.jirawebapp.components.issue.IssueRepository;
import com.gptp.jirawebapp.components.user.UserRepository;
import com.gptp.jirawebapp.data.Issue;
import com.gptp.jirawebapp.utilities.JWT;
import com.gptp.jirawebapp.utilities.JWTContent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;


@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentRepository repository;
    private final UserRepository userRepository;
    private final IssueRepository issueRepository;
    private final JWT jwt;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CommentCreateDto data) {
        JWTContent context = jwt.context();
        Long userId = context.getUserId();

        CommentDto dto = new CommentDto();
        dto.setCreator(userRepository.findById(userId).orElseThrow());
        dto.setCreationDate(Date.from(Instant.now()));
        dto.setText(data.text);
        dto.setIssue(issueRepository.findById(data.issueId).orElseThrow());

        return ResponseEntity.ok(repository.save(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> read(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(repository.findById(id));
    }

    @GetMapping("/issue/{id}")
    public ResponseEntity<?> listByProject(@PathVariable(name = "id") Long issueId) {
        Issue issue = issueRepository.findById(issueId).orElseThrow();
        return ResponseEntity.ok(repository.findByIssue(issue));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
        repository.deleteById(id);
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") Long id, @RequestBody CommentCreateDto data) {
        CommentDto dto = repository.findById(id).orElseThrow();
        dto.setText(data.text);
        return ResponseEntity.ok(repository.save(dto));
    }
}
