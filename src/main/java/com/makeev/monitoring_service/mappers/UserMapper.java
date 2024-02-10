package com.makeev.monitoring_service.mappers;

import com.makeev.monitoring_service.dto.UserDTO;
import com.makeev.monitoring_service.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

    @Mapping(target = "login", source = "user.login")
    UserDTO toDTO(User user);
}
