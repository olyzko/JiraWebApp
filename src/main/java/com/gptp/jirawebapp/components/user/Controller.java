package com.gptp.jirawebapp.components.user;

import com.gptp.jirawebapp.utilities.JWT;
import com.gptp.jirawebapp.utilities.JWTContent;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.gptp.jirawebapp.data.User;

@org.springframework.stereotype.Controller
@RequestMapping("/api/user")
public class Controller {
    private final Repository repository;
    private final JWT jwt;

    public Controller(Repository repository, JWT jwt) {
        this.repository = repository;
        this.jwt = jwt;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        // Check if the username already exists
        if (repository.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        // todo: remove
        user.setFirstName("");
        user.setLastName("");

        // Hash the user's password using BCrypt
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);

        // Save the user to the database
        repository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        User storedUser = repository.findByUsername(user.getUsername());

        if (storedUser == null || !BCrypt.checkpw(user.getPassword(), storedUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        String userId = storedUser.getId().toString();
        JWTContent content = new JWTContent(userId);
        String token = jwt.encode(content);

        return ResponseEntity.ok(token);
    }

    @PostMapping("/testToken")
    public ResponseEntity<String> testToken() {
        SecurityContext context = SecurityContextHolder.getContext();
        JWTContent principal = (JWTContent) context.getAuthentication().getPrincipal();
        return ResponseEntity.ok(principal.getUserId());
    }
}
