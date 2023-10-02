package com.gptp.jirawebapp.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "attachment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToOne
    @JoinColumn(name = "issue_id")
    private Issue issue;

    @Temporal(TemporalType.DATE)
    @Column(name = "upload_date")
    private Date uploadDate;

    @Column(name = "file_path", length = 512)
    private String filePath;

}
