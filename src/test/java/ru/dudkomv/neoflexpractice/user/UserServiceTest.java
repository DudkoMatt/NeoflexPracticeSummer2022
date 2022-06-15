package ru.dudkomv.neoflexpractice.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.dudkomv.neoflexpractice.exception.TeacherEntityNotFoundException;
import ru.dudkomv.neoflexpractice.exception.UserEntityNotFoundException;
import ru.dudkomv.neoflexpractice.role.RoleService;
import ru.dudkomv.neoflexpractice.teacher.Teacher;
import ru.dudkomv.neoflexpractice.teacher.TeacherRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(teacherRepository, userRepository, roleService, passwordEncoder);
        userService = Mockito.spy(userService);
    }

    @Test
    void registerTeacher_nullTeacherId() {
        User user = Mockito.mock(User.class);
        Mockito.doReturn(user).when(userService).createUser(user);

        User actual = userService.registerTeacher(user, null);
        assertEquals(user, actual);
    }

    @Test
    void registerTeacher_notNullTeacherId() {
        User user = Mockito.mock(User.class);
        Long teacherId = 1L;
        Mockito.doReturn(user).when(userService).updateUser(user, teacherId);

        User actual = userService.registerTeacher(user, teacherId);
        assertEquals(user, actual);
    }

    @Test
    void createUser_userCreation() {
        User user = Mockito.mock(User.class);
        String password = "password";
        Long userId = null;

        Mockito.when(user.getPassword()).thenReturn(password);
        Mockito.when(passwordEncoder.encode(password)).thenReturn(password);
        Mockito.when(user.getId()).thenReturn(userId);
        Mockito.when(userRepository.save(user)).thenReturn(user);

        User actual = userService.createUser(user);
        assertEquals(user, actual);
        Mockito.verify(roleService, Mockito.times(1)).registerUser(Mockito.any());
    }

    @Test
    void createUser_userUpdate() {
        User user = Mockito.mock(User.class);
        String password = "password";
        Long userId = 1L;

        Mockito.when(user.getPassword()).thenReturn(password);
        Mockito.when(passwordEncoder.encode(password)).thenReturn(password);
        Mockito.when(user.getId()).thenReturn(userId);
        Mockito.when(userRepository.save(user)).thenReturn(user);

        User actual = userService.createUser(user);
        assertEquals(user, actual);
        Mockito.verify(roleService, Mockito.times(0)).registerUser(Mockito.any());
    }

    @Test
    void updateUser() {
        User user = Mockito.mock(User.class);
        Long teacherId = 1L;
        Long userId = 2L;

        Mockito.doReturn(userId).when(userService).getIdByTeacherId(teacherId);
        Mockito.doReturn(user).when(userService).createUser(user);

        User actual = userService.updateUser(user, teacherId);
        assertEquals(user, actual);
        Mockito.verify(user, Mockito.times(1)).setId(userId);
        Mockito.verify(userService, Mockito.times(1)).createUser(user);
    }

    @Test
    void findByUsername_exist() {
        String username = "SomeUsername";
        User user = Mockito.mock(User.class);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(user);

        Optional<User> expected = Optional.of(user);
        Optional<User> actual = userService.findByUsername(username);
        assertEquals(expected, actual);
    }

    @Test
    void findByUsername_notExist() {
        String username = "SomeUsername";
        Mockito.when(userRepository.findByUsername(username)).thenReturn(null);

        Optional<User> expected = Optional.empty();
        Optional<User> actual = userService.findByUsername(username);
        assertEquals(expected, actual);
    }

    @Test
    void findIdByUsername_exist() {
        String username = "SomeUsername";
        Long userId = 1L;
        User user = Mockito.mock(User.class);
        Optional<User> userOptional = Optional.of(user);
        Optional<Long> expected = Optional.of(userId);

        Mockito.doReturn(userOptional).when(userService).findByUsername(username);
        Mockito.when(user.getId()).thenReturn(userId);

        Optional<Long> actual = userService.findIdByUsername(username);
        assertEquals(expected, actual);
    }

    @Test
    void findIdByUsername_notExist() {
        String username = "SomeUsername";
        Optional<Long> expected = Optional.empty();
        Mockito.doReturn(expected).when(userService).findByUsername(username);

        Optional<Long> actual = userService.findIdByUsername(username);
        assertEquals(expected, actual);
    }

    @Test
    void getIdByUsername_exist() {
        Long userId = 1L;
        String username = "SomeUsername";

        Mockito.doReturn(Optional.of(userId)).when(userService).findIdByUsername(username);

        Long actual = userService.getIdByUsername(username);
        assertEquals(userId, actual);
    }

    @Test
    void getIdByUsername_notExist() {
        String username = "SomeUsername";

        Mockito.doReturn(Optional.empty()).when(userService).findIdByUsername(username);

        Exception actual = assertThrows(UserEntityNotFoundException.class, () -> userService.getIdByUsername(username));
        assertEquals("User entity not found with username=" + username, actual.getMessage());
    }

    @Test
    void getUsernameById_exist() {
        Long userId = 1L;
        User user = Mockito.mock(User.class);
        String username = "SomeUsername";

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(user.getUsername()).thenReturn(username);

        String actual = userService.getUsernameById(userId);
        assertEquals(username, actual);
    }

    @Test
    void getUsernameById_notExist() {
        Long userId = 1L;

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception actual = assertThrows(UserEntityNotFoundException.class, () -> userService.getUsernameById(userId));
        assertEquals("User entity not found with id=" + userId, actual.getMessage());
    }

    @Test
    void getIdByTeacherId_exist() {
        Long teacherId = 1L;
        Long userId = 2L;
        Teacher teacher = Mockito.mock(Teacher.class);

        Mockito.when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));
        Mockito.when(teacher.getUserId()).thenReturn(userId);

        Long actual = userService.getIdByTeacherId(teacherId);
        assertEquals(userId, actual);
    }

    @Test
    void getIdByTeacherId_notExist() {
        Long teacherId = 1L;

        Mockito.when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        Exception actual = assertThrows(TeacherEntityNotFoundException.class, () -> userService.getIdByTeacherId(teacherId));
        assertEquals("Teacher entity not found with id=" + teacherId, actual.getMessage());
    }
}