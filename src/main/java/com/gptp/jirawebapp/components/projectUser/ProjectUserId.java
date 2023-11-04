package com.gptp.jirawebapp.components.projectUser;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProjectUserId implements Serializable {
    private Long project;
    private Long user;
}
