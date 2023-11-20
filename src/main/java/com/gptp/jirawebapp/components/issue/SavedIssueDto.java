package com.gptp.jirawebapp.components.issue;

import com.gptp.jirawebapp.components.project.ProjectDto;
import com.gptp.jirawebapp.components.user.UserDto;
import com.gptp.jirawebapp.data.Attachment;
import com.gptp.jirawebapp.data.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class SavedIssueDto {
    private Long id;

    private String name;

    private String description;

    private ProjectDto project;

    private Long creatorId;

    private Date creationDate;

    private Date dueDate;

    private Integer priority;

    private UserDto assignee;

    private String status;

    private Set<Attachment> attachments;

    private Set<Comment> comments;
}
