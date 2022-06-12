package ru.dudkomv.neoflexpractice.role;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dudkomv.neoflexpractice.user.User;

import java.util.List;

import static ru.dudkomv.neoflexpractice.role.DefinedRoles.ROLE_TEACHER;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public void registerUser(User user) {
        Role teacherRole = roleRepository.findByName(ROLE_TEACHER);
        roleRepository.setUserRole(user.getId(), teacherRole.getId());
    }

    public List<Role> getAllByUserId(Long userId) {
        return roleRepository.findAllByUserId(userId);
    }
}
