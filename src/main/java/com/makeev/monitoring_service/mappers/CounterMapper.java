package com.makeev.monitoring_service.mappers;

import com.makeev.monitoring_service.model.Counter;
import com.makeev.monitoring_service.dto.CounterDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CounterMapper {
    CounterMapper INSTANCE = Mappers.getMapper(CounterMapper.class);

    @Mapping(target = "name", source = "counter.name")
    CounterDTO toDTO(Counter counter);
}
