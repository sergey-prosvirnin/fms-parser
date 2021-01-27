package com.prosvirnin.fmsparser.service.impl;

import com.prosvirnin.fmsparser.mapper.FmsMapper;
import com.prosvirnin.fmsparser.repository.FmsRepository;
import com.prosvirnin.fmsparser.rest.model.FmsRsDto;
import com.prosvirnin.fmsparser.service.FmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FmsServiceImpl implements FmsService {

    private final FmsRepository fmsRepository;
    private final FmsMapper fmsMapper;

    @Override
    @Transactional
    public Optional<FmsRsDto> findById(Long id) {
        return fmsRepository.findById(id).map(fmsMapper::toFmsRsDto);
    }

    @Override
    @Transactional
    public Page<FmsRsDto> getAll(Pageable pageable) {
        return fmsRepository.findAll(pageable).map(fmsMapper::toFmsRsDto);
    }
}
