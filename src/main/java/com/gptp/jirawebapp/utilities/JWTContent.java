package com.gptp.jirawebapp.utilities;

import lombok.Data;

@Data
public class JWTContent {
    String userId;

    public JWTContent(String userId) {
        this.userId = userId;
    }
}
