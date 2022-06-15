package ru.dudkomv.neoflexpractice.role.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.dudkomv.neoflexpractice.teacher.TeacherService;
import ru.dudkomv.neoflexpractice.user.UserService;
import ru.dudkomv.neoflexpractice.user.security.PermissionEvaluator;

import java.util.Objects;

@Component("RolePermissionEvaluator")
public class RolePermissionEvaluator extends PermissionEvaluator {
    public RolePermissionEvaluator(UserService userService, TeacherService teacherService) {
        super(userService, teacherService);
    }

    public boolean canView(Long requestedUserId, UserDetails userDetails) {
        return checkAccess(userDetails, userId -> Objects.equals(requestedUserId, userId));
    }
}
