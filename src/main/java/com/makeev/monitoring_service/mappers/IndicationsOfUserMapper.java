package com.makeev.monitoring_service.mappers;

import com.makeev.monitoring_service.dto.IndicationsOfUserDTO;
import com.makeev.monitoring_service.model.IndicationsOfUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IndicationsOfUserMapper {
    IndicationsOfUserMapper INSTANCE = Mappers.getMapper(IndicationsOfUserMapper.class);

    @Mapping(target = "login", source = "indicationsOfUser.login")
    @Mapping(target = "nameOfCounter", source = "indicationsOfUser.counter.name")
    @Mapping(target = "year", source = "indicationsOfUser.indication.date.year")
    @Mapping(target = "month", source = "indicationsOfUser.indication.date.month")
    @Mapping(target = "value", source = "indicationsOfUser.indication.value")
    IndicationsOfUserDTO toDTO(IndicationsOfUser indicationsOfUser);
}
