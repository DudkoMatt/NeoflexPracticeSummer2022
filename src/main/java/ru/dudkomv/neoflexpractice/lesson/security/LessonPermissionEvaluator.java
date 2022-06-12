package ru.dudkomv.neoflexpractice.lesson.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.dudkomv.neoflexpractice.course.CourseService;
import ru.dudkomv.neoflexpractice.lesson.LessonService;
import ru.dudkomv.neoflexpractice.lesson.dto.LessonCreationDto;
import ru.dudkomv.neoflexpractice.lesson.dto.LessonUpdateDto;
import ru.dudkomv.neoflexpractice.teacher.TeacherService;
import ru.dudkomv.neoflexpractice.user.UserService;
import ru.dudkomv.neoflexpractice.user.security.PermissionEvaluator;

import java.util.Objects;

@Component("LessonPermissionEvaluator")
public class LessonPermissionEvaluator extends PermissionEvaluator {
    private final LessonService lessonService;
    private final CourseService courseService;

    public LessonPermissionEvaluator(UserService userService, TeacherService teacherService, LessonService lessonService, CourseService courseService) {
        super(userService, teacherService);
        this.lessonService = lessonService;
        this.courseService = courseService;
    }

    public boolean canCreate(LessonCreationDto creationDto, UserDetails userDetails) {
        injectIfNullTeacherIdTo(creationDto::getTeacherId, creationDto::setTeacherId);  // Inject dynamically

        Long expectedIdByTeacherId = userService.getIdByTeacherId(creationDto.getTeacherId());
        Long courseCuratorId = courseService.getById(creationDto.getCourseId()).getCuratorId();
        Long expectedIdByCuratorId = userService.getIdByTeacherId(courseCuratorId);
        return checkAccess(
                userDetails,
                userId -> Objects.equals(expectedIdByTeacherId, userId) && Objects.equals(expectedIdByCuratorId, userId)
        );
    }

    public boolean canView(Long lessonId, UserDetails userDetails) {
        return canCreate(lessonService.getById(lessonId), userDetails);
    }

    public boolean canUpdate(LessonUpdateDto updateDto, UserDetails userDetails) {
        return canCreate(updateDto, userDetails);
    }

    public boolean canDelete(Long lessonId, UserDetails userDetails) {
        return canView(lessonId, userDetails);
    }
}
