package com.makeev.monitoring_service.mappers;

import com.makeev.monitoring_service.dto.UserEventDTO;
import com.makeev.monitoring_service.model.UserEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserEventMapper {
    @Mapping(target = "date", source = "userEvent.date")
    @Mapping(target = "time", source = "userEvent.time")
    @Mapping(target = "message", source = "userEvent.message")
    UserEventDTO toDTO(UserEvent userEvent);
}
