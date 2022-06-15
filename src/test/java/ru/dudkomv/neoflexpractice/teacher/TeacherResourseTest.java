package ru.dudkomv.neoflexpractice.teacher;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.dudkomv.neoflexpractice.ApplicationIntegrationTest;
import ru.dudkomv.neoflexpractice.teacher.dto.TeacherCreationDto;
import ru.dudkomv.neoflexpractice.teacher.dto.TeacherUpdateDto;

import java.time.LocalDate;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class TeacherResourseTest extends ApplicationIntegrationTest {
    private static final String URL_PREFIX = "/teacher";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void tearDown() {
        cleanAndMigrate();
    }

    @Test
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            truncate table courses restart identity cascade;
            truncate table teachers restart identity cascade;
            """)
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void create_valid_admin() throws Exception {
        TeacherCreationDto creationDto = TeacherCreationDto.builder()
                .username("username")
                .password("password")
                .firstName("Name")
                .lastName("Surname")
                .secondName("Second Name")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        String request = objectMapper.writeValueAsString(creationDto);
        mockMvc.perform(
                        post(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isOk());
    }

    @Test
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            truncate table courses restart identity cascade;
            truncate table teachers restart identity cascade;
            """)
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void create_valid_user() throws Exception {
        TeacherCreationDto creationDto = TeacherCreationDto.builder()
                .username("username")
                .password("password")
                .firstName("Name")
                .lastName("Surname")
                .secondName("Second Name")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        String request = objectMapper.writeValueAsString(creationDto);
        mockMvc.perform(
                        post(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            truncate table courses restart identity cascade;
            truncate table teachers restart identity cascade;
            """)
    void create_valid_unauthorized() throws Exception {
        TeacherCreationDto creationDto = TeacherCreationDto.builder()
                .username("username")
                .password("password")
                .firstName("Name")
                .lastName("Surname")
                .secondName("Second Name")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        String request = objectMapper.writeValueAsString(creationDto);
        mockMvc.perform(
                        post(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void create_invalid_admin() throws Exception {
        TeacherCreationDto creationDto = TeacherCreationDto.builder()
                .username("")
                .password("")
                .firstName("")
                .lastName("")
                .secondName(null)
                .birthday(LocalDate.MAX)
                .build();

        String invalidRequest = objectMapper.writeValueAsString(creationDto);
        mockMvc.perform(
                        post(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(invalidRequest)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void create_invalid_user() throws Exception {
        TeacherCreationDto creationDto = TeacherCreationDto.builder()
                .username("")
                .password("")
                .firstName("")
                .lastName("")
                .secondName(null)
                .birthday(LocalDate.MAX)
                .build();

        String invalidRequest = objectMapper.writeValueAsString(creationDto);
        mockMvc.perform(
                        post(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(invalidRequest)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_invalid_unauthorized() throws Exception {
        TeacherCreationDto creationDto = TeacherCreationDto.builder()
                .username("")
                .password("")
                .firstName("")
                .lastName("")
                .secondName(null)
                .birthday(LocalDate.MAX)
                .build();

        String invalidRequest = objectMapper.writeValueAsString(creationDto);
        mockMvc.perform(
                        post(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(invalidRequest)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void getForUser_admin() throws Exception {
        mockMvc.perform(get(URL_PREFIX))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void getForUser_user() throws Exception {
        mockMvc.perform(get(URL_PREFIX))
                .andExpect(status().isOk());
    }

    @Test
    void getForUser_unauthorized() throws Exception {
        mockMvc.perform(get(URL_PREFIX))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void getAll_admin() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/all"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void getAll_user() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/all"))
                .andExpect(status().isOk());
    }

    @Test
    void getAll_unauthorized() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/all"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void getById_teacherExist_admin() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void getById_teacherExist_user() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getById_teacherExist_unauthorized() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void getById_teacherNotExist_admin() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void getById_teacherNotExist_user() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getById_teacherNotExist_unauthorized() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/-1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void update_valid_admin() throws Exception {
        TeacherUpdateDto updateDto = TeacherUpdateDto.builder()
                .id(1L)
                .username("username")
                .password("password")
                .firstName("Name")
                .lastName("Surname")
                .secondName("Second Name")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        String request = objectMapper.writeValueAsString(updateDto);
        mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void update_valid_user() throws Exception {
        TeacherUpdateDto updateDto = TeacherUpdateDto.builder()
                .id(1L)
                .username("username")
                .password("password")
                .firstName("Name")
                .lastName("Surname")
                .secondName("Second Name")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        String request = objectMapper.writeValueAsString(updateDto);
        mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isOk());
    }

    @Test
    void update_valid_unauthorized() throws Exception {
        TeacherUpdateDto updateDto = TeacherUpdateDto.builder()
                .id(1L)
                .username("username")
                .password("password")
                .firstName("Name")
                .lastName("Surname")
                .secondName("Second Name")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        String request = objectMapper.writeValueAsString(updateDto);
        mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void update_invalid_admin() throws Exception {
        TeacherUpdateDto updateDto = TeacherUpdateDto.builder()
                .id(1L)
                .username("")
                .password("")
                .firstName("")
                .lastName("")
                .secondName(null)
                .birthday(LocalDate.MAX)
                .build();

        String invalidRequest = objectMapper.writeValueAsString(updateDto);
        mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(invalidRequest)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void update_invalid_user() throws Exception {
        TeacherUpdateDto updateDto = TeacherUpdateDto.builder()
                .id(1L)
                .username("")
                .password("")
                .firstName("")
                .lastName("")
                .secondName(null)
                .birthday(LocalDate.MAX)
                .build();

        String invalidRequest = objectMapper.writeValueAsString(updateDto);
        mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(invalidRequest)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_invalid_unauthorized() throws Exception {
        TeacherUpdateDto updateDto = TeacherUpdateDto.builder()
                .id(1L)
                .username("")
                .password("")
                .firstName("")
                .lastName("")
                .secondName(null)
                .birthday(LocalDate.MAX)
                .build();

        String invalidRequest = objectMapper.writeValueAsString(updateDto);
        mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(invalidRequest)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void update_nonExistent_admin() throws Exception {
        TeacherUpdateDto updateDto = TeacherUpdateDto.builder()
                .id(-1L)
                .username("username")
                .password("password")
                .firstName("Name")
                .lastName("Surname")
                .secondName("Second Name")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        String invalidRequest = objectMapper.writeValueAsString(updateDto);
        mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(invalidRequest)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void update_nonExistent_user() throws Exception {
        TeacherUpdateDto updateDto = TeacherUpdateDto.builder()
                .id(-1L)
                .username("username")
                .password("password")
                .firstName("Name")
                .lastName("Surname")
                .secondName("Second Name")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        String invalidRequest = objectMapper.writeValueAsString(updateDto);
        mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(invalidRequest)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void update_nonExistent_unauthorized() throws Exception {
        TeacherUpdateDto updateDto = TeacherUpdateDto.builder()
                .id(-1L)
                .username("username")
                .password("password")
                .firstName("Name")
                .lastName("Surname")
                .secondName("Second Name")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        String invalidRequest = objectMapper.writeValueAsString(updateDto);
        mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(invalidRequest)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            truncate table courses restart identity cascade;
            """)
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void deleteById_valid_admin() throws Exception {
        mockMvc.perform(delete(URL_PREFIX + "/1"))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            truncate table courses restart identity cascade;
            """)
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void deleteById_valid_user() throws Exception {
        mockMvc.perform(delete(URL_PREFIX + "/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            truncate table courses restart identity cascade;
            """)
    void deleteById_valid_unauthorized() throws Exception {
        mockMvc.perform(delete(URL_PREFIX + "/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            truncate table courses restart identity cascade;
            """)
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void deleteById_nonExistent_admin() throws Exception {
        mockMvc.perform(delete(URL_PREFIX + "/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            truncate table courses restart identity cascade;
            """)
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void deleteById_nonExistent_user() throws Exception {
        mockMvc.perform(delete(URL_PREFIX + "/-1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            truncate table courses restart identity cascade;
            """)
    void deleteById_nonExistent_unauthorized() throws Exception {
        mockMvc.perform(delete(URL_PREFIX + "/-1"))
                .andExpect(status().isUnauthorized());
    }
}