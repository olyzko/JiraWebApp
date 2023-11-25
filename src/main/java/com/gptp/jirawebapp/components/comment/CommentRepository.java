package com.gptp.jirawebapp.components.comment;

import com.gptp.jirawebapp.data.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommentRepository extends JpaRepository<CommentDto, Long> {
    List<CommentDto> findByIssue(Issue issue);
}
