package com.project.taskmanager.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "tasks")
@Getter
@Setter
public class Task {

    @Id
    private String id;
    private String title;
    private String description;
    private boolean completed = false;
    private String userId;

}
