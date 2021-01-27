package com.prosvirnin.fmsparser.service;

import com.prosvirnin.fmsparser.rest.model.FmsRsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface FmsService {

    Optional<FmsRsDto> findById(Long id);

    Page<FmsRsDto> getAll(Pageable pageable);

}
