package ru.dudkomv.neoflexpractice.lesson;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LessonRepository extends CrudRepository<Lesson, Long> {
    List<Lesson> findLessonsByCourseIdOrderByStartDateTimeAsc(Long courseId);

    List<Lesson> findLessonsByTeacherIdOrderByStartDateTimeAsc(Long teacherId);

    @Query("""
            select
                *
            from
                lessons as l
            where
                    l.course_id = :course_id
                and
                    l.start_datetime is not null
                and
                    l.end_datetime is not null
                and
                    l.end_datetime > :after_date_time
                and
                    (:update_id is null or l.id != :update_id)
            order by
                l.start_datetime
            limit 1;
            """)
    Lesson findFirstByCourseIdAndEndDateTimeAfter(@Param("course_id") Long courseId,
                                                  @Param("after_date_time") LocalDateTime afterDateTime,
                                                  @Param("update_id") Long updateId);

    @Query("""
            select
                *
            from
                lessons as l
            where
                    l.teacher_id = :teacher_id
                and
                    l.start_datetime is not null
                and
                    l.end_datetime is not null
                and
                    l.end_datetime > :after_date_time
                and
                    (:update_id is null or l.id != :update_id)
            order by
                l.start_datetime
            limit 1;
            """)
    Lesson findFirstByTeacherIdAndEndDateTimeAfter(@Param("teacher_id") Long teacherId,
                                                   @Param("after_date_time") LocalDateTime afterDateTime,
                                                   @Param("update_id") Long updateId);

    @Modifying
    @Query("""
            insert into lessons (
                teacher_id,
                course_id,
                title,
                description,
                start_datetime,
                end_datetime
            )
            select
                :new_teacher_id,
                :new_course_id,
                old.title,
                old.description,
                null,
                null
            from
                lessons as old
            where
                old.course_id = :old_course_id;
            """)
    void cloneLessons(@Param("new_teacher_id") Long newTeacherId,
                      @Param("old_course_id") Long oldCourseId,
                      @Param("new_course_id") Long newCourseId);

    @Override
    List<Lesson> findAll();
}
