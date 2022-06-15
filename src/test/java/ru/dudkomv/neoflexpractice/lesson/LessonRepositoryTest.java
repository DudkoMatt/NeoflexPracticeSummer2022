package ru.dudkomv.neoflexpractice.lesson;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import ru.dudkomv.neoflexpractice.ApplicationIntegrationTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LessonRepositoryTest extends ApplicationIntegrationTest {
    @Autowired
    private LessonRepository lessonRepository;

    @AfterEach
    void tearDown() {
        cleanAndMigrate();
    }

    @Test
    void findFirstByCourseIdAndEndDateTimeAfter() {
        Long courseId = 3L;
        LocalDateTime time0 = LocalDateTime.of(2022, 5, 19, 8, 0,0);
        LocalDateTime time1 = LocalDateTime.of(2022, 5, 19, 9, 0,0);
        LocalDateTime time2 = LocalDateTime.of(2022, 5, 19, 10, 0,0);

        Lesson lesson1 = Lesson.of(1L, "First lesson", "First lesson description", 1L, 3L, time0, time1);
        Lesson lesson2 = Lesson.of(2L, "Second lesson", "Second lesson description", 1L, 3L, time1, time2);

        Lesson actualLesson1 = lessonRepository.findFirstByCourseIdAndEndDateTimeAfter(courseId, time0, null);
        Lesson actualLesson2 = lessonRepository.findFirstByCourseIdAndEndDateTimeAfter(courseId, time1, null);
        Lesson actualLesson3 = lessonRepository.findFirstByCourseIdAndEndDateTimeAfter(courseId, time2, null);

        Lesson actualLesson4 = lessonRepository.findFirstByCourseIdAndEndDateTimeAfter(courseId, time0, 1L);

        assertEquals(lesson1, actualLesson1);
        assertEquals(lesson2, actualLesson2);
        assertEquals(lesson2, actualLesson4);
        assertNull(actualLesson3);
    }

    @Test
    void findFirstByTeacherIdAndEndDateTimeAfter() {
        Long teacherId = 1L;
        LocalDateTime time0 = LocalDateTime.of(2022, 5, 19, 8, 0,0);
        LocalDateTime time1 = LocalDateTime.of(2022, 5, 19, 9, 0,0);
        LocalDateTime time2 = LocalDateTime.of(2022, 5, 19, 10, 0,0);
        LocalDateTime time3 = LocalDateTime.of(2022, 5, 19, 11, 0,0);

        Lesson lesson1 = Lesson.of(1L, "First lesson", "First lesson description", 1L, 3L, time0, time1);
        Lesson lesson2 = Lesson.of(2L, "Second lesson", "Second lesson description", 1L, 3L, time1, time2);
        Lesson lesson3 = Lesson.of(3L, "Another lesson", "Another lesson description", 1L, 4L, time2, time3);

        Lesson actualLesson1 = lessonRepository.findFirstByTeacherIdAndEndDateTimeAfter(teacherId, time0, null);
        Lesson actualLesson2 = lessonRepository.findFirstByTeacherIdAndEndDateTimeAfter(teacherId, time1, null);
        Lesson actualLesson3 = lessonRepository.findFirstByTeacherIdAndEndDateTimeAfter(teacherId, time2, null);
        Lesson actualLesson4 = lessonRepository.findFirstByTeacherIdAndEndDateTimeAfter(teacherId, time3, null);

        Lesson actualLesson5 = lessonRepository.findFirstByTeacherIdAndEndDateTimeAfter(teacherId, time0, 1L);

        assertEquals(lesson1, actualLesson1);
        assertEquals(lesson2, actualLesson2);
        assertEquals(lesson3, actualLesson3);
        assertEquals(lesson2, actualLesson5);
        assertNull(actualLesson4);
    }

    @Test
    @Sql(statements = """
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (0, 'Test', 'Test', 0, 0, 0, '{"type": "base"}');
            """)
    void cloneLessons() {
        Long newLessonId = 4L;
        Long newTeacherId = 0L;
        Long oldCourseId = 4L;
        Long newCourseId = 0L;

        assertFalse(lessonRepository.existsById(newLessonId));
        lessonRepository.cloneLessons(newTeacherId, oldCourseId, newCourseId);

        Lesson expectedNewLesson = Lesson.of(newLessonId, "Another lesson", "Another lesson description", newTeacherId, newCourseId, null, null);
        List<Lesson> lessons = lessonRepository.findAll();

        assertTrue(lessons.contains(expectedNewLesson));
    }
}