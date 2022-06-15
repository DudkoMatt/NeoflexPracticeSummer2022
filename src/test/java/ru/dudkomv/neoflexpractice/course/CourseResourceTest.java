package ru.dudkomv.neoflexpractice.course;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.dudkomv.neoflexpractice.ApplicationIntegrationTest;
import ru.dudkomv.neoflexpractice.course.dto.CourseCreationDto;
import ru.dudkomv.neoflexpractice.course.dto.CourseUpdateDto;
import ru.dudkomv.neoflexpractice.course.type.BaseCourseType;
import ru.dudkomv.neoflexpractice.course.type.OfflineCourseType;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class CourseResourceTest extends ApplicationIntegrationTest {
    private static final String URL_PREFIX = "/course";

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
            """)
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void create_valid_admin() throws Exception {
        // Case 1:
        // Explicitly for yourself
        CourseCreationDto creationDto = CourseCreationDto.builder()
                .title("Title")
                .description("Description")
                .categoryId(0L)
                .curatorId(0L)
                .studentCount(0L)
                .type(new BaseCourseType())
                .build();

        String request = objectMapper.writeValueAsString(creationDto);
        mockMvc.perform(
                        post(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isOk());

        // Case 2:
        // Implicitly for yourself
        creationDto.setCuratorId(null);
        request = objectMapper.writeValueAsString(creationDto);
        mockMvc.perform(
                        post(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isOk());

        // Case 3:
        // For another user
        creationDto.setCuratorId(1L);
        request = objectMapper.writeValueAsString(creationDto);
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
            """)
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void create_valid_user() throws Exception {
        // Case 1:
        // Explicitly for yourself
        CourseCreationDto creationDto = CourseCreationDto.builder()
                .title("Title")
                .description("Description")
                .categoryId(0L)
                .curatorId(1L)
                .studentCount(0L)
                .type(new BaseCourseType())
                .build();

        String request = objectMapper.writeValueAsString(creationDto);
        mockMvc.perform(
                        post(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isOk());

        // Case 2:
        // Implicitly for yourself
        creationDto.setCuratorId(null);
        request = objectMapper.writeValueAsString(creationDto);
        mockMvc.perform(
                        post(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isOk());

        // Case 3:
        // For another user
        creationDto.setCuratorId(0L);
        request = objectMapper.writeValueAsString(creationDto);
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
            """)
    void create_valid_unauthorized() throws Exception {
        // Case 1:
        // Explicitly for some user
        CourseCreationDto creationDto = CourseCreationDto.builder()
                .title("Title")
                .description("Description")
                .categoryId(0L)
                .curatorId(1L)
                .studentCount(0L)
                .type(new BaseCourseType())
                .build();

        String request = objectMapper.writeValueAsString(creationDto);
        mockMvc.perform(
                        post(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isUnauthorized());

        // Case 2:
        // Implicitly for yourself
        creationDto.setCuratorId(null);
        request = objectMapper.writeValueAsString(creationDto);
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
        CourseCreationDto creationDto = CourseCreationDto.builder()
                .title("")
                .description("")
                .categoryId(null)
                .curatorId(null)
                .studentCount(-1L)
                .type(null)
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
        CourseCreationDto creationDto = CourseCreationDto.builder()
                .title("")
                .description("")
                .categoryId(null)
                .curatorId(null)
                .studentCount(-1L)
                .type(null)
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
        CourseCreationDto creationDto = CourseCreationDto.builder()
                .title("")
                .description("")
                .categoryId(null)
                .curatorId(null)
                .studentCount(-1L)
                .type(null)
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
    void getAllForUser_admin() throws Exception {
        mockMvc.perform(get(URL_PREFIX))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void getAllForUser_user() throws Exception {
        mockMvc.perform(get(URL_PREFIX))
                .andExpect(status().isOk());
    }

    @Test
    void getAllForUser_unauthorized() throws Exception {
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
    void getById_courseExist_admin() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void getById_courseExist_user() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getById_courseExist_unauthorized() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void getById_courseNotExist_admin() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void getById_courseNotExist_user() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getById_courseNotExist_unauthorized() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/-1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void getLessonsById_courseExist_admin() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/5/lesson"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void getLessonsById_courseExist_user() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/5/lesson"))
                .andExpect(status().isOk());
    }

    @Test
    void getLessonsById_courseExist_unauthorized() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/5/lesson"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void getLessonsById_courseNotExist_admin() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/-1/lesson"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void getLessonsById_courseNotExist_user() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/-1/lesson"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getLessonsById_courseNotExist_unauthorized() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/-1/lesson"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void update_valid_admin() throws Exception {
        // Case 1:
        // Explicitly for yourself
        CourseUpdateDto updateDto = CourseUpdateDto.builder()
                .id(1L)
                .title("New Title")
                .description("New Description")
                .categoryId(0L)
                .curatorId(0L)
                .studentCount(0L)
                .type(new BaseCourseType())
                .build();

        String expectedAnswer = objectMapper.writeValueAsString(updateDto);
        mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(expectedAnswer)
                )
                .andExpect(status().isOk());

        // Case 2:
        // Implicitly for yourself
        updateDto.setCuratorId(null);
        expectedAnswer = objectMapper.writeValueAsString(updateDto);
        mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(expectedAnswer)
                )
                .andExpect(status().isOk());

        // Case 3:
        // For another user
        updateDto.setCuratorId(1L);
        expectedAnswer = objectMapper.writeValueAsString(updateDto);
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
        // Case 1:
        // Explicitly for yourself
        CourseUpdateDto updateDto = CourseUpdateDto.builder()
                .id(1L)
                .title("New Title")
                .description("New Description")
                .categoryId(0L)
                .curatorId(1L)
                .studentCount(0L)
                .type(new BaseCourseType())
                .build();

        String expectedAnswer = objectMapper.writeValueAsString(updateDto);
        mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(expectedAnswer)
                )
                .andExpect(status().isOk());

        // Case 2:
        // Implicitly for yourself
        updateDto.setCuratorId(null);
        expectedAnswer = objectMapper.writeValueAsString(updateDto);
        mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(expectedAnswer)
                )
                .andExpect(status().isOk());

        // Case 3:
        // For another user
        updateDto.setCuratorId(0L);
        expectedAnswer = objectMapper.writeValueAsString(updateDto);
        mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(expectedAnswer)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void update_valid_unauthorized() throws Exception {
        // Case 1:
        // Explicitly for some user
        CourseUpdateDto updateDto = CourseUpdateDto.builder()
                .id(1L)
                .title("New Title")
                .description("New Description")
                .categoryId(0L)
                .curatorId(0L)
                .studentCount(0L)
                .type(new BaseCourseType())
                .build();

        String expectedAnswer = objectMapper.writeValueAsString(updateDto);
        mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(expectedAnswer)
                )
                .andExpect(status().isUnauthorized());

        // Case 2:
        // Implicitly for yourself
        updateDto.setCuratorId(null);
        expectedAnswer = objectMapper.writeValueAsString(updateDto);
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
        CourseUpdateDto updateDto = CourseUpdateDto.builder()
                .id(1L)
                .title("")
                .description("")
                .categoryId(null)
                .curatorId(null)
                .studentCount(-1L)
                .type(null)
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
        CourseUpdateDto updateDto = CourseUpdateDto.builder()
                .id(1L)
                .title("")
                .description("")
                .categoryId(null)
                .curatorId(null)
                .studentCount(-1L)
                .type(null)
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
        CourseUpdateDto updateDto = CourseUpdateDto.builder()
                .id(1L)
                .title("")
                .description("")
                .categoryId(null)
                .curatorId(null)
                .studentCount(-1L)
                .type(null)
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
        // Case 1:
        // Set curator as another teacher
        CourseUpdateDto updateDto = CourseUpdateDto.builder()
                .id(-1L)
                .title("New Title")
                .description("New Description")
                .categoryId(0L)
                .curatorId(1L)
                .studentCount(0L)
                .type(new BaseCourseType())
                .build();

        String invalidRequest = objectMapper.writeValueAsString(updateDto);
        mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(invalidRequest)
                )
                .andExpect(status().isNotFound());

        // Case 2:
        // Curator is yourself
        updateDto.setCuratorId(0L);
        invalidRequest = objectMapper.writeValueAsString(updateDto);
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
        // Case 1:
        // Set curator as another teacher
        CourseUpdateDto updateDto = CourseUpdateDto.builder()
                .id(-1L)
                .title("New Title")
                .description("New Description")
                .categoryId(0L)
                .curatorId(0L)
                .studentCount(0L)
                .type(new BaseCourseType())
                .build();

        String invalidRequest = objectMapper.writeValueAsString(updateDto);
        mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(invalidRequest)
                )
                .andExpect(status().isForbidden());

        // Case 2:
        // Curator is yourself
        updateDto.setCuratorId(1L);
        invalidRequest = objectMapper.writeValueAsString(updateDto);
        mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(invalidRequest)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void update_nonExistent_unauthorized() throws Exception {
        CourseUpdateDto updateDto = CourseUpdateDto.builder()
                .id(-1L)
                .title("New Title")
                .description("New Description")
                .categoryId(0L)
                .curatorId(0L)
                .studentCount(0L)
                .type(new BaseCourseType())
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
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Test Course', 'Test Course', 0, 0, 0, '{"type": "base"}');
            """)
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void deleteById_valid_admin() throws Exception {
        mockMvc.perform(delete(URL_PREFIX + "/1"))
                .andExpect(status().isOk());

        mockMvc.perform(delete(URL_PREFIX + "/0"))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(statements = """
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Test Course', 'Test Course', 0, 0, 0, '{"type": "base"}');
            """)
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void deleteById_valid_user() throws Exception {
        mockMvc.perform(delete(URL_PREFIX + "/1"))
                .andExpect(status().isOk());

        mockMvc.perform(delete(URL_PREFIX + "/0"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql(statements = """
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Test Course', 'Test Course', 0, 0, 0, '{"type": "base"}');
            """)
    void deleteById_valid_unauthorized() throws Exception {
        mockMvc.perform(delete(URL_PREFIX + "/1"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(delete(URL_PREFIX + "/0"))
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
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteById_nonExistent_unauthorized() throws Exception {
        mockMvc.perform(delete(URL_PREFIX + "/-1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void getCoursesByCategoryIdRecursively_valid_admin() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/by-category-id-recursively/0"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void getCoursesByCategoryIdRecursively_valid_user() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/by-category-id-recursively/0"))
                .andExpect(status().isOk());
    }

    @Test
    void getCoursesByCategoryIdRecursively_valid_unauthorized() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/by-category-id-recursively/0"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void getCoursesByCategoryIdRecursively_nonExistent_admin() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/by-category-id-recursively/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void getCoursesByCategoryIdRecursively_nonExistent_user() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/by-category-id-recursively/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCoursesByCategoryIdRecursively_nonExistent_unauthorized() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/by-category-id-recursively/-1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void getCoursesByType_valid_admin() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/by-type-name-recursively/" + OfflineCourseType.CLASS_TYPE_NAME))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void getCoursesByType_valid_user() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/by-type-name-recursively/" + OfflineCourseType.CLASS_TYPE_NAME))
                .andExpect(status().isOk());
    }

    @Test
    void getCoursesByType_valid_unauthorized() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/by-type-name-recursively/" + OfflineCourseType.CLASS_TYPE_NAME))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void getCoursesByType_nonExistent_admin() throws Exception {
        String courseType = "nonExistentType";

        mockMvc.perform(get(URL_PREFIX + "/by-type-name-recursively/" + courseType))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void getCoursesByType_nonExistent_user() throws Exception {
        String courseType = "nonExistentType";

        mockMvc.perform(get(URL_PREFIX + "/by-type-name-recursively/" + courseType))
                .andExpect(status().isOk());
    }

    @Test
    void getCoursesByType_nonExistent_unauthorized() throws Exception {
        String courseType = "nonExistentType";

        mockMvc.perform(get(URL_PREFIX + "/by-type-name-recursively/" + courseType))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void getCoursesByCuratorId_valid_admin() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/by-curator-id/0"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void getCoursesByCuratorId_valid_user() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/by-curator-id/0"))
                .andExpect(status().isOk());
    }

    @Test
    void getCoursesByCuratorId_valid_unauthorized() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/by-curator-id/0"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void getCoursesByCuratorId_nonExistent_admin() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/by-curator-id/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void getCoursesByCuratorId_nonExistent_user() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/by-curator-id/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCoursesByCuratorId_nonExistent_unauthorized() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/by-curator-id/-1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void cloneCourse_valid_admin() throws Exception {
        mockMvc.perform(post(URL_PREFIX + "/clone/5"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void cloneCourse_valid_user() throws Exception {
        mockMvc.perform(post(URL_PREFIX + "/clone/5"))
                .andExpect(status().isOk());
    }

    @Test
    void cloneCourse_valid_unauthorized() throws Exception {
        mockMvc.perform(post(URL_PREFIX + "/clone/5"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void cloneCourse_nonExistent_admin() throws Exception {
        mockMvc.perform(post(URL_PREFIX + "/clone/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void cloneCourse_nonExistent_user() throws Exception {
        mockMvc.perform(post(URL_PREFIX + "/clone/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void cloneCourse_nonExistent_unauthorized() throws Exception {
        mockMvc.perform(post(URL_PREFIX + "/clone/-1"))
                .andExpect(status().isUnauthorized());
    }
}
