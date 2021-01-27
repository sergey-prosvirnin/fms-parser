package com.prosvirnin.fmsparser.rest;

import com.prosvirnin.fmsparser.rest.model.FmsRsDto;
import com.prosvirnin.fmsparser.service.FmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/fms")
@RequiredArgsConstructor
@Api(tags = "Реестр ФМС")
public class FmsController {

    private final FmsService fmsService;

    @GetMapping("/{id}")
    @ApiOperation("Получить информацию об одном подразделении ФМС")
    public FmsRsDto getById(@PathVariable Long id) {
        return fmsService.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с id = " + id + " не найден."));
    }

    @GetMapping
    @ApiOperation("Получить список всех подразделений ФМС")
    public Page<FmsRsDto> getAll(Pageable pageable) {
        return fmsService.getAll(pageable);
    }

}
