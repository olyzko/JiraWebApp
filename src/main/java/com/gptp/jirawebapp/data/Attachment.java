package com.gptp.jirawebapp.data;

import com.gptp.jirawebapp.components.user.UserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
    private UserDto creator;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "issue_id")
    private Issue issue;

    @Temporal(TemporalType.DATE)
    @Column(name = "upload_date")
    private Date uploadDate;

    @Column(name = "file_path", length = 512)
    private String filePath;

}
