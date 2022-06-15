package ru.dudkomv.neoflexpractice.course.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.dudkomv.neoflexpractice.course.Course;
import ru.dudkomv.neoflexpractice.course.dto.CourseCreationDto;
import ru.dudkomv.neoflexpractice.course.dto.CourseUpdateDto;

@Component
@Mapper(componentModel = "spring")
public interface CourseMapper {
    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "categoryId", source = "categoryId")
    @Mapping(target = "curatorId", source = "curatorId")
    @Mapping(target = "studentCount", source = "studentCount")
    @Mapping(target = "type", source = "type")
    Course toEntity(CourseCreationDto creationDto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "categoryId", source = "categoryId")
    @Mapping(target = "curatorId", source = "curatorId")
    @Mapping(target = "studentCount", source = "studentCount")
    @Mapping(target = "type", source = "type")
    Course toEntity(CourseUpdateDto updateDto);

    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "categoryId", source = "categoryId")
    @Mapping(target = "curatorId", source = "curatorId")
    @Mapping(target = "studentCount", source = "studentCount")
    @Mapping(target = "type", source = "type")
    CourseCreationDto toCreation(CourseUpdateDto updateDto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "categoryId", source = "categoryId")
    @Mapping(target = "curatorId", source = "curatorId")
    @Mapping(target = "studentCount", source = "studentCount")
    @Mapping(target = "type", source = "type")
    CourseUpdateDto fromEntity(Course course);
}
