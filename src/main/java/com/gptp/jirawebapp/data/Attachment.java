package com.gptp.jirawebapp.data;

import com.gptp.jirawebapp.components.user.UserDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "attachment")
@Data
@Builder
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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "upload_date")
    private Date uploadDate;

    @Column(name = "file_name", length = 128)
    private String fileName;

    @Column(name = "file", length = 128)
    private byte[] file;
}
