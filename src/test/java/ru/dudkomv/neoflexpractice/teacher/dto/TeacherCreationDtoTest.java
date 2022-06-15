package ru.dudkomv.neoflexpractice.teacher.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dudkomv.neoflexpractice.teacher.Teacher;
import ru.dudkomv.neoflexpractice.teacher.mapper.TeacherMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TeacherCreationDtoTest {
    @Mock
    private TeacherMapper teacherMapper;

    @Test
    void acceptTeacherMapper() {
        TeacherCreationDto creationDto = TeacherCreationDto.builder().build();
        Teacher expected = new Teacher();

        Mockito.when(teacherMapper.toEntity(creationDto)).thenReturn(expected);

        Teacher actual = creationDto.acceptTeacherMapper(teacherMapper);
        assertEquals(actual, expected);
    }
}