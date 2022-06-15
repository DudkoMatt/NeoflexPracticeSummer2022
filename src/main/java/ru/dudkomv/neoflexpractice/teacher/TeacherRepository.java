package ru.dudkomv.neoflexpractice.teacher;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends CrudRepository<Teacher, Long> {
    Optional<Teacher> findByUserId(Long userId);

    @Override
    List<Teacher> findAll();
}
