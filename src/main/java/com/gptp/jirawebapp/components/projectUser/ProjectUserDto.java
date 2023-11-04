package com.gptp.jirawebapp.components.projectUser;

import com.gptp.jirawebapp.components.project.ProjectDto;
import com.gptp.jirawebapp.components.user.UserDto;
import com.gptp.jirawebapp.components.role.RoleDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "project_user_role")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ProjectUserId.class)
public class ProjectUserDto {
    @Id
    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectDto project;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDto user;

    @OneToOne
    @JoinColumn(name = "role_id")
    private RoleDto role;
}