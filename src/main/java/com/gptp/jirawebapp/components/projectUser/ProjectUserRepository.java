package com.gptp.jirawebapp.components.projectUser;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectUserRepository extends JpaRepository<ProjectUserDto, ProjectUserId> {
    List<ProjectUserDto> findByProjectId(Long projectId);
    List<ProjectUserDto> findByUserId(Long userId);
    List<ProjectUserDto> findByProjectIdAndUserId(Long projectId, Long userId);
}