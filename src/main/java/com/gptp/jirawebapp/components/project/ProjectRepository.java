package com.gptp.jirawebapp.components.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectDto, Long> {
    @NonNull
    Optional<ProjectDto> findById(@NonNull Long id);
}
