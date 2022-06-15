package ru.dudkomv.neoflexpractice.lesson.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dudkomv.neoflexpractice.lesson.Lesson;
import ru.dudkomv.neoflexpractice.lesson.mapper.LessonMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class LessonCreationDtoTest {
    @Mock
    private LessonMapper lessonMapper;

    @Test
    void acceptLessonMapper() {
        LessonCreationDto creationDto = LessonCreationDto.builder().build();
        Lesson expected = new Lesson();

        Mockito.when(lessonMapper.toEntity(creationDto)).thenReturn(expected);

        Lesson actual = creationDto.acceptLessonMapper(lessonMapper);
        assertEquals(actual, expected);
    }
}