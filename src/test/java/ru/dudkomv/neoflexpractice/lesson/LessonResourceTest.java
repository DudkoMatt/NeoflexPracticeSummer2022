package ru.dudkomv.neoflexpractice.lesson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.dudkomv.neoflexpractice.ApplicationIntegrationTest;
import ru.dudkomv.neoflexpractice.lesson.dto.LessonCreationDto;
import ru.dudkomv.neoflexpractice.lesson.dto.LessonUpdateDto;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class LessonResourceTest extends ApplicationIntegrationTest {
    private static final String URL_PREFIX = "/lesson";

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
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Test', 'Test', 0, 0, 0, '{"type": "base"}');
            """)
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void create_valid_explicitTeacherId_courseIdMatch_admin() throws Exception {
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .title("Title")
                .description("Description")
                .teacherId(0L)
                .courseId(0L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
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
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Test', 'Test', 0, 0, 0, '{"type": "base"}');
            """)
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void create_valid_explicitTeacherId_courseIdMatch_user() throws Exception {
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .title("Title")
                .description("Description")
                .teacherId(1L)
                .courseId(1L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
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
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Test', 'Test', 0, 0, 0, '{"type": "base"}');
            """)
    void create_valid_explicitTeacherId_courseIdMatch_unauthorized() throws Exception {
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .title("Title")
                .description("Description")
                .teacherId(0L)
                .courseId(0L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
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
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Test', 'Test', 0, 0, 0, '{"type": "base"}');
            """)
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void create_valid_explicitTeacherId_courseIdNotMatch_admin() throws Exception {
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .title("Title")
                .description("Description")
                .teacherId(0L)
                .courseId(1L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
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
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Test', 'Test', 0, 0, 0, '{"type": "base"}');
            """)
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void create_valid_explicitTeacherId_courseIdNotMatch_user() throws Exception {
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .title("Title")
                .description("Description")
                .teacherId(1L)
                .courseId(0L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
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
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Test', 'Test', 0, 0, 0, '{"type": "base"}');
            """)
    void create_valid_explicitTeacherId_courseIdNotMatch_unauthorized() throws Exception {
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .title("Title")
                .description("Description")
                .teacherId(0L)
                .courseId(1L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
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
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Test', 'Test', 0, 0, 0, '{"type": "base"}');
            """)
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void create_valid_implicitTeacherId_courseIdMatch_admin() throws Exception {
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .title("Title")
                .description("Description")
                .teacherId(null)
                .courseId(0L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
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
            """)
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void create_valid_implicitTeacherId_courseIdMatch_user() throws Exception {
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .title("Title")
                .description("Description")
                .teacherId(null)
                .courseId(1L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
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
            """)
    void create_valid_implicitTeacherId_courseIdMatch_unauthorized() throws Exception {
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .title("Title")
                .description("Description")
                .teacherId(null)
                .courseId(1L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
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
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Test', 'Test', 0, 0, 0, '{"type": "base"}');
            """)
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void create_valid_implicitTeacherId_courseIdNotMatch_admin() throws Exception {
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .title("Title")
                .description("Description")
                .teacherId(null)
                .courseId(1L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
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
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Test', 'Test', 0, 0, 0, '{"type": "base"}');
            """)
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void create_valid_implicitTeacherId_courseIdNotMatch_user() throws Exception {
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .title("Title")
                .description("Description")
                .teacherId(null)
                .courseId(0L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
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
            """)
    void create_valid_implicitTeacherId_courseIdNotMatch_unauthorized() throws Exception {
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .title("Title")
                .description("Description")
                .teacherId(null)
                .courseId(1L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
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
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Test', 'Test', 0, 0, 0, '{"type": "base"}');
            """)
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void create_valid_anotherTeacherId_courseIdMatchCurrentUser_admin() throws Exception {
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .title("Title")
                .description("Description")
                .teacherId(1L)
                .courseId(0L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
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
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Test', 'Test', 0, 0, 0, '{"type": "base"}');
            """)
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void create_valid_anotherTeacherId_courseIdMatchCurrentUser_user() throws Exception {
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .title("Title")
                .description("Description")
                .teacherId(0L)
                .courseId(1L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
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
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Test', 'Test', 0, 0, 0, '{"type": "base"}');
            """)
    void create_valid_anotherTeacherId_courseIdMatchCurrentUser_unauthorized() throws Exception {
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .title("Title")
                .description("Description")
                .teacherId(0L)
                .courseId(0L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
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
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Test', 'Test', 0, 0, 0, '{"type": "base"}');
            """)
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void create_valid_anotherTeacherId_courseIdNotMatchCurrentUser_admin() throws Exception {
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .title("Title")
                .description("Description")
                .teacherId(1L)
                .courseId(2L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
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
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Test', 'Test', 0, 0, 0, '{"type": "base"}');
            """)
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void create_valid_anotherTeacherId_courseIdNotMatchCurrentUser_user() throws Exception {
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .title("Title")
                .description("Description")
                .teacherId(0L)
                .courseId(0L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
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
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Test', 'Test', 0, 0, 0, '{"type": "base"}');
            """)
    void create_valid_anotherTeacherId_courseIdNotMatchCurrentUser_unauthorized() throws Exception {
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .title("Title")
                .description("Description")
                .teacherId(0L)
                .courseId(0L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
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
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            insert into lessons(id, teacher_id, course_id, title, description, start_datetime, end_datetime)
                values (0, 0, 5, 'Admin lesson', 'Admin lesson description', '2022-05-19T10:00:00Z', '2022-05-19T11:00:00Z');
            """)
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void create_valid_teacherTimeInterception_admin() throws Exception {
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .title("Title")
                .description("Description")
                .teacherId(0L)
                .courseId(1L)
                .startDateTime(OffsetDateTime.of(2022, 5, 19, 10, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 19, 11, 0, 0, 0, ZoneOffset.UTC))
                .build();

        String request = objectMapper.writeValueAsString(creationDto);
        mockMvc.perform(
                        post(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            insert into lessons(id, teacher_id, course_id, title, description, start_datetime, end_datetime)
                values (0, 0, 5, 'Admin lesson', 'Admin lesson description', '2022-05-19T10:00:00Z', '2022-05-19T11:00:00Z');
            """)
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void create_valid_teacherTimeInterception_user() throws Exception {
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .title("Title")
                .description("Description")
                .teacherId(0L)
                .courseId(1L)
                .startDateTime(OffsetDateTime.of(2022, 5, 19, 10, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 19, 11, 0, 0, 0, ZoneOffset.UTC))
                .build();

        String request = objectMapper.writeValueAsString(creationDto);
        mockMvc.perform(
                        post(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            insert into lessons(id, teacher_id, course_id, title, description, start_datetime, end_datetime)
                values (0, 0, 5, 'Admin lesson', 'Admin lesson description', '2022-05-19T10:00:00Z', '2022-05-19T11:00:00Z');
            """)
    void create_valid_teacherTimeInterception_unauthorized() throws Exception {
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .title("Title")
                .description("Description")
                .teacherId(0L)
                .courseId(1L)
                .startDateTime(OffsetDateTime.of(2022, 5, 19, 10, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 19, 11, 0, 0, 0, ZoneOffset.UTC))
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
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            insert into lessons(id, teacher_id, course_id, title, description, start_datetime, end_datetime)
                values (0, 0, 5, 'Admin lesson', 'Admin lesson description', '2022-05-19T10:00:00Z', '2022-05-19T11:00:00Z');
            """)
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void create_valid_courseTimeInterception_admin() throws Exception {
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .title("Title")
                .description("Description")
                .teacherId(1L)
                .courseId(5L)
                .startDateTime(OffsetDateTime.of(2022, 5, 19, 10, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 19, 11, 0, 0, 0, ZoneOffset.UTC))
                .build();

        String request = objectMapper.writeValueAsString(creationDto);
        mockMvc.perform(
                        post(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            insert into lessons(id, teacher_id, course_id, title, description, start_datetime, end_datetime)
                values (0, 0, 5, 'Admin lesson', 'Admin lesson description', '2022-05-19T10:00:00Z', '2022-05-19T11:00:00Z');
            """)
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void create_valid_courseTimeInterception_user() throws Exception {
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .title("Title")
                .description("Description")
                .teacherId(1L)
                .courseId(5L)
                .startDateTime(OffsetDateTime.of(2022, 5, 19, 10, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 19, 11, 0, 0, 0, ZoneOffset.UTC))
                .build();

        String request = objectMapper.writeValueAsString(creationDto);
        mockMvc.perform(
                        post(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            insert into lessons(id, teacher_id, course_id, title, description, start_datetime, end_datetime)
                values (0, 0, 5, 'Admin lesson', 'Admin lesson description', '2022-05-19T10:00:00Z', '2022-05-19T11:00:00Z');
            """)
    void create_valid_courseTimeInterception_unauthorized() throws Exception {
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .title("Title")
                .description("Description")
                .teacherId(1L)
                .courseId(5L)
                .startDateTime(OffsetDateTime.of(2022, 5, 19, 10, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 19, 11, 0, 0, 0, ZoneOffset.UTC))
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
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .title("")
                .description("")
                .teacherId(-1L)
                .courseId(null)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 14, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 10, 0, 0, 0, ZoneOffset.UTC))
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
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .title("")
                .description("")
                .teacherId(-1L)
                .courseId(null)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 14, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 10, 0, 0, 0, ZoneOffset.UTC))
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
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .title("")
                .description("")
                .teacherId(-1L)
                .courseId(null)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 14, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 10, 0, 0, 0, ZoneOffset.UTC))
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
    void getAllForUserByUserId_userExist_admin() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/user/0"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void getAllForUserByUserId_userExist_user() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/user/0"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllForUserByUserId_userExist_unauthorized() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/user/0"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void getAllForUserByUserId_userNotExist_admin() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/user/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void getAllForUserByUserId_userNotExist_user() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/user/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllForUserByUserId_userNotExist_unauthorized() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/user/-1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            insert into lessons(id, teacher_id, course_id, title, description, start_datetime, end_datetime)
                values (0, 0, 5, 'Admin lesson', 'Admin lesson description', '2022-05-19T10:00:00Z', '2022-05-19T11:00:00Z');
            """)
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
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            insert into lessons(id, teacher_id, course_id, title, description, start_datetime, end_datetime)
                values (0, 0, 5, 'Admin lesson', 'Admin lesson description', '2022-05-19T10:00:00Z', '2022-05-19T11:00:00Z');
            """)
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
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            insert into lessons(id, teacher_id, course_id, title, description, start_datetime, end_datetime)
                values (0, 0, 5, 'Admin lesson', 'Admin lesson description', '2022-05-19T10:00:00Z', '2022-05-19T11:00:00Z');
            """)
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
    void getById_lessonExist_admin() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/0"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void getById_lessonExist_user() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/0"))
                .andExpect(status().isOk());
    }

    @Test
    void getById_lessonExist_unauthorized() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/0"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void getById_lessonNotExist_admin() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void getById_lessonNotExist_user() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getById_lessonNotExist_unauthorized() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/-1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Sql(statements = """
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Test', 'Test', 0, 0, 0, '{"type": "base"}');
            """)
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void update_valid_explicitTeacherId_courseIdMatch_admin() throws Exception {
        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .teacherId(0L)
                .courseId(0L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
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
    @Sql(statements = """
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Test', 'Test', 0, 0, 0, '{"type": "base"}');
            """)
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void update_valid_explicitTeacherId_courseIdMatch_user() throws Exception {
        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .teacherId(1L)
                .courseId(1L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
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
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Test', 'Test', 0, 0, 0, '{"type": "base"}');
            """)
    void update_valid_explicitTeacherId_courseIdMatch_unauthorized() throws Exception {
        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(0L)
                .title("Title")
                .description("Description")
                .teacherId(0L)
                .courseId(0L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
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
    @Sql(statements = """
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Test', 'Test', 0, 0, 0, '{"type": "base"}');
            """)
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void update_valid_explicitTeacherId_courseIdNotMatch_admin() throws Exception {
        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(0L)
                .title("Title")
                .description("Description")
                .teacherId(0L)
                .courseId(1L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
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
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Test', 'Test', 0, 0, 0, '{"type": "base"}');
            """)
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void update_valid_explicitTeacherId_courseIdNotMatch_user() throws Exception {
        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(0L)
                .title("Title")
                .description("Description")
                .teacherId(1L)
                .courseId(0L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
                .build();

        String request = objectMapper.writeValueAsString(updateDto);
        mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Test', 'Test', 0, 0, 0, '{"type": "base"}');
            """)
    void update_valid_explicitTeacherId_courseIdNotMatch_unauthorized() throws Exception {
        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(0L)
                .title("Title")
                .description("Description")
                .teacherId(0L)
                .courseId(1L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
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
    @Sql(statements = """
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Test', 'Test', 0, 0, 0, '{"type": "base"}');
            """)
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void update_valid_implicitTeacherId_courseIdMatch_admin() throws Exception {
        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(0L)
                .title("Title")
                .description("Description")
                .teacherId(null)
                .courseId(0L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
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
    void update_valid_implicitTeacherId_courseIdMatch_user() throws Exception {
        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .teacherId(null)
                .courseId(1L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
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
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            """)
    void update_valid_implicitTeacherId_courseIdMatch_unauthorized() throws Exception {
        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(0L)
                .title("Title")
                .description("Description")
                .teacherId(null)
                .courseId(1L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
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
    @Sql(statements = """
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Test', 'Test', 0, 0, 0, '{"type": "base"}');
            """)
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void update_valid_implicitTeacherId_courseIdNotMatch_admin() throws Exception {
        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .teacherId(null)
                .courseId(1L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
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
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Test', 'Test', 0, 0, 0, '{"type": "base"}');
            """)
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void update_valid_implicitTeacherId_courseIdNotMatch_user() throws Exception {
        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(0L)
                .title("Title")
                .description("Description")
                .teacherId(null)
                .courseId(0L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
                .build();

        String request = objectMapper.writeValueAsString(updateDto);
        mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            """)
    void update_valid_implicitTeacherId_courseIdNotMatch_unauthorized() throws Exception {
        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(0L)
                .title("Title")
                .description("Description")
                .teacherId(null)
                .courseId(1L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
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
    @Sql(statements = """
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Test', 'Test', 0, 0, 0, '{"type": "base"}');
            """)
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void update_valid_anotherTeacherId_courseIdMatchCurrentUser_admin() throws Exception {
        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .teacherId(1L)
                .courseId(0L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
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
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Test', 'Test', 0, 0, 0, '{"type": "base"}');
            """)
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void update_valid_anotherTeacherId_courseIdMatchCurrentUser_user() throws Exception {
        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(0L)
                .title("Title")
                .description("Description")
                .teacherId(0L)
                .courseId(1L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
                .build();

        String request = objectMapper.writeValueAsString(updateDto);
        mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Test', 'Test', 0, 0, 0, '{"type": "base"}');
            """)
    void update_valid_anotherTeacherId_courseIdMatchCurrentUser_unauthorized() throws Exception {
        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(0L)
                .title("Title")
                .description("Description")
                .teacherId(0L)
                .courseId(0L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
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
    @Sql(statements = """
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Test', 'Test', 0, 0, 0, '{"type": "base"}');
            """)
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void update_valid_anotherTeacherId_courseIdNotMatchCurrentUser_admin() throws Exception {
        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .teacherId(1L)
                .courseId(2L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
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
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Test', 'Test', 0, 0, 0, '{"type": "base"}');
            """)
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void update_valid_anotherTeacherId_courseIdNotMatchCurrentUser_user() throws Exception {
        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(0L)
                .title("Title")
                .description("Description")
                .teacherId(0L)
                .courseId(0L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
                .build();

        String request = objectMapper.writeValueAsString(updateDto);
        mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Test', 'Test', 0, 0, 0, '{"type": "base"}');
            """)
    void update_valid_anotherTeacherId_courseIdNotMatchCurrentUser_unauthorized() throws Exception {
        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(0L)
                .title("Title")
                .description("Description")
                .teacherId(0L)
                .courseId(0L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
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
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            insert into lessons(id, teacher_id, course_id, title, description, start_datetime, end_datetime)
                values (0, 0, 5, 'Admin lesson', 'Admin lesson description', '2022-05-19T10:00:00Z', '2022-05-19T11:00:00Z');
            """)
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void update_valid_teacherTimeInterception_admin() throws Exception {
        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .teacherId(0L)
                .courseId(1L)
                .startDateTime(OffsetDateTime.of(2022, 5, 19, 10, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 19, 11, 0, 0, 0, ZoneOffset.UTC))
                .build();

        String request = objectMapper.writeValueAsString(updateDto);
        mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            insert into lessons(id, teacher_id, course_id, title, description, start_datetime, end_datetime)
                values (0, 1, 5, 'Test lesson', 'Test lesson description', '2022-05-19T10:00:00Z', '2022-05-19T11:00:00Z');
            """)
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void update_valid_teacherTimeInterception_user() throws Exception {
        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .teacherId(1L)
                .courseId(1L)
                .startDateTime(OffsetDateTime.of(2022, 5, 19, 10, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 19, 11, 0, 0, 0, ZoneOffset.UTC))
                .build();

        String request = objectMapper.writeValueAsString(updateDto);
        mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            insert into lessons(id, teacher_id, course_id, title, description, start_datetime, end_datetime)
                values (0, 0, 5, 'Admin lesson', 'Admin lesson description', '2022-05-19T10:00:00Z', '2022-05-19T11:00:00Z');
            """)
    void update_valid_teacherTimeInterception_unauthorized() throws Exception {
        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(0L)
                .title("Title")
                .description("Description")
                .teacherId(0L)
                .courseId(1L)
                .startDateTime(OffsetDateTime.of(2022, 5, 19, 10, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 19, 11, 0, 0, 0, ZoneOffset.UTC))
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
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            insert into lessons(id, teacher_id, course_id, title, description, start_datetime, end_datetime)
                values (0, 0, 5, 'Admin lesson', 'Admin lesson description', '2022-05-19T10:00:00Z', '2022-05-19T11:00:00Z');
            """)
    @WithMockUser(
            username = "admin",
            password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
            roles = "ADMIN"
    )
    void update_valid_courseTimeInterception_admin() throws Exception {
        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .teacherId(1L)
                .courseId(5L)
                .startDateTime(OffsetDateTime.of(2022, 5, 19, 10, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 19, 11, 0, 0, 0, ZoneOffset.UTC))
                .build();

        String request = objectMapper.writeValueAsString(updateDto);
        mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            insert into lessons(id, teacher_id, course_id, title, description, start_datetime, end_datetime)
                values (0, 0, 5, 'Admin lesson', 'Admin lesson description', '2022-05-19T10:00:00Z', '2022-05-19T11:00:00Z');
            """)
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void update_valid_courseTimeInterception_user() throws Exception {
        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .teacherId(1L)
                .courseId(5L)
                .startDateTime(OffsetDateTime.of(2022, 5, 19, 10, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 19, 11, 0, 0, 0, ZoneOffset.UTC))
                .build();

        String request = objectMapper.writeValueAsString(updateDto);
        mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            insert into lessons(id, teacher_id, course_id, title, description, start_datetime, end_datetime)
                values (0, 0, 5, 'Admin lesson', 'Admin lesson description', '2022-05-19T10:00:00Z', '2022-05-19T11:00:00Z');
            """)
    void update_valid_courseTimeInterception_unauthorized() throws Exception {
        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(0L)
                .title("Title")
                .description("Description")
                .teacherId(1L)
                .courseId(5L)
                .startDateTime(OffsetDateTime.of(2022, 5, 19, 10, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 19, 11, 0, 0, 0, ZoneOffset.UTC))
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
        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(0L)
                .title("")
                .description("")
                .teacherId(-1L)
                .courseId(null)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 14, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 10, 0, 0, 0, ZoneOffset.UTC))
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
        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(0L)
                .title("")
                .description("")
                .teacherId(-1L)
                .courseId(null)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 14, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 10, 0, 0, 0, ZoneOffset.UTC))
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
        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(0L)
                .title("")
                .description("")
                .teacherId(-1L)
                .courseId(null)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 14, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 10, 0, 0, 0, ZoneOffset.UTC))
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
        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(-1L)
                .title("Title")
                .description("Description")
                .teacherId(0L)
                .courseId(1L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
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
        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(-1L)
                .title("Title")
                .description("Description")
                .teacherId(1L)
                .courseId(1L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
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
        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(-1L)
                .title("Title")
                .description("Description")
                .teacherId(0L)
                .courseId(1L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
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
    void deleteById_valid_admin() throws Exception {
        mockMvc.perform(delete(URL_PREFIX + "/0"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void deleteById_valid_user_notMatch() throws Exception {
        mockMvc.perform(delete(URL_PREFIX + "/0"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(
            username = "user",
            password = "$2a$12$P.3wP0iRFhf1sf.uz/Mw3uAryavgimcHeH89YB90gTrWjYh/3o6Nu",
            roles = "USER"
    )
    void deleteById_valid_user_match() throws Exception {
        mockMvc.perform(delete(URL_PREFIX + "/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteById_valid_unauthorized() throws Exception {
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
}