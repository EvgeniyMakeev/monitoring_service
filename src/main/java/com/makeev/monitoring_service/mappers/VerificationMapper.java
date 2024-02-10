package com.makeev.monitoring_service.mappers;

import com.makeev.monitoring_service.dto.VerificationResponseDTO;
import com.makeev.monitoring_service.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface VerificationMapper {

    @Mapping(source = "login", target = "message")
    VerificationResponseDTO toResponseDTO(User user);
}