package ru.dudkomv.neoflexpractice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import ru.dudkomv.neoflexpractice.course.type.converter.CourseTypeToJSONConverter;
import ru.dudkomv.neoflexpractice.course.type.converter.JSONToCourseTypeConverter;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class JdbcConfiguration extends AbstractJdbcConfiguration {
    private final ObjectMapper objectMapper;

    @Override
    @NonNull
    public JdbcCustomConversions jdbcCustomConversions() {
        return new JdbcCustomConversions(List.of(
                new CourseTypeToJSONConverter(objectMapper),
                new JSONToCourseTypeConverter(objectMapper)
        ));
    }
}
