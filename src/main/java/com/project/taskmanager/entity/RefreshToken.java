package com.project.taskmanager.entity;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Document(collection = "refresh_tokens")
@Builder
@Getter
@Setter
public class RefreshToken {

    @Id
    private String id;
    private String token;
    private String username;
    private Instant expiryDate;

    public boolean isExpired() {
        return Instant.now().isAfter(expiryDate);
    }

}
