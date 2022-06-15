package ru.dudkomv.neoflexpractice.course.type;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.URL;
import org.springframework.lang.Nullable;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Online course type")
public class OnlineCourseType extends CourseType {
    public static final String CLASS_TYPE_NAME = "online";

    @Nullable
    @URL(message = "Lesson URL of online course type must be URL")
    @Schema(description = "URL of the platform where lessons are published", nullable = true)
    protected String lessonUrl;

    public OnlineCourseType() {
        this.type = CLASS_TYPE_NAME;
    }

    public OnlineCourseType(@Nullable String lessonUrl) {
        this();
        this.lessonUrl = lessonUrl;
    }

    @Override
    @Schema(description = "Course type identification string", example = CLASS_TYPE_NAME, required = true)
    public String getType() {
        return type;
    }
}
