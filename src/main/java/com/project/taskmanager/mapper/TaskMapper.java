package com.project.taskmanager.mapper;

import org.mapstruct.Mapper;

import com.project.taskmanager.dto.TaskDTO;
import com.project.taskmanager.entity.Task;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    Task toEntity(TaskDTO taskDTO);

    TaskDTO toDTO(Task task);

}
