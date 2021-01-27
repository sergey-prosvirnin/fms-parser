package com.prosvirnin.fmsparser.rest;

import com.prosvirnin.fmsparser.entity.FmsEntity;
import com.prosvirnin.fmsparser.repository.FmsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ContextConfiguration(initializers = {FmsControllerTest.Initializer.class})
public class FmsControllerTest {

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
    FmsRepository fmsRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        FmsEntity fmsEntityOne = FmsEntity.builder().version(858585L).name("Межрайонная ИФНС России №1 по Республике Адыгея").build();
        FmsEntity fmsEntityTwo = FmsEntity.builder().version(777777L).name("Межрайонная ИФНС России №1 по Московской области").build();

        fmsRepository.deleteAll();
        fmsRepository.save(fmsEntityOne);
        fmsRepository.save(fmsEntityTwo);
    }

    @Test
    public void getAllFmsEntities() throws Exception {
        mockMvc.perform(
                get("/fms"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].version").value(858585))
                .andExpect(jsonPath("$.content[0].name").value("Межрайонная ИФНС России №1 по Республике Адыгея"))
                .andExpect(jsonPath("$.content[1].version").value(777777))
                .andExpect(jsonPath("$.content[1].name").value("Межрайонная ИФНС России №1 по Московской области"));
    }

    @Test
    public void getAllEmptyTest() throws Exception {
        fmsRepository.deleteAll();

        mockMvc.perform(
                get("/fms"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getByIdEntity() throws Exception {
        mockMvc.perform(
                get("/fms/{id}", 5))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.version").value(858585))
                .andExpect(jsonPath("$.name").value("Межрайонная ИФНС России №1 по Республике Адыгея"));
    }

    @Test
    public void getByIdEntity404() throws Exception {
        mockMvc.perform(
                get("/fms/{id}", 999))
                .andExpect(status().isNotFound());
    }

}
