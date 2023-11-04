package com.gptp.jirawebapp.components.project;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectAddUserData {
    Long userId;
    Long roleId;
}
