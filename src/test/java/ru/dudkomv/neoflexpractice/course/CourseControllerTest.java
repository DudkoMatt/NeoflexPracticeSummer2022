package ru.dudkomv.neoflexpractice.course;

import com.fasterxml.jackson.core.type.TypeReference;
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
import ru.dudkomv.neoflexpractice.advice.ExceptionControllerAdvice.ApiError;
import ru.dudkomv.neoflexpractice.course.dto.CourseCreationDto;
import ru.dudkomv.neoflexpractice.course.dto.CourseUpdateDto;
import ru.dudkomv.neoflexpractice.course.mapper.CourseMapper;
import ru.dudkomv.neoflexpractice.course.type.BaseCourseType;
import ru.dudkomv.neoflexpractice.course.type.OfflineCourseType;
import ru.dudkomv.neoflexpractice.course.type.OnlineCourseType;
import ru.dudkomv.neoflexpractice.lesson.dto.LessonUpdateDto;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
class CourseControllerTest extends ApplicationIntegrationTest {
    private static final String URL_PREFIX = "/course";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CourseMapper courseMapper;

    @SpyBean
    private CourseController courseController;

    @BeforeEach
    void setUp() {
        Mockito.reset(courseController);
    }

    @AfterEach
    void tearDown() {
        cleanAndMigrate();
    }

    @Test
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            truncate table courses restart identity cascade;
            """)
    void create_valid() throws Exception {
        assertFalse(courseRepository.existsById(1L));

        CourseCreationDto creationDto = CourseCreationDto.builder()
                .title("Title")
                .description("Description")
                .categoryId(0L)
                .curatorId(0L)
                .studentCount(0L)
                .type(new BaseCourseType())
                .build();

        CourseUpdateDto updateDto = CourseUpdateDto.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .categoryId(0L)
                .curatorId(0L)
                .studentCount(0L)
                .type(new BaseCourseType())
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

        assertTrue(courseRepository.existsById(1L));
        assertEquals(courseMapper.toEntity(updateDto), courseRepository.findById(1L).orElseThrow());
        verify(courseController, times(1)).create(creationDto);
    }

    @Test
    void create_invalid() throws Exception {
        CourseCreationDto creationDto = CourseCreationDto.builder()
                .title("")
                .description("")
                .categoryId(null)
                .curatorId(null)
                .studentCount(-1L)
                .type(null)
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
                "categoryId: Course category must not be null",
                "description: Course description must not be blank",
                "studentCount: Course student count must be positive or zero",
                "title: Course title must not be blank",
                "type: Course type must not be null"
        );

        ApiError apiError = toApiError(objectMapper, response);
        List<String> actualErrors = toSortedListOfErrors(apiError);

        assertEquals(expectedErrors, actualErrors);
        assertNull(apiError.getMessage());
        verify(courseController, times(0)).create(any());
    }

    @Test
    @Sql(statements = """
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Test Course', 'Test Course', 0, 0, 0, '{"type": "base"}');
            """)
    void getAllForUser() throws Exception {
        assertTrue(courseRepository.existsById(0L));

        CourseUpdateDto updateDto = CourseUpdateDto.builder()
                .id(0L)
                .title("Test Course")
                .description("Test Course")
                .categoryId(0L)
                .curatorId(0L)
                .studentCount(0L)
                .type(new BaseCourseType())
                .build();

        String expectedAnswer = objectMapper.writeValueAsString(List.of(updateDto));
        mockMvc.perform(get(URL_PREFIX))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedAnswer));

        verify(courseController, times(1)).getAllForUser(any());
    }

    @Test
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            truncate table courses restart identity cascade;
            
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (1, 'Base Course', 'Base Course', 0, 1, 0, '{"type": "base"}');
            
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (2, 'Programming Course', 'Programming Course', 1, 1, 0, '{"type": "base"}');
            """)
    void getAll() throws Exception {
        CourseUpdateDto updateDto1 = CourseUpdateDto.builder()
                .id(1L)
                .title("Base Course")
                .description("Base Course")
                .categoryId(0L)
                .curatorId(1L)
                .studentCount(0L)
                .type(new BaseCourseType())
                .build();

        CourseUpdateDto updateDto2 = CourseUpdateDto.builder()
                .id(2L)
                .title("Programming Course")
                .description("Programming Course")
                .categoryId(1L)
                .curatorId(1L)
                .studentCount(0L)
                .type(new BaseCourseType())
                .build();

        List<CourseUpdateDto> expectedList = List.of(updateDto1, updateDto2);
        String expectedAnswer = objectMapper.writeValueAsString(expectedList);

        mockMvc.perform(get(URL_PREFIX + "/all"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedAnswer));

        verify(courseController, times(1)).getAll();
    }

    @Test
    void getById_courseExist() throws Exception {
        Long expectedId = 1L;
        CourseUpdateDto expectedCourse = courseService.getById(expectedId);

        String expectedResponse = objectMapper.writeValueAsString(expectedCourse);
        mockMvc.perform(get(URL_PREFIX + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));

        verify(courseController, times(1)).getById(expectedId);
    }

    @Test
    void getById_courseNotExist() throws Exception {
        String expectedMessage = "Course entity not found with id=-1";
        ApiError expectedError = ApiError.builder()
                .message(expectedMessage)
                .build();

        String expectedResponse = objectMapper.writeValueAsString(expectedError);
        mockMvc.perform(get(URL_PREFIX + "/-1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(expectedResponse));

        verify(courseController, times(1)).getById(-1L);
    }

    @Test
    void getLessonsById_courseExist() throws Exception {
        Long courseId = 5L;

        LocalDateTime startDateTime = LocalDateTime.of(2022, 5, 19, 10, 0 ,0);
        LocalDateTime endDateTime = LocalDateTime.of(2022, 5, 19, 11, 0 ,0);

        LessonUpdateDto lessonUpdateDto = LessonUpdateDto.builder()
                .id(0L)
                .teacherId(0L)
                .courseId(courseId)
                .title("Admin lesson")
                .description("Admin lesson description")
                .startDateTime(OffsetDateTime.of(startDateTime, ZoneOffset.UTC))
                .endDateTime(OffsetDateTime.of(endDateTime, ZoneOffset.UTC))
                .build();

        String expectedResponse = objectMapper.writeValueAsString(List.of(lessonUpdateDto));
        mockMvc.perform(get(URL_PREFIX + "/" + courseId + "/lesson"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));

        verify(courseController, times(1)).getLessonsById(courseId);
    }

    @Test
    void getLessonsById_courseNotExist() throws Exception {
        String expectedMessage = "Course entity not found with id=-1";
        ApiError expectedError = ApiError.builder()
                .message(expectedMessage)
                .build();

        String expectedResponse = objectMapper.writeValueAsString(expectedError);
        mockMvc.perform(get(URL_PREFIX + "/-1/lesson"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(expectedResponse));

        verify(courseController, times(1)).getLessonsById(-1L);
    }

    @Test
    void update_valid() throws Exception {
        assertTrue(courseRepository.existsById(1L));

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
                .andExpect(status().isOk())
                .andExpect(content().string(expectedAnswer));

        CourseUpdateDto actualCourse = courseService.getById(1L);
        assertEquals(updateDto, actualCourse);
        verify(courseController, times(1)).update(updateDto);
    }

    @Test
    void update_invalid() throws Exception {
        assertTrue(courseRepository.existsById(1L));
        CourseUpdateDto initialCourseUpdateDto = courseService.getById(1L);

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
        MvcResult response = mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(invalidRequest)
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        List<String> expectedErrors = List.of(
                "categoryId: Course category must not be null",
                "description: Course description must not be blank",
                "studentCount: Course student count must be positive or zero",
                "title: Course title must not be blank",
                "type: Course type must not be null"
        );

        ApiError apiError = toApiError(objectMapper, response);
        List<String> actualErrors = toSortedListOfErrors(apiError);

        assertEquals(expectedErrors, actualErrors);
        assertNull(apiError.getMessage());

        assertEquals(initialCourseUpdateDto, courseService.getById(1L));
        verify(courseController, times(0)).update(updateDto);
    }

    @Test
    void update_nonExistent() throws Exception {
        assertFalse(courseRepository.existsById(-1L));

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
        String expectedMessage = "Course entity not found with id=-1";
        ApiError expectedError = ApiError.builder()
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

        verify(courseController, times(1)).update(updateDto);
        assertFalse(courseRepository.existsById(-1L));
    }

    @Test
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            truncate table courses restart identity cascade;
            
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (1, 'Base Course', 'Base Course', 0, 1, 0, '{"type": "base"}');
            """)
    void deleteById_valid() throws Exception {
        assertTrue(courseRepository.existsById(1L));

        mockMvc.perform(delete(URL_PREFIX + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        assertFalse(courseRepository.existsById(1L));
        verify(courseController, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_nonExistent() throws Exception {
        assertFalse(courseRepository.existsById(-1L));

        String expectedMessage = "Course entity not found with id=-1";
        ApiError expectedError = ApiError.builder()
                .message(expectedMessage)
                .build();

        String expectedResponse = objectMapper.writeValueAsString(expectedError);
        mockMvc.perform(delete(URL_PREFIX + "/-1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(expectedResponse));

        assertFalse(courseRepository.existsById(-1L));
        verify(courseController, times(0)).deleteById(-1L);
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    void getCoursesByCategoryIdRecursively_valid() throws Exception {
        Long defaultCategoryId = 0L;
        Long programmingCategoryId = 1L;
        Long javaCategoryId = 2L;
        Long pythonCategoryId = 3L;
        Long dataScienceCategoryId = 4L;
        Long pythonMLCategoryId = 5L;

        Course course1 = Course.of(1L, "Base Course", "Base Course", defaultCategoryId, 1L, 0L, new BaseCourseType());
        Course course2 = Course.of(2L, "Programming Course", "Programming Course", programmingCategoryId, 1L, 0L, new BaseCourseType());
        Course course3 = Course.of(3L, "Java Course", "Java Course", javaCategoryId, 1L, 0L, new BaseCourseType());
        Course course4 = Course.of(4L, "Python Course", "Python Course", pythonCategoryId, 1L, 0L, new BaseCourseType());
        Course course5 = Course.of(5L, "Data Science Course", "Data Science Course", dataScienceCategoryId, 1L, 0L, new OfflineCourseType("ITMO", "Kronverksky Pr. 49"));
        Course course6 = Course.of(6L, "Python ML Course", "Python ML Course", pythonMLCategoryId, 1L, 0L, new OnlineCourseType("http://some.url"));

        List<Course> baseCategory = List.of(course1, course2, course3, course4, course5, course6);
        List<Course> programmingCategory = List.of(course2, course3, course4);
        List<Course> javaCategory = List.of(course3);
        List<Course> pythonCategory = List.of(course4);
        List<Course> dataScienceCategory = List.of(course5, course6);
        List<Course> pythonMLCategory = List.of(course6);

        String result = mockMvc.perform(get(URL_PREFIX + "/by-category-id-recursively/" + defaultCategoryId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Course> resultList = objectMapper.readValue(result, new TypeReference<>(){});
        resultList.sort((c1, c2) -> (int) (c1.getId() - c2.getId()));
        assertEquals(baseCategory, resultList);


        result = mockMvc.perform(get(URL_PREFIX + "/by-category-id-recursively/" + programmingCategoryId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        resultList = objectMapper.readValue(result, new TypeReference<>(){});
        resultList.sort((c1, c2) -> (int) (c1.getId() - c2.getId()));
        assertEquals(programmingCategory, resultList);


        result = mockMvc.perform(get(URL_PREFIX + "/by-category-id-recursively/" + javaCategoryId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        resultList = objectMapper.readValue(result, new TypeReference<>(){});
        resultList.sort((c1, c2) -> (int) (c1.getId() - c2.getId()));
        assertEquals(javaCategory, resultList);


        result = mockMvc.perform(get(URL_PREFIX + "/by-category-id-recursively/" + pythonCategoryId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        resultList = objectMapper.readValue(result, new TypeReference<>(){});
        resultList.sort((c1, c2) -> (int) (c1.getId() - c2.getId()));
        assertEquals(pythonCategory, resultList);


        result = mockMvc.perform(get(URL_PREFIX + "/by-category-id-recursively/" + dataScienceCategoryId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        resultList = objectMapper.readValue(result, new TypeReference<>(){});
        resultList.sort((c1, c2) -> (int) (c1.getId() - c2.getId()));
        assertEquals(dataScienceCategory, resultList);


        result = mockMvc.perform(get(URL_PREFIX + "/by-category-id-recursively/" + pythonMLCategoryId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        resultList = objectMapper.readValue(result, new TypeReference<>(){});
        resultList.sort((c1, c2) -> (int) (c1.getId() - c2.getId()));
        assertEquals(pythonMLCategory, resultList);

        verify(courseController, times(1)).getCoursesByCategoryIdRecursively(defaultCategoryId);
        verify(courseController, times(1)).getCoursesByCategoryIdRecursively(programmingCategoryId);
        verify(courseController, times(1)).getCoursesByCategoryIdRecursively(javaCategoryId);
        verify(courseController, times(1)).getCoursesByCategoryIdRecursively(pythonCategoryId);
        verify(courseController, times(1)).getCoursesByCategoryIdRecursively(dataScienceCategoryId);
        verify(courseController, times(1)).getCoursesByCategoryIdRecursively(pythonMLCategoryId);
    }

    @Test
    void getCoursesByCategoryIdRecursively_nonExistent() throws Exception {
        Long nonExistentCategoryId = -1L;

        String expectedMessage = "Course category entity not found with id=" + nonExistentCategoryId;
        ApiError expectedError = ApiError.builder()
                .message(expectedMessage)
                .build();

        String expectedResponse = objectMapper.writeValueAsString(expectedError);
        mockMvc.perform(get(URL_PREFIX + "/by-category-id-recursively/" + nonExistentCategoryId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(expectedResponse));

        verify(courseController, times(1)).getCoursesByCategoryIdRecursively(nonExistentCategoryId);
    }

    @Test
    void getCoursesByType_valid() throws Exception {
        Long courseId = 5L;
        String courseType = OfflineCourseType.CLASS_TYPE_NAME;
        CourseUpdateDto expectedCourse = courseService.getById(courseId);
        String expectedResponse = objectMapper.writeValueAsString(List.of(expectedCourse));

        mockMvc.perform(get(URL_PREFIX + "/by-type-name-recursively/" + courseType))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));

        verify(courseController, times(1)).getCoursesByType(courseType);
    }

    @Test
    void getCoursesByType_nonExistent() throws Exception {
        String courseType = "nonExistentType";

        mockMvc.perform(get(URL_PREFIX + "/by-type-name-recursively/" + courseType))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

        verify(courseController, times(1)).getCoursesByType(courseType);
    }

    @Test
    @Sql(statements = """
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Admin Course', 'Admin Course', 0, 0, 0, '{"type": "base"}');
            """)
    void getCoursesByCuratorId_valid() throws Exception {
        Long curatorId = 0L;
        Course expectedCourse = Course.of(0L, "Admin Course", "Admin Course", 0L, 0L, 0L, new BaseCourseType());

        String expectedResponse = objectMapper.writeValueAsString(List.of(expectedCourse));
        mockMvc.perform(get(URL_PREFIX + "/by-curator-id/" + curatorId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));

        verify(courseController, times(1)).getCoursesByCuratorId(curatorId);
    }

    @Test
    void getCoursesByCuratorId_nonExistent() throws Exception {
        Long curatorId = -1L;

        String expectedMessage = "Teacher entity not found with id=" + curatorId;
        ApiError expectedError = ApiError.builder()
                .message(expectedMessage)
                .build();

        String expectedResponse = objectMapper.writeValueAsString(expectedError);
        mockMvc.perform(get(URL_PREFIX + "/by-curator-id/" + curatorId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(expectedResponse));

        verify(courseController, times(1)).getCoursesByCuratorId(curatorId);
    }

    @Test
    void cloneCourse_valid() throws Exception {
        Long courseId = 5L;
        assertTrue(courseRepository.existsById(courseId));

        Long expectedClonedCourseId = 7L;
        CourseUpdateDto expectedCourse = courseService.getById(courseId);
        expectedCourse.setId(expectedClonedCourseId);
        expectedCourse.setCuratorId(0L);

        List<LessonUpdateDto> expectedLessons = courseService.getLessonsById(courseId);
        Function<LessonUpdateDto, LessonUpdateDto> updateLessonDto = lesson -> {
            lesson.setId(null);
            lesson.setTeacherId(0L);
            lesson.setCourseId(7L);
            lesson.setStartDateTime(null);
            lesson.setEndDateTime(null);
            return lesson;
        };

        expectedLessons = expectedLessons.stream()
                .map(updateLessonDto)
                .sorted((c1, c2) -> (int) (c1.getId() - c2.getId()))
                .toList();

        String expectedResponse = objectMapper.writeValueAsString(expectedCourse);

        mockMvc.perform(post(URL_PREFIX + "/clone/" + courseId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));

        assertTrue(courseRepository.existsById(expectedClonedCourseId));

        Function<LessonUpdateDto, LessonUpdateDto> setNullId = lesson -> {
            lesson.setId(null);
            return lesson;
        };

        List<LessonUpdateDto> actualLessons = courseService.getLessonsById(expectedClonedCourseId)
                .stream()
                .map(setNullId)
                .sorted((c1, c2) -> (int) (c1.getId() - c2.getId()))
                .toList();

        assertEquals(expectedLessons, actualLessons);
        verify(courseController, times(1)).cloneCourse(eq(courseId), any());
    }

    @Test
    void cloneCourse_nonExistent() throws Exception {
        Long courseId = -1L;

        String expectedMessage = "Course entity not found with id=" + courseId;
        ApiError expectedError = ApiError.builder()
                .message(expectedMessage)
                .build();

        String expectedResponse = objectMapper.writeValueAsString(expectedError);
        mockMvc.perform(post(URL_PREFIX + "/clone/" + courseId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(expectedResponse));

        verify(courseController, times(1)).cloneCourse(eq(courseId), any());
    }
}