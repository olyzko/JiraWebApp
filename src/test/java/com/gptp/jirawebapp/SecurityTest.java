package com.gptp.jirawebapp;

import com.gptp.jirawebapp.components.projectUser.ProjectUserRepository;
import com.gptp.jirawebapp.components.user.UserDto;
import com.gptp.jirawebapp.components.user.UserRepository;
import com.gptp.jirawebapp.components.user.UserService;
import com.gptp.jirawebapp.utilities.JWT;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SecurityTest {
    @Mock
    private UserRepository mockRepository;

    @Mock
    private ProjectUserRepository mockProjectUserRepository;

    @Mock
    private JWT mockJwt;

    @InjectMocks
    private UserService userService;

    private UserDto mockUserDto() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("some@gmai.com");
        userDto.setPassword("password");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setCreatedProjects(new HashSet<>());
        userDto.setIssues(new HashSet<>());
        userDto.setAttachments(new HashSet<>());
        userDto.setComments(new HashSet<>());
        return userDto;
    }

    @Test
    public void testRegisterUser_Success() throws Exception {
        // Setup
        UserDto newUser = mockUserDto();
        when(mockRepository.findByEmail(anyString())).thenReturn(null);
        when(mockRepository.save(any(UserDto.class))).thenReturn(newUser);

        // Execute
        UserDto savedUser = userService.registerUser(newUser);

        // Assert
        assertNotNull(savedUser);
        assertEquals(savedUser.getEmail(), newUser.getEmail());
        assertEquals(savedUser.getFirstName(), newUser.getFirstName());
        assertEquals(savedUser.getLastName(), newUser.getLastName());

        verify(mockRepository).findByEmail(newUser.getEmail());
        verify(mockRepository).save(newUser);
    }

    @Test
    public void testRegisterUser_EmailExists() {
        // Setup
        UserDto existingUser = mockUserDto();
        when(mockRepository.findByEmail(anyString())).thenReturn(existingUser);

        // Execute & Assert
        assertThrows(Exception.class, () -> userService.registerUser(existingUser));
    }
}
