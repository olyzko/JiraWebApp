package com.gptp.jirawebapp.components.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gptp.jirawebapp.components.project.ProjectDto;
import com.gptp.jirawebapp.data.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "\"user\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    @NotBlank
    private String email;

    @Column(name = "password", length = 256)
    @NotBlank
    @JsonIgnore
    private String password;

    @Column(name= "first_name")
    @NotBlank
    private String firstName;

    @Column(name = "last_name")
    @NotBlank
    private String lastName;


    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private Set<ProjectDto> createdProjects = new HashSet<>();

    @OneToMany(mappedBy = "assignee", cascade = CascadeType.ALL)
    private Set<Issue> issues = new HashSet<>();

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private Set<Attachment> attachments = new HashSet<>();

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<IssueUser> issueUsers = new HashSet<>();
}
