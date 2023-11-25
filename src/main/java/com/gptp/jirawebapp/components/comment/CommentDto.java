package com.gptp.jirawebapp.components.comment;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gptp.jirawebapp.components.user.UserDto;
import com.gptp.jirawebapp.data.Issue;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "comment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "creation_date")
    private Date creationDate;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "creator_id")
    private UserDto creator;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "issue_id")
    private Issue issue;

    @Column(name = "text")
    private String text;

}
