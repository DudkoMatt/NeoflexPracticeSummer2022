package ru.dudkomv.neoflexpractice.coursecategory;

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
import ru.dudkomv.neoflexpractice.coursecategory.dto.CourseCategoryCreationDto;
import ru.dudkomv.neoflexpractice.coursecategory.dto.CourseCategoryUpdateDto;

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
class CourseCategoryControllerTest extends ApplicationIntegrationTest {
    private static final String URL_PREFIX = "/course/category";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseCategoryService courseCategoryService;
    
    @Autowired
    private CourseCategoryRepository courseCategoryRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @SpyBean
    private CourseCategoryController courseCategoryController;
    
    @BeforeEach
    void setUp() {
        Mockito.reset(courseCategoryController);
    }

    @AfterEach
    void tearDown() {
        cleanAndMigrate();
    }

    @Test
    void create_valid() throws Exception {
        assertFalse(courseCategoryRepository.existsById(6L));
        CourseCategoryCreationDto creationDto = CourseCategoryCreationDto.builder()
                .title("Test title")
                .parentId(0L)
                .build();

        CourseCategory courseCategory = CourseCategory.builder()
                .id(6L)
                .title("Test title")
                .parentId(0L)
                .build();

        String request = objectMapper.writeValueAsString(creationDto);
        String expectedAnswer = objectMapper.writeValueAsString(courseCategory);

        mockMvc.perform(
                        post(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(expectedAnswer));

        assertTrue(courseCategoryRepository.existsById(6L));
        assertEquals(courseCategory, courseCategoryService.getById(6L));
        verify(courseCategoryController, times(1)).create(creationDto);
    }

    @Test
    void create_invalid() throws Exception {
        CourseCategoryCreationDto creationDto = CourseCategoryCreationDto.builder()
                .title("")
                .parentId(null)
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
                "parentId: Course category parent id must not be null",
                "title: Course category title must not be blank"
        );

        ExceptionControllerAdvice.ApiError apiError = toApiError(objectMapper, response);
        List<String> actualErrors = toSortedListOfErrors(apiError);

        assertEquals(expectedErrors, actualErrors);
        assertNull(apiError.getMessage());
        verify(courseCategoryController, times(0)).create(any());
    }

    @Test
    void getAll() throws Exception {
        CourseCategory category0 = CourseCategory.of(0L, "Default Category", null);
        CourseCategory category1 = CourseCategory.of(1L, "Programming", 0L);
        CourseCategory category2 = CourseCategory.of(2L, "Java", 1L);
        CourseCategory category3 = CourseCategory.of(3L, "Python", 1L);
        CourseCategory category4 = CourseCategory.of(4L, "Data Science", 0L);
        CourseCategory category5 = CourseCategory.of(5L, "Python ML", 4L);

        List<CourseCategory> expectedList = List.of(category0, category1, category2, category3, category4, category5);
        String expectedAnswer = objectMapper.writeValueAsString(expectedList);

        mockMvc.perform(get(URL_PREFIX))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedAnswer));

        verify(courseCategoryController, times(1)).getAll();
    }

    @Test
    void getById_categoryExist() throws Exception {
        CourseCategory category = CourseCategory.of(0L, "Default Category", null);
        String expectedAnswer = objectMapper.writeValueAsString(category);

        mockMvc.perform(get(URL_PREFIX + "/0"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedAnswer));

        verify(courseCategoryController, times(1)).getById(0L);
    }

    @Test
    void getById_categoryNotExist() throws Exception {
        String expectedMessage = "Course category entity not found with id=-1";
        ExceptionControllerAdvice.ApiError expectedError = ExceptionControllerAdvice.ApiError.builder()
                .message(expectedMessage)
                .build();
        String expectedResponse = objectMapper.writeValueAsString(expectedError);

        mockMvc.perform(get(URL_PREFIX + "/-1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(expectedResponse));

        verify(courseCategoryController, times(1)).getById(-1L);
    }

    @Test
    void update_valid() throws Exception {
        assertTrue(courseCategoryRepository.existsById(1L));
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
                .andExpect(status().isOk())
                .andExpect(content().string(expectedAnswer));

        CourseCategory expected = CourseCategory.of(1L, "Test title", 0L);
        CourseCategory actual = courseCategoryService.getById(1L);
        assertEquals(expected, actual);
        verify(courseCategoryController, times(1)).update(updateDto);
    }

    @Test
    void update_invalid() throws Exception {
        assertTrue(courseCategoryRepository.existsById(1L));
        CourseCategory initialCourseCategory = courseCategoryService.getById(1L);

        CourseCategoryUpdateDto updateDto = CourseCategoryUpdateDto.builder()
                .id(1L)
                .title("")
                .parentId(null)
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
                "parentId: Course category parent id must not be null",
                "title: Course category title must not be blank"
        );

        ExceptionControllerAdvice.ApiError apiError = toApiError(objectMapper, response);
        List<String> actualErrors = toSortedListOfErrors(apiError);

        assertEquals(expectedErrors, actualErrors);
        assertNull(apiError.getMessage());

        assertEquals(initialCourseCategory, courseCategoryService.getById(1L));
        verify(courseCategoryController, times(0)).update(updateDto);
    }

    @Test
    void update_nonExistent() throws Exception {
        assertFalse(courseCategoryRepository.existsById(-1L));
        CourseCategoryUpdateDto updateDto = CourseCategoryUpdateDto.builder()
                .id(-1L)
                .title("Title")
                .parentId(0L)
                .build();

        String invalidRequest = objectMapper.writeValueAsString(updateDto);

        String expectedMessage = "Course category entity not found with id=-1";
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

        verify(courseCategoryController, times(1)).update(updateDto);
        assertFalse(courseCategoryRepository.existsById(-1L));
    }

    @Test
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            truncate table courses restart identity cascade;
            """)
    void deleteById_valid() throws Exception {
        assertTrue(courseCategoryRepository.existsById(5L));

        mockMvc.perform(delete(URL_PREFIX + "/5"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        assertFalse(courseCategoryRepository.existsById(5L));
        verify(courseCategoryController, times(1)).deleteById(5L);
    }

    @Test
    void deleteById_nonExistent() throws Exception {
        assertFalse(courseCategoryRepository.existsById(-1L));

        String expectedMessage = "Course category entity not found with id=-1";
        ExceptionControllerAdvice.ApiError expectedError = ExceptionControllerAdvice.ApiError.builder()
                .message(expectedMessage)
                .build();

        String expectedResponse = objectMapper.writeValueAsString(expectedError);

        mockMvc.perform(delete(URL_PREFIX + "/-1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(expectedResponse));

        assertFalse(courseCategoryRepository.existsById(-1L));
        verify(courseCategoryController, times(1)).deleteById(-1L);
    }
}