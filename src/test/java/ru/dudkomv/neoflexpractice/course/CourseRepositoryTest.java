package ru.dudkomv.neoflexpractice.course;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import ru.dudkomv.neoflexpractice.ApplicationIntegrationTest;
import ru.dudkomv.neoflexpractice.course.type.BaseCourseType;
import ru.dudkomv.neoflexpractice.course.type.OfflineCourseType;
import ru.dudkomv.neoflexpractice.course.type.OnlineCourseType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CourseRepositoryTest extends ApplicationIntegrationTest {
    @Autowired
    private CourseRepository courseRepository;

    @AfterEach
    void tearDown() {
        cleanAndMigrate();
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    void findCoursesByCategoryIdRecursively() {
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

        List<Course> actualBaseCategory = courseRepository.findCoursesByCategoryIdRecursively(defaultCategoryId);
        List<Course> actualProgrammingCategory = courseRepository.findCoursesByCategoryIdRecursively(programmingCategoryId);
        List<Course> actualJavaCategory = courseRepository.findCoursesByCategoryIdRecursively(javaCategoryId);
        List<Course> actualPythonCategory = courseRepository.findCoursesByCategoryIdRecursively(pythonCategoryId);
        List<Course> actualDataScienceCategory = courseRepository.findCoursesByCategoryIdRecursively(dataScienceCategoryId);
        List<Course> actualPythonMLCategory = courseRepository.findCoursesByCategoryIdRecursively(pythonMLCategoryId);

        actualBaseCategory.sort((c1, c2) -> (int) (c1.getId() - c2.getId()));
        actualProgrammingCategory.sort((c1, c2) -> (int) (c1.getId() - c2.getId()));
        actualJavaCategory.sort((c1, c2) -> (int) (c1.getId() - c2.getId()));
        actualPythonCategory.sort((c1, c2) -> (int) (c1.getId() - c2.getId()));
        actualDataScienceCategory.sort((c1, c2) -> (int) (c1.getId() - c2.getId()));
        actualPythonMLCategory.sort((c1, c2) -> (int) (c1.getId() - c2.getId()));

        assertEquals(baseCategory, actualBaseCategory);
        assertEquals(programmingCategory, actualProgrammingCategory);
        assertEquals(javaCategory, actualJavaCategory);
        assertEquals(pythonCategory, actualPythonCategory);
        assertEquals(dataScienceCategory, actualDataScienceCategory);
        assertEquals(pythonMLCategory, actualPythonMLCategory);
    }

    @Test
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            truncate table courses restart identity cascade;
            
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (1, 'Python ML Course', 'Python ML Course', 5, 1, 0, '{"type": "online", "lessonUrl": "http://some.url"}');
            
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (2, 'Python Course', 'Python Course', 3, 1, 0, '{"type": "base"}');
            
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (3, 'Data Science Course', 'Data Science Course', 4, 1, 0, '{"type": "offline", "universityName": "ITMO", "address": "Kronverksky Pr. 49"}');
            """)
    void findCoursesByType() {
        Course onlineCourse = Course.builder()
                .id(1L)
                .title("Python ML Course")
                .description("Python ML Course")
                .categoryId(5L)
                .curatorId(1L)
                .studentCount(0L)
                .type(new OnlineCourseType("http://some.url"))
                .build();

        List<Course> expectedOnlineCourses = List.of(onlineCourse);
        List<Course> actualOnlineCourses = courseRepository.findCoursesByType(OnlineCourseType.CLASS_TYPE_NAME);

        assertEquals(expectedOnlineCourses, actualOnlineCourses);
    }

    @Test
    @Sql(statements = """
            truncate table lessons restart identity cascade;
            truncate table courses restart identity cascade;
            
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (1, 'Additional Admin Course', 'Additional Admin Course', 0, 0, 0, '{"type": "base"}');
            
            insert into courses(id, title, description, category_id, curator_id, student_count, type)
                values (2, 'Base Course', 'Base Course', 0, 1, 0, '{"type": "base"}');
            """)
    void findCoursesByCuratorId() {
        Course course1 = Course.of(1L, "Additional Admin Course", "Additional Admin Course", 0L, 0L, 0L, new BaseCourseType());
        Course course2 = Course.of(2L, "Base Course", "Base Course", 0L, 1L, 0L, new BaseCourseType());

        Long adminId = 0L;
        Long teacherId = 1L;

        List<Course> adminCourses = List.of(course1);
        List<Course> teacherCourses = List.of(course2);

        List<Course> actualAdminCourses = courseRepository.findCoursesByCuratorId(adminId);
        List<Course> actualTeacherCourses = courseRepository.findCoursesByCuratorId(teacherId);

        assertEquals(adminCourses, actualAdminCourses);
        assertEquals(teacherCourses, actualTeacherCourses);
    }
}