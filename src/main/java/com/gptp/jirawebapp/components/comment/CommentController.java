package com.gptp.jirawebapp.components.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CommentCreateDto data) {
        return ResponseEntity.ok(commentService.createComment(data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> read(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(commentService.getComment(id));
    }

    @GetMapping("/issue/{id}")
    public ResponseEntity<?> listByProject(@PathVariable(name = "id") Long issueId) {
        return ResponseEntity.ok(commentService.getCommentsByIssue(issueId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok("Comment deleted successfully");
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") Long id,
                                    @RequestBody CommentCreateDto data) {
        return ResponseEntity.ok(commentService.updateComment(id, data));
    }
}
