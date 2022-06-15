package ru.dudkomv.neoflexpractice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;
import ru.dudkomv.neoflexpractice.advice.ExceptionControllerAdvice.ApiError;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest(properties = "spring.profiles.active:test")
public abstract class ApplicationIntegrationTest {

    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:11.3");

    static {
        Startables.deepStart(Stream.of(postgresContainer)).join();
        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgresContainer.getUsername());
        System.setProperty("spring.datasource.password", postgresContainer.getPassword());
    }

    @Autowired
    private Flyway flyway;

    public void cleanAndMigrate() {
        flyway.clean();
        flyway.migrate();
    }

    public static ApiError toApiError(ObjectMapper objectMapper, MvcResult response) throws Exception {
        return objectMapper.readValue(response.getResponse().getContentAsString(), ApiError.class);
    }

    public static List<String> toSortedListOfErrors(ApiError error) {
        List<String> errors = error.getErrors();
        Collections.sort(errors);
        return errors;
    }
}
