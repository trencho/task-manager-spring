package com.project.taskmanager.model;

import jakarta.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "tasks")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Task {

    @Id
    private String id;

    @NotBlank
    private String title;

    private String description;

    private boolean completed = false;

    private String userId;

    public Task(String title, String description, boolean completed, String userId) {
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.userId = userId;
    }

}
