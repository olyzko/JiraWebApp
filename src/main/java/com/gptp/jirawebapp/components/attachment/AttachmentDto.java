package com.gptp.jirawebapp.components.attachment;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class AttachmentDto {

    @NonNull
    private Long issueId;

    @NonNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date uploadDate;

    @NonNull
    private String fileName;
}
