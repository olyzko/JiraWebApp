package com.gptp.jirawebapp.components.user;

import com.gptp.jirawebapp.components.projectUser.ProjectUserRepository;
import com.gptp.jirawebapp.utilities.JWT;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repository;
    private final ProjectUserRepository projectUserRepository;
    private final JWT jwt;

    @Autowired
    public UserService(UserRepository repository,
                       ProjectUserRepository projectUserRepository,
                       JWT jwt) {
        this.repository = repository;
        this.projectUserRepository = projectUserRepository;
        this.jwt = jwt;
    }

    public UserDto registerUser(UserDto user) throws Exception {
        // Check if the username already exists
        if (repository.findByEmail(user.getEmail()) != null) {
            throw new Exception("Email already taken");
        }

        // Hash the user's password using BCrypt
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);

        // Save the user to the database
        return repository.save(user);
    }
}
