package ru.dudkomv.neoflexpractice.course;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends CrudRepository<Course, Long> {
    @Query("""
            with recursive cte as (
                select
                    c.*,
                    cc.parent_id
                from
                    courses as c
                inner join
                        course_category cc on cc.id = c.category_id
                where
                    c.category_id = :category_id
            
                union
            
                select
                    c.*,
                    cc.parent_id
                from
                    courses as c
                inner join
                        course_category cc on cc.id = c.category_id
                inner join
                        cte as cte on cte.category_id = cc.parent_id
            )
            select *
            from cte;
            """)
    List<Course> findCoursesByCategoryIdRecursively(@Param("category_id") Long categoryId);

    @Query("""
            select *
            from courses
            where type->>'type' = :course_type;
            """)
    List<Course> findCoursesByType(@Param("course_type") String courseType);

    List<Course> findCoursesByCuratorId(Long curatorId);

    @Override
    List<Course> findAll();
}
