package ru.dudkomv.neoflexpractice.course;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dudkomv.neoflexpractice.advice.ExceptionControllerAdvice.ApiError;
import ru.dudkomv.neoflexpractice.course.dto.CourseCreationDto;
import ru.dudkomv.neoflexpractice.course.dto.CourseUpdateDto;
import ru.dudkomv.neoflexpractice.lesson.dto.LessonUpdateDto;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
@Tag(name = "Course", description = "API for course management")
public class CourseController {
    private final CourseService courseService;

    @PostMapping
    @PreAuthorize("@CoursePermissionEvaluator.canCreate(#creationDto, principal)")
    @Operation(summary = "Create a new course")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Course created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CourseUpdateDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content
            )
    })
    public CourseUpdateDto create(@RequestBody @Valid CourseCreationDto creationDto) {
        return courseService.create(creationDto);
    }

    @GetMapping
    @Operation(summary = "Get all courses of current logged in user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get all courses of current logged in user successful",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CourseUpdateDto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content
            )
    })
    public List<CourseUpdateDto> getAllForUser(Principal principal) {
        return courseService.getAllForUser(principal.getName());
    }

    @GetMapping("/all")
    @Operation(summary = "Get all existing courses")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get all existing courses successful",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CourseUpdateDto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content
            )
    })
    public List<CourseUpdateDto> getAll() {
        return courseService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get course by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get course by id successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CourseUpdateDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            )
    })
    public CourseUpdateDto getById(@PathVariable("id") Long id) {
        return courseService.getById(id);
    }

    @GetMapping("/{id}/lesson")
    @Operation(summary = "Get lessons of the course by course id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get lessons successful",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = LessonUpdateDto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            )
    })
    public List<LessonUpdateDto> getLessonsById(@PathVariable("id") Long id) {
        return courseService.getLessonsById(id);
    }

    @PutMapping
    @PreAuthorize("@CoursePermissionEvaluator.canUpdate(#updateDto, principal)")
    @Operation(summary = "Update a course")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Course updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CourseUpdateDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            )
    })
    public CourseUpdateDto update(@RequestBody @Valid CourseUpdateDto updateDto) {
        return courseService.update(updateDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@CoursePermissionEvaluator.canDelete(#id, principal)")
    @Operation(summary = "Delete course by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Course deleted successfully",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            )
    })
    public void deleteById(@PathVariable("id") Long id) {
        courseService.deleteById(id);
    }

    @GetMapping("/by-category-id-recursively/{categoryId}")
    @Operation(summary = "Get courses by category id recursively")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get courses by category id successful",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CourseUpdateDto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            )
    })
    public List<CourseUpdateDto> getCoursesByCategoryIdRecursively(@PathVariable("categoryId") Long categoryId) {
        return courseService.getCoursesByCategoryIdRecursively(categoryId);
    }

    @GetMapping("/by-type-name-recursively/{type}")
    @Operation(summary = "Get courses by type")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get courses by type successful",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CourseUpdateDto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content
            )
    })
    public List<CourseUpdateDto> getCoursesByType(@PathVariable("type") String type) {
        return courseService.getCoursesByType(type);
    }

    @GetMapping("/by-curator-id/{curatorId}")
    @Operation(summary = "Get courses by curator id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get courses by curator id successful",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CourseUpdateDto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            )
    })
    public List<CourseUpdateDto> getCoursesByCuratorId(@PathVariable("curatorId") Long curatorId) {
        return courseService.getCoursesByCuratorId(curatorId);
    }

    @PostMapping("/clone/{id}")
    @Operation(summary = "Clone a course")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Course cloned successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CourseUpdateDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            )
    })
    public CourseUpdateDto cloneCourse(@PathVariable("id") Long id, Principal principal) {
        return courseService.cloneCourse(id, principal.getName());
    }
}
