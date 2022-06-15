package ru.dudkomv.neoflexpractice.course.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.dudkomv.neoflexpractice.course.CourseService;
import ru.dudkomv.neoflexpractice.course.dto.CourseCreationDto;
import ru.dudkomv.neoflexpractice.course.dto.CourseUpdateDto;
import ru.dudkomv.neoflexpractice.teacher.TeacherService;
import ru.dudkomv.neoflexpractice.user.UserService;
import ru.dudkomv.neoflexpractice.user.security.PermissionEvaluator;

import java.util.Objects;

@Component("CoursePermissionEvaluator")
public class CoursePermissionEvaluator extends PermissionEvaluator {
    private final CourseService courseService;

    public CoursePermissionEvaluator(UserService userService, TeacherService teacherService, CourseService courseService) {
        super(userService, teacherService);
        this.courseService = courseService;
    }

    public boolean canCreate(CourseCreationDto creationDto, UserDetails userDetails) {
        if (injectIfNullTeacherIdTo(creationDto::getCuratorId, creationDto::setCuratorId)) {  // Inject dynamically
            return true;
        }

        Long expectedId = userService.getIdByTeacherId(creationDto.getCuratorId());
        return checkAccess(userDetails, userId -> Objects.equals(expectedId, userId));
    }

    public boolean canView(Long courseId, UserDetails userDetails) {
        CourseUpdateDto courseUpdateDto = courseService.getById(courseId);
        return canCreate(courseUpdateDto, userDetails);
    }

    public boolean canUpdate(CourseUpdateDto updateDto, UserDetails userDetails) {
        return canCreate(updateDto, userDetails);
    }

    public boolean canDelete(Long courseId, UserDetails userDetails) {
        return canView(courseId, userDetails);
    }
}
