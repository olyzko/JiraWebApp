package com.gptp.jirawebapp.components.issue;

import com.gptp.jirawebapp.data.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
}
