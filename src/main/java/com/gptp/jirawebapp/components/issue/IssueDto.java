package com.gptp.jirawebapp.components.issue;

import com.gptp.jirawebapp.data.IssueStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class IssueDto {

    @NonNull
    private String name;

    private String description;

    @NonNull
    private Long projectId;

    @NonNull
    private Date creationDate;

    private Date dueDate;

    @NonNull
    private Integer priority;

    private Long assigneeId;

    @NonNull
    private IssueStatus status;

}
