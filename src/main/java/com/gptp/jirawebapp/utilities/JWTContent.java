package com.gptp.jirawebapp.utilities;

import lombok.Data;

@Data
public class JWTContent {
    Long userId;

    public JWTContent(Long userId) {
        this.userId = userId;
    }
}
