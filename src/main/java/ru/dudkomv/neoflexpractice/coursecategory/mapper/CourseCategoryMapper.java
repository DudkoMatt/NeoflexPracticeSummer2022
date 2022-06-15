package ru.dudkomv.neoflexpractice.coursecategory.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.dudkomv.neoflexpractice.coursecategory.CourseCategory;
import ru.dudkomv.neoflexpractice.coursecategory.dto.CourseCategoryCreationDto;
import ru.dudkomv.neoflexpractice.coursecategory.dto.CourseCategoryUpdateDto;

@Component
@Mapper(componentModel = "spring")
public interface CourseCategoryMapper {
    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "parentId", source = "parentId")
    CourseCategory toEntity(CourseCategoryCreationDto creationDto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "parentId", source = "parentId")
    CourseCategory toEntity(CourseCategoryUpdateDto updateDto);
}
