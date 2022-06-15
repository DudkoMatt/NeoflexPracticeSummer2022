package ru.dudkomv.neoflexpractice.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dudkomv.neoflexpractice.exception.TeacherEntityNotFoundException;
import ru.dudkomv.neoflexpractice.teacher.dto.TeacherCreationDto;
import ru.dudkomv.neoflexpractice.teacher.dto.TeacherDto;
import ru.dudkomv.neoflexpractice.teacher.dto.TeacherUpdateDto;
import ru.dudkomv.neoflexpractice.teacher.mapper.TeacherMapper;
import ru.dudkomv.neoflexpractice.user.User;
import ru.dudkomv.neoflexpractice.user.UserService;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherMapper teacherMapper;
    private final TeacherRepository teacherRepository;
    private final UserService userService;

    public TeacherDto create(TeacherCreationDto creationDto) {
        Teacher teacher = creationDto.acceptTeacherMapper(teacherMapper);

        User user = new User(creationDto.getUsername(), creationDto.getPassword());
        user = userService.registerTeacher(user, teacher.getId());

        teacher.setUserId(user.getId());

        return teacherMapper.fromEntity(
                teacherRepository.save(
                        teacher
                )
        );
    }

    public List<TeacherDto> getAll() {
        return teacherRepository
                .findAll()
                .stream()
                .map(teacherMapper::fromEntity)
                .toList();
    }

    public TeacherDto getById(Long id) {
        return getById(id, () -> teacherRepository.findById(id));
    }

    @Transactional
    public TeacherDto getForUser(String username) {
        Long userId = userService.getIdByUsername(username);
        return getByUserId(userId);
    }

    public TeacherDto getByUserId(Long userId) {
        return getById(userId, () -> teacherRepository.findByUserId(userId));
    }

    private TeacherDto getById(Long id, Supplier<Optional<Teacher>> supplier) {
        if (id == null) {
            return null;
        }

        return teacherMapper.fromEntity(
                supplier
                        .get()
                        .orElseThrow(() -> new TeacherEntityNotFoundException(id))
        );
    }

    @Transactional
    public TeacherDto update(TeacherUpdateDto updateDto) {
        validateExists(updateDto.getId());
        return create(updateDto);
    }

    @Transactional
    public void deleteById(Long id) {
        validateExists(id);
        teacherRepository.deleteById(id);
    }

    public void validateExists(Long id) {
        if (!teacherRepository.existsById(id)) {
            throw new TeacherEntityNotFoundException(id);
        }
    }
}
