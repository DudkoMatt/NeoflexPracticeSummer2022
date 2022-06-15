package ru.dudkomv.neoflexpractice.user.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.dudkomv.neoflexpractice.teacher.TeacherService;
import ru.dudkomv.neoflexpractice.user.UserService;

import java.util.Objects;

@Component("UserPermissionEvaluator")
public class UserPermissionEvaluator extends PermissionEvaluator {
    public UserPermissionEvaluator(UserService userService, TeacherService teacherService) {
        super(userService, teacherService);
    }

    public boolean canView(Long requestedUserId, UserDetails userDetails) {
        return checkAccess(userDetails, userId -> Objects.equals(requestedUserId, userId));
    }
}
