package ru.dudkomv.neoflexpractice.lesson;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dudkomv.neoflexpractice.course.CourseService;
import ru.dudkomv.neoflexpractice.exception.LessonEntityNotFoundException;
import ru.dudkomv.neoflexpractice.lesson.dto.LessonCreationDto;
import ru.dudkomv.neoflexpractice.lesson.dto.LessonUpdateDto;
import ru.dudkomv.neoflexpractice.lesson.mapper.LessonMapper;
import ru.dudkomv.neoflexpractice.teacher.TeacherService;
import ru.dudkomv.neoflexpractice.user.UserService;

import java.util.List;

@Service
public class LessonService {
    private final LessonMapper lessonMapper;
    private final LessonRepository lessonRepository;
    private final UserService userService;
    private final TeacherService teacherService;
    private final CourseService courseService;

    public LessonService(LessonMapper lessonMapper,
                         LessonRepository lessonRepository,
                         UserService userService,
                         TeacherService teacherService,
                         @Lazy CourseService courseService) {
        this.lessonMapper = lessonMapper;
        this.lessonRepository = lessonRepository;
        this.userService = userService;
        this.teacherService = teacherService;
        this.courseService = courseService;
    }

    @Transactional
    public LessonUpdateDto create(LessonCreationDto creationDto) {
        courseService.validateExists(creationDto.getCourseId());
        return lessonMapper.fromEntity(
                lessonRepository.save(
                        creationDto.acceptLessonMapper(lessonMapper)
                )
        );
    }

    @Transactional
    public List<LessonUpdateDto> getAllForUser(String username) {
        Long userId = userService.getIdByUsername(username);
        return getAllForUser(userId);
    }

    @Transactional
    public List<LessonUpdateDto> getAllForUser(Long userId) {
        Long teacherId = teacherService.getByUserId(userId).getId();

        return lessonRepository
                .findLessonsByTeacherIdOrderByStartDateTimeAsc(teacherId)
                .stream()
                .map(lessonMapper::fromEntity)
                .toList();
    }

    public List<LessonUpdateDto> getAll() {
        return lessonRepository
                .findAll()
                .stream()
                .map(lessonMapper::fromEntity)
                .toList();
    }

    public LessonUpdateDto getById(Long id) {
        if (id == null) {
            return null;
        }

        return lessonMapper.fromEntity(
                lessonRepository
                        .findById(id)
                        .orElseThrow(() -> new LessonEntityNotFoundException(id))
        );
    }

    @Transactional
    public LessonUpdateDto update(LessonUpdateDto updateDto) {
        validateExists(updateDto.getId());
        return create(updateDto);
    }

    @Transactional
    public void deleteById(Long id) {
        validateExists(id);
        lessonRepository.deleteById(id);
    }

    public List<LessonUpdateDto> getLessonsByCourseIdOrderByStartDateTimeAsc(Long courseId) {
        return lessonRepository
                .findLessonsByCourseIdOrderByStartDateTimeAsc(courseId)
                .stream()
                .map(lessonMapper::fromEntity)
                .toList();
    }

    public void cloneLessons(Long newTeacherId, Long oldCourseId, Long newCourseId) {
        lessonRepository.cloneLessons(newTeacherId, oldCourseId, newCourseId);
    }

    private void validateExists(Long id) {
        if (!lessonRepository.existsById(id)) {
            throw new LessonEntityNotFoundException(id);
        }
    }
}
