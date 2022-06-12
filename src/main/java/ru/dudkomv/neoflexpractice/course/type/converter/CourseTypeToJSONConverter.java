package ru.dudkomv.neoflexpractice.course.type.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.postgresql.util.PGobject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import ru.dudkomv.neoflexpractice.course.type.CourseType;

@WritingConverter
@RequiredArgsConstructor
public class CourseTypeToJSONConverter implements Converter<CourseType, PGobject> {
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public PGobject convert(CourseType source) {
        PGobject jsonObject = new PGobject();
        jsonObject.setType("json");
        jsonObject.setValue(objectMapper.writeValueAsString(source));
        return jsonObject;
    }
}
