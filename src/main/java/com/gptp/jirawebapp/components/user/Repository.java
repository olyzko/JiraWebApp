package com.gptp.jirawebapp.components.user;
import com.gptp.jirawebapp.data.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Repository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
