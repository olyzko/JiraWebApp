package com.gptp.jirawebapp.components.user;

import com.gptp.jirawebapp.utilities.JWT;
import com.gptp.jirawebapp.utilities.JWTContent;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@org.springframework.stereotype.Controller
@RequestMapping("/api/user")
public class UserController {
    private final UserRepository repository;
    private final JWT jwt;

    public UserController(UserRepository repository, JWT jwt) {
        this.repository = repository;
        this.jwt = jwt;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDto user) {
        // Check if the username already exists
        if (repository.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email already taken");
        }

        // Hash the user's password using BCrypt
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);

        // Save the user to the database
        repository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserDto user) {
        UserDto storedUser = repository.findByEmail(user.getEmail());

        if (storedUser == null || !BCrypt.checkpw(user.getPassword(), storedUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        Long userId = storedUser.getId();
        JWTContent content = new JWTContent(userId);
        String token = jwt.encode(content);

        return ResponseEntity.ok(token);
    }

    @PostMapping("/testToken")
    public ResponseEntity<Long> testToken() {
        JWTContent context = jwt.context();
        return ResponseEntity.ok(context.getUserId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> get(@PathVariable Long id) {
        Optional<UserDto> user = repository.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
