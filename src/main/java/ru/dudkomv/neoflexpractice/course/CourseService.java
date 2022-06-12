package ru.dudkomv.neoflexpractice.course;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dudkomv.neoflexpractice.course.dto.CourseCreationDto;
import ru.dudkomv.neoflexpractice.course.dto.CourseUpdateDto;
import ru.dudkomv.neoflexpractice.course.mapper.CourseMapper;
import ru.dudkomv.neoflexpractice.coursecategory.CourseCategoryService;
import ru.dudkomv.neoflexpractice.exception.CourseEntityNotFoundException;
import ru.dudkomv.neoflexpractice.lesson.LessonService;
import ru.dudkomv.neoflexpractice.lesson.dto.LessonUpdateDto;
import ru.dudkomv.neoflexpractice.teacher.TeacherService;
import ru.dudkomv.neoflexpractice.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final UserService userService;
    private final CourseMapper courseMapper;
    private final CourseRepository courseRepository;
    private final TeacherService teacherService;
    private final LessonService lessonService;
    private final CourseCategoryService courseCategoryService;

    public CourseUpdateDto create(CourseCreationDto creationDto) {
        return courseMapper.fromEntity(
                courseRepository.save(
                        creationDto.acceptCourseMapper(courseMapper)
                )
        );
    }

    public List<CourseUpdateDto> getAll() {
        return courseRepository
                .findAll()
                .stream()
                .map(courseMapper::fromEntity)
                .toList();
    }

    @Transactional
    public List<CourseUpdateDto> getAllForUser(String username) {
        Long userId = userService.getIdByUsername(username);
        Long teacherId = teacherService.getByUserId(userId).getId();

        return courseRepository
                .findCoursesByCuratorId(teacherId)
                .stream()
                .map(courseMapper::fromEntity)
                .toList();
    }

    public CourseUpdateDto getById(Long id) {
        if (id == null) {
            return null;
        }

        return courseMapper.fromEntity(
                courseRepository
                        .findById(id)
                        .orElseThrow(() -> new CourseEntityNotFoundException(id))
        );
    }

    @Transactional
    public List<LessonUpdateDto> getLessonsById(Long id) {
        validateExists(id);
        return lessonService.getLessonsByCourseIdOrderByStartDateTimeAsc(id);
    }

    @Transactional
    public CourseUpdateDto update(CourseUpdateDto updateDto) {
        validateExists(updateDto.getId());
        return create(updateDto);
    }

    @Transactional
    public void deleteById(Long id) {
        validateExists(id);
        courseRepository.deleteById(id);
    }

    @Transactional
    public List<CourseUpdateDto> getCoursesByCategoryIdRecursively(Long categoryId) {
        courseCategoryService.validateExists(categoryId);
        return courseRepository
                .findCoursesByCategoryIdRecursively(categoryId)
                .stream()
                .map(courseMapper::fromEntity)
                .toList();
    }

    public List<CourseUpdateDto> getCoursesByType(String courseType) {
        return courseRepository
                .findCoursesByType(courseType)
                .stream()
                .map(courseMapper::fromEntity)
                .toList();
    }

    @Transactional
    public List<CourseUpdateDto> getCoursesByCuratorId(Long curatorId) {
        teacherService.validateExists(curatorId);
        return courseRepository
                .findCoursesByCuratorId(curatorId)
                .stream()
                .map(courseMapper::fromEntity)
                .toList();
    }

    @Transactional
    public CourseUpdateDto cloneCourse(Long id, String username) {
        validateExists(id);

        Long userId = userService.getIdByUsername(username);
        Long teacherId = teacherService.getByUserId(userId).getId();

        CourseUpdateDto updateDto = getById(id);
        CourseCreationDto creationDto = courseMapper.toCreation(updateDto);
        creationDto.setCuratorId(teacherId);

        CourseUpdateDto clonedCourseUpdateDto = create(creationDto);

        lessonService.cloneLessons(teacherId, id, clonedCourseUpdateDto.getId());

        return clonedCourseUpdateDto;
    }

    public void validateExists(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new CourseEntityNotFoundException(id);
        }
    }
}
