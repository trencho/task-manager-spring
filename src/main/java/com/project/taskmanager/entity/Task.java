package com.project.taskmanager.entity;

import com.project.taskmanager.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "tasks")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Task {

    @Id
    private String id;

    private String title;

    private String description;

    private LocalDate dueDate;

    private TaskStatus status;

    private String username;

    public Task(final String title, final String description, final LocalDate dueDate, final TaskStatus status,
                final String username) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
        this.username = username;
    }

}
