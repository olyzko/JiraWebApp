package com.gptp.jirawebapp.components.attachment;

import com.gptp.jirawebapp.components.issue.IssueRepository;
import com.gptp.jirawebapp.components.user.UserDto;
import com.gptp.jirawebapp.components.user.UserRepository;
import com.gptp.jirawebapp.data.Attachment;
import com.gptp.jirawebapp.data.Issue;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;

    public AttachmentInfoData create(Long userId, AttachmentDto dto, MultipartFile file)
            throws EntityNotFoundException, IOException {
        Attachment attachment = toAttachment(userId, dto, file);
        Attachment savedAttachment = attachmentRepository.save(attachment);
        return toAttachmentInfoData(savedAttachment);
    }

    public AttachmentInfoData readInfo(Long id) throws EntityNotFoundException {
        List<Object[]> resultList = attachmentRepository.findInfoById(id);
        if (resultList.isEmpty()) {
            throw new EntityNotFoundException();
        }
        Object[] result = resultList.get(0);
        return new AttachmentInfoData(
                (Long) result[0], // id
                (Long) result[1], // creatorId
                (Long) result[2], // issueId
                (Date) result[3], // uploadDate
                (String) result[4] // fileName
        );
    }

    public byte[] readFile(Long id) throws EntityNotFoundException {
        Attachment attachment = attachmentRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return attachment.getFile();
    }

    public List<AttachmentInfoData> readByIssue(Long id) {
        List<Object[]> resultList = attachmentRepository.findInfoListByIssueId(id);
        return resultList.stream()
                .map(objects -> new AttachmentInfoData(
                        (Long) objects[0], // id
                        (Long) objects[1], // creatorId
                        (Long) objects[2], // issueId
                        (Date) objects[3], // uploadDate
                        (String) objects[4] // fileName
                ))
                .collect(Collectors.toList());
    }

    public String delete(Long id) throws EntityNotFoundException {
        attachmentRepository.deleteById(id);
        return "OK";
    }

    private Attachment toAttachment(Long userId, AttachmentDto dto, MultipartFile file) throws IOException {
        Issue issue = issueRepository.findById(dto.getIssueId()).orElseThrow(EntityNotFoundException::new);
        UserDto creator = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        byte[] bytes = file.getBytes();
        return Attachment.builder()
                .creator(creator)
                .issue(issue)
                .uploadDate(dto.getUploadDate())
                .fileName(dto.getFileName())
                .file(bytes)
                .build();
    }

    private AttachmentInfoData toAttachmentInfoData(Attachment attachment) {
        return AttachmentInfoData.builder()
                .id(attachment.getId())
                .creatorId(attachment.getCreator().getId())
                .issueId(attachment.getIssue().getId())
                .uploadDate(attachment.getUploadDate())
                .fileName(attachment.getFileName())
                .build();
    }
}
