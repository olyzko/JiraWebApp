package com.gptp.jirawebapp.components.issue;

import com.gptp.jirawebapp.data.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    @Query("SELECT NEW com.gptp.jirawebapp.components.issue.IssueDto(i.name, i.description, i.project.id, i.creationDate, i.dueDate, i.priority, i.assignee.id, i.status) FROM Issue i WHERE i.project.id = :projectId")
    List<IssueDto> findAllByProject(@Param("projectId") Long projectId);

    @Query("SELECT NEW com.gptp.jirawebapp.components.issue.IssueDto(i.name, i.description, i.project.id, i.creationDate, i.dueDate, i.priority, i.assignee.id, i.status) FROM Issue i WHERE i.assignee.id = :userId")
    List<IssueDto> findAllByAssignee(@Param("userId") Long userId);
}
