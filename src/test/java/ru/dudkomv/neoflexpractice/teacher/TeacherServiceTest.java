package ru.dudkomv.neoflexpractice.teacher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dudkomv.neoflexpractice.exception.TeacherEntityNotFoundException;
import ru.dudkomv.neoflexpractice.teacher.dto.TeacherCreationDto;
import ru.dudkomv.neoflexpractice.teacher.dto.TeacherDto;
import ru.dudkomv.neoflexpractice.teacher.dto.TeacherUpdateDto;
import ru.dudkomv.neoflexpractice.teacher.mapper.TeacherMapper;
import ru.dudkomv.neoflexpractice.user.User;
import ru.dudkomv.neoflexpractice.user.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {
    @Mock
    private TeacherMapper teacherMapper;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private UserService userService;

    private TeacherService teacherService;

    @BeforeEach
    void setUp() {
        teacherService = new TeacherService(teacherMapper, teacherRepository, userService);
        teacherService = Mockito.spy(teacherService);
    }

    @Test
    void create() {
        String username = "SomeUsername";
        String password = "Password";
        Long teacherId = 1L;
        User user = new User(username, password);
        TeacherCreationDto creationDto = new TeacherCreationDto();
        creationDto = Mockito.spy(creationDto);

        TeacherDto teacherDto = Mockito.mock(TeacherDto.class);
        Teacher teacher = Mockito.mock(Teacher.class);

        Mockito.when(teacherMapper.toEntity(creationDto)).thenReturn(teacher);
        Mockito.when(teacher.getId()).thenReturn(teacherId);

        Mockito.doReturn(username).when(creationDto).getUsername();
        Mockito.doReturn(password).when(creationDto).getPassword();

        Mockito.when(userService.registerTeacher(user, teacherId)).thenReturn(user);

        Mockito.when(teacherRepository.save(teacher)).thenReturn(teacher);
        Mockito.when(teacherMapper.fromEntity(teacher)).thenReturn(teacherDto);

        TeacherDto actual = teacherService.create(creationDto);
        assertEquals(teacherDto, actual);
    }

    @Test
    void getAll() {
        TeacherDto teacherDto = Mockito.mock(TeacherDto.class);
        Teacher teacher = Mockito.mock(Teacher.class);

        Mockito.when(teacherRepository.findAll()).thenReturn(List.of(teacher));
        Mockito.when(teacherMapper.fromEntity(teacher)).thenReturn(teacherDto);

        List<TeacherDto> expected = List.of(teacherDto);
        List<TeacherDto> actual = teacherService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void getById_null() {
        assertNull(teacherService.getById(null));
        Mockito.verify(teacherRepository, Mockito.times(0)).findById(Mockito.anyLong());
    }

    @Test
    void getById_nonNull_existing() {
        Long teacherId = 1L;
        Teacher teacher = Mockito.mock(Teacher.class);
        TeacherDto updateDto = Mockito.mock(TeacherDto.class);

        Mockito.when(teacherMapper.fromEntity(teacher)).thenReturn(updateDto);
        Mockito.when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));

        TeacherDto actual = teacherService.getById(teacherId);
        assertEquals(updateDto, actual);
    }

    @Test
    void getById_nonNull_nonExisting() {
        Long teacherId = 1L;

        Mockito.when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        Exception actual = assertThrows(TeacherEntityNotFoundException.class, () -> teacherService.getById(teacherId));
        assertEquals("Teacher entity not found with id=" + teacherId, actual.getMessage());
    }

    @Test
    void getForUser() {
        String username = "SomeUsername";
        Long userId = 1L;
        TeacherDto teacherDto = Mockito.mock(TeacherDto.class);

        Mockito.when(userService.getIdByUsername(username)).thenReturn(userId);
        Mockito.doReturn(teacherDto).when(teacherService).getByUserId(userId);

        TeacherDto actual = teacherService.getForUser(username);
        assertEquals(teacherDto, actual);
    }

    @Test
    void getByUserId() {
        Long userId = 1L;
        Teacher teacher = Teacher.of(1L, 1L, "F", "L", "S", LocalDate.of(1970, 1, 1));
        Optional<Teacher> teacherOptional = Optional.of(teacher);
        TeacherDto teacherDto = Mockito.mock(TeacherDto.class);

        Mockito.when(teacherRepository.findByUserId(userId)).thenReturn(teacherOptional);
        Mockito.when(teacherMapper.fromEntity(teacher)).thenReturn(teacherDto);

        TeacherDto actual = teacherService.getByUserId(userId);
        assertEquals(teacherDto, actual);
    }

    @Test
    void update_existing() {
        Long teacherId = 1L;
        TeacherUpdateDto updateDto = Mockito.mock(TeacherUpdateDto.class);
        TeacherDto teacherDto = Mockito.mock(TeacherDto.class);

        Mockito.when(updateDto.getId()).thenReturn(teacherId);
        Mockito.when(teacherRepository.existsById(teacherId)).thenReturn(true);

        Mockito.doReturn(teacherDto).when(teacherService).create(updateDto);

        TeacherDto actual = teacherService.update(updateDto);
        assertEquals(teacherDto, actual);
        Mockito.verify(teacherService, Mockito.times(1)).create(updateDto);
    }

    @Test
    void update_nonExisting() {
        Long teacherId = 1L;
        TeacherUpdateDto updateDto = Mockito.mock(TeacherUpdateDto.class);

        Mockito.when(updateDto.getId()).thenReturn(teacherId);

        Exception actual = assertThrows(TeacherEntityNotFoundException.class, () -> teacherService.update(updateDto));
        assertEquals("Teacher entity not found with id=" + teacherId, actual.getMessage());
    }

    @Test
    void deleteById_existing() {
        Long teacherId = 1L;

        Mockito.when(teacherRepository.existsById(teacherId)).thenReturn(true);
        assertDoesNotThrow(() -> teacherService.deleteById(teacherId));
        Mockito.verify(teacherRepository, Mockito.times(1)).deleteById(teacherId);
    }

    @Test
    void deleteById_nonExisting() {
        Long teacherId = 1L;

        Exception actual = assertThrows(TeacherEntityNotFoundException.class, () -> teacherService.deleteById(teacherId));
        assertEquals("Teacher entity not found with id=" + teacherId, actual.getMessage());
    }

    @Test
    void validateExists() {
        Long categoryId = 1L;

        Exception actual = assertThrows(TeacherEntityNotFoundException.class, () -> teacherService.validateExists(categoryId));
        assertEquals("Teacher entity not found with id=" + categoryId, actual.getMessage());

        Mockito.when(teacherRepository.existsById(categoryId)).thenReturn(true);
        assertDoesNotThrow(() -> teacherService.validateExists(categoryId));
    }
}