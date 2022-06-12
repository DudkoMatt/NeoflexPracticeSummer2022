package ru.dudkomv.neoflexpractice.user.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import ru.dudkomv.neoflexpractice.role.DefinedRoles;
import ru.dudkomv.neoflexpractice.teacher.TeacherService;
import ru.dudkomv.neoflexpractice.user.UserService;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.LongConsumer;
import java.util.function.Supplier;

@RequiredArgsConstructor
public abstract class PermissionEvaluator {
    protected final UserService userService;
    protected final TeacherService teacherService;

    protected boolean userIsAdmin(UserDetails userDetails) {
        return userDetails
                .getAuthorities()
                .contains(new SimpleGrantedAuthority(DefinedRoles.ROLE_ADMIN));
    }

    protected boolean checkAccess(UserDetails userDetails, Function<Long, Boolean> checkUserIdFunction) {
        if (userIsAdmin(userDetails)) {
            return true;
        }

        Optional<Long> optionalUserId = userService.findIdByUsername(userDetails.getUsername());
        return optionalUserId
                .map(checkUserIdFunction)
                .orElse(false);
    }

    protected boolean injectIfNullTeacherIdTo(Supplier<?> propertySupplier, LongConsumer injectFunction) {
        if (propertySupplier.get() == null) {
            injectTeacherIdTo(injectFunction);
            return true;
        }
        return false;
    }

    private void injectTeacherIdTo(LongConsumer injectFunction) {
        Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
        Long userId = userService.getIdByUsername(authenticatedUser.getName());
        Long teacherId = teacherService.getByUserId(userId).getId();
        injectFunction.accept(teacherId);
    }
}
