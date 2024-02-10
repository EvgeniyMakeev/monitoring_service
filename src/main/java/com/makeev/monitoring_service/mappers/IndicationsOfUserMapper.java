package com.makeev.monitoring_service.mappers;

import com.makeev.monitoring_service.dto.IndicationsOfUserDTO;
import com.makeev.monitoring_service.model.IndicationsOfUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface IndicationsOfUserMapper {
    @Mapping(target = "login", source = "indicationsOfUser.login")
    @Mapping(target = "counter", source = "indicationsOfUser.counter")
    @Mapping(target = "indication", source = "indicationsOfUser.indication")
    IndicationsOfUserDTO toDTO(IndicationsOfUser indicationsOfUser);
}
