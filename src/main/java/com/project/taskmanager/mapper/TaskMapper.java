package com.project.taskmanager.mapper;

import com.project.taskmanager.dto.TaskDTO;
import com.project.taskmanager.entity.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    Task toEntity(TaskDTO taskDTO);

    TaskDTO toDTO(Task task);

}
