package ru.dudkomv.neoflexpractice.course.type.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.postgresql.util.PGobject;
import ru.dudkomv.neoflexpractice.course.type.BaseCourseType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CourseTypeToJSONConverterTest {
    @Test
    void convert() throws Exception {
        ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
        BaseCourseType baseCourseType = Mockito.mock(BaseCourseType.class);
        String expectedString = "expected string";

        Mockito.when(objectMapper.writeValueAsString(baseCourseType)).thenReturn(expectedString);

        CourseTypeToJSONConverter converter = new CourseTypeToJSONConverter(objectMapper);
        PGobject returnObject = converter.convert(baseCourseType);

        assertNotNull(returnObject);
        assertNotNull(returnObject.getValue());
        assertEquals(expectedString, returnObject.getValue());
    }
}