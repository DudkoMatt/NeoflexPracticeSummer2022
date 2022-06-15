package ru.dudkomv.neoflexpractice.course.type;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.Nullable;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Offline course type")
public class OfflineCourseType extends CourseType {
    public static final String CLASS_TYPE_NAME = "offline";

    @Nullable
    @Schema(description = "University name where offline course will take place", nullable = true)
    protected String universityName;

    @Nullable
    @Schema(description = "Address where offline course will take place", nullable = true)
    protected String address;

    public OfflineCourseType() {
        this.type = CLASS_TYPE_NAME;
    }

    public OfflineCourseType(@Nullable String universityName, @Nullable String address) {
        this();
        this.universityName = universityName;
        this.address = address;
    }

    @Override
    @Schema(description = "Course type identification string", example = CLASS_TYPE_NAME, required = true)
    public String getType() {
        return type;
    }
}
