package com.makeev.monitoring_service.mappers;

import com.makeev.monitoring_service.dto.UserDTO;
import com.makeev.monitoring_service.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "login", source = "user.login")
    UserDTO toDTO(User user);
}
