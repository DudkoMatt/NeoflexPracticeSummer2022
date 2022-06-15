package ru.dudkomv.neoflexpractice.coursecategory;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.dudkomv.neoflexpractice.ApplicationIntegrationTest;
import ru.dudkomv.neoflexpractice.coursecategory.dto.CourseCategoryCreationDto;
import ru.dudkomv.neoflexpractice.coursecategory.dto.CourseCategoryUpdateDto;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class CourseCategoryResourceTest extends ApplicationIntegrationTest {
    private static final String URL_PREFIX = "/course/category";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void tearDown() {
        cleanAndMigrate();
    }

    @Test
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void create_valid_admin() throws Exception {
        CourseCategoryCreationDto creationDto = CourseCategoryCreationDto.builder()
                .title("Test title")
                .parentId(0L)
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
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void create_valid_user() throws Exception {
        CourseCategoryCreationDto creationDto = CourseCategoryCreationDto.builder()
                .title("Test title")
                .parentId(0L)
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
    void create_valid_unauthorized() throws Exception {
        CourseCategoryCreationDto creationDto = CourseCategoryCreationDto.builder()
                .title("Test title")
                .parentId(0L)
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
        CourseCategoryCreationDto creationDto = CourseCategoryCreationDto.builder()
                .title("")
                .parentId(null)
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
        CourseCategoryCreationDto creationDto = CourseCategoryCreationDto.builder()
                .title("")
                .parentId(null)
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
        CourseCategoryCreationDto creationDto = CourseCategoryCreationDto.builder()
                .title("")
                .parentId(null)
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
    void getAll_admin() throws Exception {
        mockMvc.perform(get(URL_PREFIX))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void getAll_user() throws Exception {
        mockMvc.perform(get(URL_PREFIX))
                .andExpect(status().isOk());
    }

    @Test
    void getAll_unauthorized() throws Exception {
        mockMvc.perform(get(URL_PREFIX))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void getById_categoryExist_admin() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/0"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void getById_categoryExist_user() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/0"))
                .andExpect(status().isOk());
    }

    @Test
    void getById_categoryExist_unauthorized() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/0"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void getById_categoryNotExist_admin() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void getById_categoryNotExist_user() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getById_categoryNotExist_unauthorized() throws Exception {
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
        CourseCategoryUpdateDto updateDto = CourseCategoryUpdateDto.builder()
                .id(1L)
                .title("Test title")
                .parentId(0L)
                .build();

        String expectedAnswer = objectMapper.writeValueAsString(updateDto);
        mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(expectedAnswer)
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
        CourseCategoryUpdateDto updateDto = CourseCategoryUpdateDto.builder()
                .id(1L)
                .title("Test title")
                .parentId(0L)
                .build();

        String expectedAnswer = objectMapper.writeValueAsString(updateDto);
        mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(expectedAnswer)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void update_valid_unauthorized() throws Exception {
        CourseCategoryUpdateDto updateDto = CourseCategoryUpdateDto.builder()
                .id(1L)
                .title("Test title")
                .parentId(0L)
                .build();

        String expectedAnswer = objectMapper.writeValueAsString(updateDto);
        mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(expectedAnswer)
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
        CourseCategoryUpdateDto updateDto = CourseCategoryUpdateDto.builder()
                .id(1L)
                .title("")
                .parentId(null)
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
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void update_invalid_user() throws Exception {
        CourseCategoryUpdateDto updateDto = CourseCategoryUpdateDto.builder()
                .id(1L)
                .title("")
                .parentId(null)
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
        CourseCategoryUpdateDto updateDto = CourseCategoryUpdateDto.builder()
                .id(1L)
                .title("")
                .parentId(null)
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
        CourseCategoryUpdateDto updateDto = CourseCategoryUpdateDto.builder()
                .id(-1L)
                .title("Title")
                .parentId(0L)
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
        CourseCategoryUpdateDto updateDto = CourseCategoryUpdateDto.builder()
                .id(-1L)
                .title("Title")
                .parentId(0L)
                .build();

        String invalidRequest = objectMapper.writeValueAsString(updateDto);
        mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(invalidRequest)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void update_nonExistent_unauthorized() throws Exception {
        CourseCategoryUpdateDto updateDto = CourseCategoryUpdateDto.builder()
                .id(-1L)
                .title("Title")
                .parentId(0L)
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
        mockMvc.perform(delete(URL_PREFIX + "/5"))
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
        mockMvc.perform(delete(URL_PREFIX + "/5"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            truncate table courses restart identity cascade;
            """)
    void deleteById_valid_unauthorized() throws Exception {
        mockMvc.perform(delete(URL_PREFIX + "/5"))
                .andExpect(status().isUnauthorized());
    }

    @Test
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
    void deleteById_nonExistent_unauthorized() throws Exception {
        mockMvc.perform(delete(URL_PREFIX + "/-1"))
                .andExpect(status().isUnauthorized());
    }
}