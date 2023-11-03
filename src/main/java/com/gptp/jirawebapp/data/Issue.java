package com.gptp.jirawebapp.data;

import com.gptp.jirawebapp.components.project.ProjectDto;
import com.gptp.jirawebapp.components.user.UserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "issue")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectDto project;

    @Column(name = "creator_id")
    private Long creatorId;

    @Temporal(TemporalType.DATE)
    @Column(name = "creation_date")
    private Date creationDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "due_date")
    private Date dueDate;

    @Column(name = "priority")
    private Integer priority;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private UserDto assignee;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL)
    private Set<Attachment> attachments = new HashSet<>();

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL)
    private Set<IssueUser> issueUsers = new HashSet<>();
}
