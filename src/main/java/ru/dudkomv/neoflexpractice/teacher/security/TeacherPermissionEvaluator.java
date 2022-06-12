package ru.dudkomv.neoflexpractice.teacher.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.dudkomv.neoflexpractice.teacher.TeacherService;
import ru.dudkomv.neoflexpractice.teacher.dto.TeacherUpdateDto;
import ru.dudkomv.neoflexpractice.user.UserService;
import ru.dudkomv.neoflexpractice.user.security.PermissionEvaluator;

import java.util.Objects;

@Component("TeacherPermissionEvaluator")
public class TeacherPermissionEvaluator extends PermissionEvaluator {
    public TeacherPermissionEvaluator(UserService userService, TeacherService teacherService) {
        super(userService, teacherService);
    }

    public boolean canView(Long teacherId, UserDetails userDetails) {
        Long expectedId = teacherService.getById(teacherId).getUserId();
        return checkAccess(userDetails, userId -> Objects.equals(expectedId, userId));
    }

    public boolean canUpdate(TeacherUpdateDto updateDto, UserDetails userDetails) {
        return canView(updateDto.getId(), userDetails);
    }
}
