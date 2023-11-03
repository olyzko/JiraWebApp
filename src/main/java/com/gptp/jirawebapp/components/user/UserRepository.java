package com.gptp.jirawebapp.components.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserDto, Long> {
    UserDto findByEmail(String email);

    @NonNull
    Optional<UserDto> findById(@NonNull Long id);
}
