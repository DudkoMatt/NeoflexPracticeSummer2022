package ru.dudkomv.neoflexpractice.teacher.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.dudkomv.neoflexpractice.teacher.Teacher;
import ru.dudkomv.neoflexpractice.teacher.dto.TeacherCreationDto;
import ru.dudkomv.neoflexpractice.teacher.dto.TeacherDto;
import ru.dudkomv.neoflexpractice.teacher.dto.TeacherUpdateDto;

@Component
@Mapper(componentModel = "spring")
public interface TeacherMapper {
    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "userId", expression = "java(null)")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "secondName", source = "secondName")
    @Mapping(target = "birthday", source = "birthday")
    Teacher toEntity(TeacherCreationDto creationDto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "userId", expression = "java(null)")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "secondName", source = "secondName")
    @Mapping(target = "birthday", source = "birthday")
    Teacher toEntity(TeacherUpdateDto updateDto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "secondName", source = "secondName")
    @Mapping(target = "birthday", source = "birthday")
    TeacherDto fromEntity(Teacher teacher);
}
