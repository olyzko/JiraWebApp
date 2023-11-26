package com.gptp.jirawebapp;

import com.gptp.jirawebapp.components.role.RoleDto;
import com.gptp.jirawebapp.components.role.RoleRepository;
import com.gptp.jirawebapp.components.role.RoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    public void testFindAllReturnsNonEmptyList() {
        RoleDto role1 = new RoleDto(1L, "Role1");
        RoleDto role2 = new RoleDto(2L, "Role2");
        when(roleRepository.findAll()).thenReturn(Arrays.asList(role1, role2));

        List<RoleDto> result = roleService.findAll();

        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        verify(roleRepository).findAll();
    }

    @Test
    public void testFindAll_ReturnsEmptyList() {
        when(roleRepository.findAll()).thenReturn(Collections.emptyList());

        List<RoleDto> result = roleService.findAll();

        assertTrue(result.isEmpty());
        verify(roleRepository).findAll();
    }
}
