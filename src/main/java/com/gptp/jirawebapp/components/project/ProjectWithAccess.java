package com.gptp.jirawebapp.components.project;

public class ProjectWithAccess extends ProjectDto {
    public boolean canModify;

    public ProjectWithAccess(ProjectDto project, boolean canModify) {
        super(project.getId(),
                project.getCreator(),
                project.getName(),
                project.getDescription(),
                project.getCreationDate(),
                project.getStartDate(),
                project.getEndDate(),
                project.getIssues()
        );
        this.canModify = canModify;
    }
}
