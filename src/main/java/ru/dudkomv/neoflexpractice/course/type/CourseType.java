package ru.dudkomv.neoflexpractice.course.type;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BaseCourseType.class, name = BaseCourseType.CLASS_TYPE_NAME),
        @JsonSubTypes.Type(value = OfflineCourseType.class, name = OfflineCourseType.CLASS_TYPE_NAME),
        @JsonSubTypes.Type(value = OnlineCourseType.class, name = OnlineCourseType.CLASS_TYPE_NAME)
})
@Schema(
        description = "Course type",
        discriminatorProperty = "type",
        discriminatorMapping = {
                @DiscriminatorMapping(schema = BaseCourseType.class, value = BaseCourseType.CLASS_TYPE_NAME),
                @DiscriminatorMapping(schema = OfflineCourseType.class, value = OfflineCourseType.CLASS_TYPE_NAME),
                @DiscriminatorMapping(schema = OnlineCourseType.class, value = OnlineCourseType.CLASS_TYPE_NAME),
        },
        oneOf = {
                BaseCourseType.class,
                OfflineCourseType.class,
                OnlineCourseType.class
        }
)
public abstract class CourseType {
    @Schema(description = "Course type identification string", required = true)
    protected String type;
}
