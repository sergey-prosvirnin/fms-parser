package com.prosvirnin.fmsparser.service.impl;

import com.prosvirnin.fmsparser.service.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = {JosServiceImplTest.Initializer.class})
public class JosServiceImplTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("fms_db")
            .withUsername("fms_user")
            .withPassword("fms_password");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Autowired
    JobService jobService;

    @BeforeEach
    public void deleteOldFilesIfExist() {
        try {
            Files.deleteIfExists(Paths.get("test.zip"));
            FileUtils.deleteDirectory(new File("fms/"));
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    @Test
    public void getFmsListFileFromCSV() {
        String fmsZipUrl = "https://regme.online/fms_structure_10012018.zip";
        jobService.getFmsListFileFromCSV(fmsZipUrl);

        String md5 = null;

        try (InputStream is = Files.newInputStream(Paths.get("fms/unzipTest/fms_structure_10012018_by_utf-8.txt"))) {
            md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(is);

        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals("839036861d3c7b4b5ea342420d063aef", md5);
    }

}
