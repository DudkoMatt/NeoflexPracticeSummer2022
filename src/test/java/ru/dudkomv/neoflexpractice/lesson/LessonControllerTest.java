package ru.dudkomv.neoflexpractice.lesson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.dudkomv.neoflexpractice.ApplicationIntegrationTest;
import ru.dudkomv.neoflexpractice.advice.ExceptionControllerAdvice;
import ru.dudkomv.neoflexpractice.lesson.dto.LessonCreationDto;
import ru.dudkomv.neoflexpractice.lesson.dto.LessonUpdateDto;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WithMockUser(
        username = "admin",
        password = "$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa",
        authorities = {"ROLE_ADMIN"}
)
class LessonControllerTest extends ApplicationIntegrationTest {
    private static final String URL_PREFIX = "/lesson";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private LessonRepository lessonRepository;
    
    @Autowired
    private LessonService lessonService;

    @SpyBean
    private LessonController lessonController;

    @BeforeEach
    void setUp() {
        Mockito.reset(lessonController);
    }

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
    void create_valid_explicitTeacherId() throws Exception {
        assertFalse(lessonRepository.existsById(1L));

        LessonCreationDto creationDto = LessonCreationDto.builder()
                .title("Title")
                .description("Description")
                .teacherId(0L)
                .courseId(0L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
                .build();

        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .teacherId(0L)
                .courseId(0L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
                .build();

        String request = objectMapper.writeValueAsString(creationDto);
        String expectedAnswer = objectMapper.writeValueAsString(updateDto);

        mockMvc.perform(
                        post(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(expectedAnswer));

        assertTrue(lessonRepository.existsById(1L));
        assertEquals(updateDto, lessonService.getById(1L));
        verify(lessonController, times(1)).create(any());
    }

    @Test
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            """)
    void create_valid_implicitTeacherId() throws Exception {
        assertFalse(lessonRepository.existsById(1L));

        LessonCreationDto creationDto = LessonCreationDto.builder()
                .title("Title")
                .description("Description")
                .teacherId(null)
                .courseId(1L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
                .build();

        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .teacherId(0L)
                .courseId(1L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
                .build();

        String request = objectMapper.writeValueAsString(creationDto);
        String expectedAnswer = objectMapper.writeValueAsString(updateDto);

        mockMvc.perform(
                        post(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(expectedAnswer));

        assertTrue(lessonRepository.existsById(1L));
        assertEquals(updateDto, lessonService.getById(1L));
        verify(lessonController, times(1)).create(any());
    }

    @Test
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            insert into lessons(id, teacher_id, course_id, title, description, start_datetime, end_datetime)
                values (0, 0, 5, 'Admin lesson', 'Admin lesson description', '2022-05-19T10:00:00Z', '2022-05-19T11:00:00Z');
            """)
    void create_valid_teacherTimeInterception() throws Exception {
        assertTrue(lessonRepository.existsById(0L));
        assertFalse(lessonRepository.existsById(1L));

        LessonCreationDto creationDto = LessonCreationDto.builder()
                .title("Title")
                .description("Description")
                .teacherId(0L)
                .courseId(1L)
                .startDateTime(OffsetDateTime.of(2022, 5, 19, 10, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 19, 11, 0, 0, 0, ZoneOffset.UTC))
                .build();

        String request = objectMapper.writeValueAsString(creationDto);

        MvcResult response = mockMvc.perform(
                        post(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        assertFalse(lessonRepository.existsById(1L));

        List<String> expectedErrors = List.of(
                "lessonCreationDto: Lesson time intercepts with some teacher's lesson"
        );

        ExceptionControllerAdvice.ApiError apiError = toApiError(objectMapper, response);
        List<String> actualErrors = toSortedListOfErrors(apiError);

        assertEquals(expectedErrors, actualErrors);
        assertNull(apiError.getMessage());
        verify(lessonController, times(0)).create(any());
    }

    @Test
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            insert into lessons(id, teacher_id, course_id, title, description, start_datetime, end_datetime)
                values (0, 0, 5, 'Admin lesson', 'Admin lesson description', '2022-05-19T10:00:00Z', '2022-05-19T11:00:00Z');
            """)
    void create_valid_courseTimeInterception() throws Exception {
        assertTrue(lessonRepository.existsById(0L));
        assertFalse(lessonRepository.existsById(1L));

        LessonCreationDto creationDto = LessonCreationDto.builder()
                .title("Title")
                .description("Description")
                .teacherId(1L)
                .courseId(5L)
                .startDateTime(OffsetDateTime.of(2022, 5, 19, 10, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 19, 11, 0, 0, 0, ZoneOffset.UTC))
                .build();

        String request = objectMapper.writeValueAsString(creationDto);

        MvcResult response = mockMvc.perform(
                        post(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        assertFalse(lessonRepository.existsById(1L));

        List<String> expectedErrors = List.of(
                "lessonCreationDto: Lesson time intercepts with some another course lesson"
        );

        ExceptionControllerAdvice.ApiError apiError = toApiError(objectMapper, response);
        List<String> actualErrors = toSortedListOfErrors(apiError);

        assertEquals(expectedErrors, actualErrors);
        assertNull(apiError.getMessage());
        verify(lessonController, times(0)).create(any());
    }

    @Test
    void create_invalid() throws Exception {
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .title("")
                .description("")
                .teacherId(-1L)
                .courseId(null)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 14, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 10, 0, 0, 0, ZoneOffset.UTC))
                .build();

        String invalidRequest = objectMapper.writeValueAsString(creationDto);
        MvcResult response = mockMvc.perform(
                        post(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(invalidRequest)
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        List<String> expectedErrors = List.of(
                "courseId: Lesson must have a course id",
                "description: Lesson description must not be blank",
                "lessonCreationDto: The start date time of the lesson must be before the end date time",
                "title: Lesson title must not be blank"
        );

        ExceptionControllerAdvice.ApiError apiError = toApiError(objectMapper, response);
        List<String> actualErrors = toSortedListOfErrors(apiError);

        assertEquals(expectedErrors, actualErrors);
        assertNull(apiError.getMessage());
        verify(lessonController, times(0)).create(any());
    }

    @Test
    void getAllForUser() throws Exception {
        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(0L)
                .title("Admin lesson")
                .description("Admin lesson description")
                .teacherId(0L)
                .courseId(5L)
                .startDateTime(OffsetDateTime.of(2022, 5, 19, 10, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 19, 11, 0, 0, 0, ZoneOffset.UTC))
                .build();

        String expectedAnswer = objectMapper.writeValueAsString(List.of(updateDto));
        mockMvc.perform(get(URL_PREFIX))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedAnswer));

        verify(lessonController, times(1)).getAllForUser(any());
    }

    @Test
    void getAllForUserByUserId_userExist() throws Exception {
        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(0L)
                .title("Admin lesson")
                .description("Admin lesson description")
                .teacherId(0L)
                .courseId(5L)
                .startDateTime(OffsetDateTime.of(2022, 5, 19, 10, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 19, 11, 0, 0, 0, ZoneOffset.UTC))
                .build();

        String expectedAnswer = objectMapper.writeValueAsString(List.of(updateDto));
        mockMvc.perform(get(URL_PREFIX + "/user/0"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedAnswer));

        verify(lessonController, times(1)).getAllForUserByUserId(0L);
    }

    @Test
    void getAllForUserByUserId_userNotExist() throws Exception {
        String expectedMessage = "Teacher entity not found with id=-1";
        ExceptionControllerAdvice.ApiError expectedError = ExceptionControllerAdvice.ApiError.builder()
                .message(expectedMessage)
                .build();

        String expectedResponse = objectMapper.writeValueAsString(expectedError);
        mockMvc.perform(get(URL_PREFIX + "/user/-1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(expectedResponse));

        verify(lessonController, times(1)).getAllForUserByUserId(-1L);
    }

    @Test
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            insert into lessons(id, teacher_id, course_id, title, description, start_datetime, end_datetime)
                values (0, 0, 5, 'Admin lesson', 'Admin lesson description', '2022-05-19T10:00:00Z', '2022-05-19T11:00:00Z');
            """)
    void getAll() throws Exception {
        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(0L)
                .title("Admin lesson")
                .description("Admin lesson description")
                .teacherId(0L)
                .courseId(5L)
                .startDateTime(OffsetDateTime.of(2022, 5, 19, 10, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 19, 11, 0, 0, 0, ZoneOffset.UTC))
                .build();

        String expectedAnswer = objectMapper.writeValueAsString(List.of(updateDto));
        mockMvc.perform(get(URL_PREFIX + "/all"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedAnswer));

        verify(lessonController, times(1)).getAll();
    }

    @Test
    void getById_lessonExist() throws Exception {
        Long expectedId = 0L;
        LessonUpdateDto expectedLesson = lessonService.getById(expectedId);

        String expectedResponse = objectMapper.writeValueAsString(expectedLesson);
        mockMvc.perform(get(URL_PREFIX + "/" + expectedId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));

        verify(lessonController, times(1)).getById(expectedId);
    }

    @Test
    void getById_lessonNotExist() throws Exception {
        String expectedMessage = "Lesson entity not found with id=-1";
        ExceptionControllerAdvice.ApiError expectedError = ExceptionControllerAdvice.ApiError.builder()
                .message(expectedMessage)
                .build();

        String expectedResponse = objectMapper.writeValueAsString(expectedError);
        mockMvc.perform(get(URL_PREFIX + "/-1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(expectedResponse));

        verify(lessonController, times(1)).getById(-1L);
    }

    @Test
    void update_valid() throws Exception {
        assertTrue(lessonRepository.existsById(1L));

        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .teacherId(0L)
                .courseId(1L)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 12, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
                .build();

        String expectedAnswer = objectMapper.writeValueAsString(updateDto);
        mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(expectedAnswer)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(expectedAnswer));

        LessonUpdateDto actualLesson = lessonService.getById(1L);
        assertEquals(updateDto, actualLesson);
        verify(lessonController, times(1)).update(any());
    }

    @Test
    void update_invalid() throws Exception {
        assertTrue(lessonRepository.existsById(1L));
        LessonUpdateDto initialLessonUpdateDto = lessonService.getById(1L);

        LessonUpdateDto updateDto = LessonUpdateDto.builder()
                .id(1L)
                .title("")
                .description("")
                .teacherId(null)
                .courseId(null)
                .startDateTime(OffsetDateTime.of(2022, 5, 22, 14, 0, 0, 0, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(2022, 5, 22, 13, 0, 0, 0, ZoneOffset.UTC))
                .build();

        String invalidRequest = objectMapper.writeValueAsString(updateDto);
        MvcResult response = mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(invalidRequest)
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        List<String> expectedErrors = List.of(
                "courseId: Lesson must have a course id",
                "description: Lesson description must not be blank",
                "lessonUpdateDto: The start date time of the lesson must be before the end date time",
                "title: Lesson title must not be blank"
        );

        ExceptionControllerAdvice.ApiError apiError = toApiError(objectMapper, response);
        List<String> actualErrors = toSortedListOfErrors(apiError);

        assertEquals(expectedErrors, actualErrors);
        assertNull(apiError.getMessage());

        assertEquals(initialLessonUpdateDto, lessonService.getById(1L));
        verify(lessonController, times(0)).update(updateDto);
    }

    @Test
    void update_nonExistent() throws Exception {
        assertFalse(lessonRepository.existsById(-1L));

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
        String expectedMessage = "Lesson entity not found with id=-1";
        ExceptionControllerAdvice.ApiError expectedError = ExceptionControllerAdvice.ApiError.builder()
                .message(expectedMessage)
                .build();

        String expectedResponse = objectMapper.writeValueAsString(expectedError);
        mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(invalidRequest)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().string(expectedResponse));

        verify(lessonController, times(1)).update(any());
        assertFalse(lessonRepository.existsById(-1L));
    }

    @Test
    void deleteById_valid() throws Exception {
        assertTrue(lessonRepository.existsById(0L));

        mockMvc.perform(delete(URL_PREFIX + "/0"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        assertFalse(lessonRepository.existsById(0L));
        verify(lessonController, times(1)).deleteById(0L);
    }

    @Test
    void deleteById_nonExistent() throws Exception {
        assertFalse(lessonRepository.existsById(-1L));

        String expectedMessage = "Lesson entity not found with id=-1";
        ExceptionControllerAdvice.ApiError expectedError = ExceptionControllerAdvice.ApiError.builder()
                .message(expectedMessage)
                .build();

        String expectedResponse = objectMapper.writeValueAsString(expectedError);
        mockMvc.perform(delete(URL_PREFIX + "/-1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(expectedResponse));

        assertFalse(lessonRepository.existsById(-1L));
        verify(lessonController, times(0)).deleteById(-1L);
    }
}