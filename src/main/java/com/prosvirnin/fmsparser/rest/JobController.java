package com.prosvirnin.fmsparser.rest;

import com.prosvirnin.fmsparser.service.JobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/job")
@RequiredArgsConstructor
@Api(tags = "Особые операции")
public class JobController {

    public final JobService jobService;

    @PostMapping("/downloadFmsZip")
    @ApiOperation("Загрузить данные о подразделениях ФМС с сервера и сохранить в БД")
    public String downloadFmsZip() {
        String fmsZipUrl = "https://regme.online/fms_structure_10012018.zip";
        return "Parse from ZIP archive and save to DB " + jobService.getFmsListFileFromCSV(fmsZipUrl) + " new records";
    }

}