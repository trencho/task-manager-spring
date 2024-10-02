package com.project.taskmanager.mapper;

import org.mapstruct.Mapper;

import com.project.taskmanager.dto.UserRegistrationDTO;
import com.project.taskmanager.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRegistrationDTO userRegistrationDTO);

}
