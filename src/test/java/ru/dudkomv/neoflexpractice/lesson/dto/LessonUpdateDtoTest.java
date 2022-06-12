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
class LessonUpdateDtoTest {
    @Mock
    private LessonMapper lessonMapper;

    @Test
    void acceptLessonMapper() {
        LessonUpdateDto updateDto = LessonUpdateDto.builder().build();
        Lesson expected = new Lesson();

        Mockito.when(lessonMapper.toEntity(updateDto)).thenReturn(expected);

        Lesson actual = updateDto.acceptLessonMapper(lessonMapper);
        assertEquals(actual, expected);
    }
}