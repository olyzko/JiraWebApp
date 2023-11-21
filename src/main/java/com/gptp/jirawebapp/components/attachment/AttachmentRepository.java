package com.gptp.jirawebapp.components.attachment;

import com.gptp.jirawebapp.data.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    @Query(value = "SELECT a.id, a.creator_id, a.issue_id, a.upload_date, a.file_name " +
            "FROM attachment a WHERE a.id = :id",
            nativeQuery = true)
    List<Object[]> findInfoById(@Param("id") Long id);

    @Query(value = "SELECT a.id, a.creator_id, a.issue_id, a.upload_date, a.file_name " +
            "FROM attachment a WHERE a.issue_id = :id", nativeQuery = true)
    List<Object[]> findInfoListByIssueId(@Param("id") Long id);
}
