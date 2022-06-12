package ru.dudkomv.neoflexpractice.course.type.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.postgresql.util.PGobject;
import ru.dudkomv.neoflexpractice.course.type.CourseType;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JSONToCourseTypeConverterTest {
    @Test
    void convert() throws Exception {
        ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
        CourseType expectedCourseType = Mockito.mock(CourseType.class);
        String expectedString = "expected string";
        PGobject pgObject = Mockito.mock(PGobject.class);

        Mockito.when(pgObject.getValue()).thenReturn(expectedString);
        Mockito.when(objectMapper.readValue(expectedString, CourseType.class)).thenReturn(expectedCourseType);

        JSONToCourseTypeConverter converter = new JSONToCourseTypeConverter(objectMapper);
        CourseType actualCourseType = converter.convert(pgObject);

        assertEquals(expectedCourseType, actualCourseType);
    }
}