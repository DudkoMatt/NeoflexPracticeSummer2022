package ru.dudkomv.neoflexpractice.course.type.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.postgresql.util.PGobject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import ru.dudkomv.neoflexpractice.course.type.CourseType;

@ReadingConverter
@RequiredArgsConstructor
public class JSONToCourseTypeConverter implements Converter<PGobject, CourseType> {
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public CourseType convert(PGobject source) {
        return objectMapper.readValue(source.getValue(), CourseType.class);
    }
}
