package ru.dudkomv.neoflexpractice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@SpringBootApplication
@EnableJdbcRepositories
@OpenAPIDefinition(
        info = @Info(
                title = "${api.title}",
                version = "${api.version}",
                description = "${api.description}",
                contact = @Contact(
                        name = "${api.contact.name}",
                        email = "${api.contact.email}",
                        url = "${api.contact.url}"
                )
        )
)
public class NeoflexPracticeSummer2022Application {

    public static void main(String[] args) {
        SpringApplication.run(NeoflexPracticeSummer2022Application.class, args);
    }

}
