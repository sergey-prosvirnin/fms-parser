package com.prosvirnin.fmsparser.mapper;

import com.prosvirnin.fmsparser.entity.FmsEntity;
import com.prosvirnin.fmsparser.rest.model.FmsRsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FmsMapper {

    public FmsRsDto toFmsRsDto(FmsEntity fmsEntity) {
        return FmsRsDto.builder()
                .name(fmsEntity.getName())
                .version(fmsEntity.getVersion())
                .build();
    }

}
