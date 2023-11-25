package com.gptp.jirawebapp.components.project;

import com.gptp.jirawebapp.components.role.RoleDto;
import com.gptp.jirawebapp.components.user.UserDto;

public class UserWithRole extends UserDto {
    public RoleDto role;

    public UserWithRole(UserDto user, RoleDto role) {
        setId(user.getId());
        setEmail(user.getEmail());
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
        this.role = role;
    }
}
