package com.makeev.monitoring_service.mappers;

import com.makeev.monitoring_service.dto.UserEventDTO;
import com.makeev.monitoring_service.model.UserEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserEventMapper {
    UserEventMapper INSTANCE = Mappers.getMapper(UserEventMapper.class);

    @Mapping(target = "date", source = "userEvent.date")
    @Mapping(target = "time", source = "userEvent.time")
    @Mapping(target = "message", source = "userEvent.message")
    UserEventDTO toDTO(UserEvent userEvent);
}
