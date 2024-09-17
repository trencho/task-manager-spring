package com.project.taskmanager.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

    public Task(final String title, final String description, final boolean completed, final String userId) {
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.userId = userId;
    }

}
