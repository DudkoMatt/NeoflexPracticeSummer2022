package ru.dudkomv.neoflexpractice.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import ru.dudkomv.neoflexpractice.exception.UserDisabledException;
import ru.dudkomv.neoflexpractice.exception.UserEntityNotFoundException;
import ru.dudkomv.neoflexpractice.role.RoleService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PostgresUserDetailsService implements UserDetailsService {
    private final UserService userService;
    private final RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userService
                .findByUsername(username)
                .orElseThrow(() -> new UserEntityNotFoundException(username));

        if (!user.getEnabled()) {
            throw new UserDisabledException(username);
        }

        List<SimpleGrantedAuthority> authorities =
                roleService
                        .getAllByUserId(user.getId())
                        .stream()
                        .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                        .toList();

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}
