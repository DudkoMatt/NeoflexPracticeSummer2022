package ru.dudkomv.neoflexpractice.course.type;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Base course type")
public class BaseCourseType extends CourseType {
    public static final String CLASS_TYPE_NAME = "base";

    public BaseCourseType() {
        this.type = CLASS_TYPE_NAME;
    }

    @Override
    @Schema(description = "Course type identification string", example = CLASS_TYPE_NAME, required = true)
    public String getType() {
        return type;
    }
}
