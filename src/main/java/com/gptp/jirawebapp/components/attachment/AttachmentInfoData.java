package com.gptp.jirawebapp.components.attachment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class AttachmentInfoData {
    private Long id;

    private Long creatorId;

    private Long issueId;

    private Date uploadDate;

    private String fileName;
}
