package ru.dudkomv.neoflexpractice.teacher;

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
import ru.dudkomv.neoflexpractice.teacher.dto.TeacherCreationDto;
import ru.dudkomv.neoflexpractice.teacher.dto.TeacherDto;
import ru.dudkomv.neoflexpractice.teacher.dto.TeacherUpdateDto;
import ru.dudkomv.neoflexpractice.user.UserRepository;

import java.time.LocalDate;
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
class TeacherControllerTest extends ApplicationIntegrationTest {
    private static final String URL_PREFIX = "/teacher";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    @Autowired
    private TeacherService teacherService;

    @Autowired
    private UserRepository userRepository;
    
    @SpyBean
    private TeacherController teacherController;
    
    @BeforeEach
    void setUp() {
        Mockito.reset(teacherController);
    }

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
    void create_valid() throws Exception {
        assertFalse(teacherRepository.existsById(2L));
        assertFalse(userRepository.existsById(2L));

        TeacherCreationDto creationDto = TeacherCreationDto.builder()
                .username("username")
                .password("password")
                .firstName("Name")
                .lastName("Surname")
                .secondName("Second Name")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        TeacherDto updateDto = TeacherDto.builder()
                .id(1L)
                .userId(2L)
                .firstName("Name")
                .lastName("Surname")
                .secondName("Second Name")
                .birthday(LocalDate.of(2000, 1, 1))
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

        assertTrue(teacherRepository.existsById(1L));
        assertTrue(userRepository.existsById(2L));
        assertEquals(updateDto, teacherService.getById(1L));
        verify(teacherController, times(1)).create(creationDto);
    }

    @Test
    void create_invalid() throws Exception {
        TeacherCreationDto creationDto = TeacherCreationDto.builder()
                .username("")
                .password("")
                .firstName("")
                .lastName("")
                .secondName(null)
                .birthday(LocalDate.MAX)
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
                "birthday: Teacher's birthday must be in the past",
                "firstName: Teacher's first name must not be blank",
                "lastName: Teacher's last name must not be blank",
                "password: User password must be greater than 4 characters",
                "password: User password must not be blank",
                "secondName: Teacher's last name must not be null (use empty string instead)",
                "username: User name must not be blank"
        );

        ExceptionControllerAdvice.ApiError apiError = toApiError(objectMapper, response);
        List<String> actualErrors = toSortedListOfErrors(apiError);

        assertEquals(expectedErrors, actualErrors);
        assertNull(apiError.getMessage());
        verify(teacherController, times(0)).create(any());
    }

    @Test
    void getForUser() throws Exception {
        assertTrue(teacherRepository.existsById(1L));

        TeacherDto updateDto = TeacherDto.builder()
                .id(0L)
                .userId(0L)
                .firstName("Admin")
                .lastName("Admin")
                .secondName("Admin")
                .birthday(LocalDate.of(1970, 1, 1))
                .build();

        String expectedAnswer = objectMapper.writeValueAsString(updateDto);
        mockMvc.perform(get(URL_PREFIX))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedAnswer));

        verify(teacherController, times(1)).getForUser(any());
    }

    @Test
    void getAll() throws Exception {
        TeacherDto adminDto = TeacherDto.builder()
                .id(0L)
                .userId(0L)
                .firstName("Admin")
                .lastName("Admin")
                .secondName("Admin")
                .birthday(LocalDate.of(1970, 1, 1))
                .build();

        TeacherDto teacherDto = TeacherDto.builder()
                .id(1L)
                .userId(1L)
                .firstName("Name")
                .lastName("Surname")
                .secondName("Second-Name")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        String expectedAnswer = objectMapper.writeValueAsString(List.of(adminDto, teacherDto));
        mockMvc.perform(get(URL_PREFIX + "/all"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedAnswer));

        verify(teacherController, times(1)).getAll();
    }

    @Test
    void getById_teacherExist() throws Exception {
        Long expectedId = 1L;
        TeacherDto expectedDto = teacherService.getById(expectedId);

        String expectedResponse = objectMapper.writeValueAsString(expectedDto);
        mockMvc.perform(get(URL_PREFIX + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));

        verify(teacherController, times(1)).getById(expectedId);
    }

    @Test
    void getById_teacherNotExist() throws Exception {
        String expectedMessage = "Teacher entity not found with id=-1";
        ExceptionControllerAdvice.ApiError expectedError = ExceptionControllerAdvice.ApiError.builder()
                .message(expectedMessage)
                .build();

        String expectedResponse = objectMapper.writeValueAsString(expectedError);
        mockMvc.perform(get(URL_PREFIX + "/-1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(expectedResponse));

        verify(teacherController, times(1)).getById(-1L);
    }

    @Test
    void update_valid() throws Exception {
        assertTrue(teacherRepository.existsById(1L));

        TeacherUpdateDto updateDto = TeacherUpdateDto.builder()
                .id(1L)
                .username("username")
                .password("password")
                .firstName("Name")
                .lastName("Surname")
                .secondName("Second Name")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        TeacherDto expected = TeacherDto.builder()
                .id(1L)
                .userId(1L)
                .firstName("Name")
                .lastName("Surname")
                .secondName("Second Name")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        String request = objectMapper.writeValueAsString(updateDto);
        String expectedAnswer = objectMapper.writeValueAsString(expected);

        mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(expectedAnswer));

        TeacherDto actualTeacher = teacherService.getById(1L);
        assertEquals(expected, actualTeacher);
        verify(teacherController, times(1)).update(updateDto);
    }

    @Test
    void update_invalid() throws Exception {
        assertTrue(teacherRepository.existsById(1L));
        TeacherDto initialTeacherDto = teacherService.getById(1L);

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
        MvcResult response = mockMvc.perform(
                        put(URL_PREFIX)
                                .contentType(APPLICATION_JSON)
                                .content(invalidRequest)
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        List<String> expectedErrors = List.of(
                "birthday: Teacher's birthday must be in the past",
                "firstName: Teacher's first name must not be blank",
                "lastName: Teacher's last name must not be blank",
                "password: User password must be greater than 4 characters",
                "password: User password must not be blank",
                "secondName: Teacher's last name must not be null (use empty string instead)",
                "username: User name must not be blank"
        );

        ExceptionControllerAdvice.ApiError apiError = toApiError(objectMapper, response);
        List<String> actualErrors = toSortedListOfErrors(apiError);

        assertEquals(expectedErrors, actualErrors);
        assertNull(apiError.getMessage());

        assertEquals(initialTeacherDto, teacherService.getById(1L));
        verify(teacherController, times(0)).update(updateDto);
    }

    @Test
    void update_nonExistent() throws Exception {
        assertFalse(teacherRepository.existsById(-1L));

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
        String expectedMessage = "Teacher entity not found with id=-1";
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

        verify(teacherController, times(0)).update(updateDto);
        assertFalse(teacherRepository.existsById(-1L));
    }

    @Test
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            truncate table courses restart identity cascade;
            """)
    void deleteById_valid() throws Exception {
        assertTrue(teacherRepository.existsById(1L));

        mockMvc.perform(delete(URL_PREFIX + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        assertFalse(teacherRepository.existsById(1L));
        verify(teacherController, times(1)).deleteById(1L);
    }

    @Test
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            truncate table courses restart identity cascade;
            """)
    void deleteById_nonExistent() throws Exception {
        assertFalse(teacherRepository.existsById(-1L));

        String expectedMessage = "Teacher entity not found with id=-1";
        ExceptionControllerAdvice.ApiError expectedError = ExceptionControllerAdvice.ApiError.builder()
                .message(expectedMessage)
                .build();

        String expectedResponse = objectMapper.writeValueAsString(expectedError);
        mockMvc.perform(delete(URL_PREFIX + "/-1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(expectedResponse));

        assertFalse(teacherRepository.existsById(-1L));
        verify(teacherController, times(1)).deleteById(-1L);
    }
}