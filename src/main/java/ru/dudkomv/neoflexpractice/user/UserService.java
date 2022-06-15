package ru.dudkomv.neoflexpractice.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dudkomv.neoflexpractice.exception.TeacherEntityNotFoundException;
import ru.dudkomv.neoflexpractice.exception.UserEntityNotFoundException;
import ru.dudkomv.neoflexpractice.role.RoleService;
import ru.dudkomv.neoflexpractice.teacher.TeacherRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User registerTeacher(User user, Long teacherId) {
        return teacherId == null ? createUser(user) : updateUser(user, teacherId);
    }

    @Transactional
    public User createUser(User user) {
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        boolean isUserCreation = user.getId() == null;
        User createdUser = userRepository.save(user);

        if (isUserCreation) {
            roleService.registerUser(user);
        }

        return createdUser;
    }

    @Transactional
    public User updateUser(User user, Long teacherId) {
        user.setId(getIdByTeacherId(teacherId));
        return createUser(user);
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    public Optional<Long> findIdByUsername(String username) {
        return findByUsername(username).map(User::getId);
    }

    public Long getIdByUsername(String username) {
        return findIdByUsername(username).orElseThrow(() -> new UserEntityNotFoundException(username));
    }

    public String getUsernameById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new UserEntityNotFoundException(id))
                .getUsername();
    }

    public Long getIdByTeacherId(Long teacherId) {
        return teacherRepository
                .findById(teacherId)
                .orElseThrow(() -> new TeacherEntityNotFoundException(teacherId))
                .getUserId();
    }
}
