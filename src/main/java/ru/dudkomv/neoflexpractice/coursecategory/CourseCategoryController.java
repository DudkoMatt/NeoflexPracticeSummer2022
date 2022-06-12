package ru.dudkomv.neoflexpractice.coursecategory;

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
import ru.dudkomv.neoflexpractice.coursecategory.dto.CourseCategoryCreationDto;
import ru.dudkomv.neoflexpractice.coursecategory.dto.CourseCategoryUpdateDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/course/category")
@RequiredArgsConstructor
@Tag(name = "Course Category", description = "API for course category management")
public class CourseCategoryController {
    private final CourseCategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasAuthority(@DefinedRoles.ROLE_ADMIN)")
    @Operation(summary = "Create a new course category")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Course category created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CourseCategory.class)
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
    public CourseCategory create(@RequestBody @Valid CourseCategoryCreationDto creationDto) {
        return categoryService.create(creationDto);
    }

    @GetMapping
    @Operation(summary = "Get all course categories")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get all course categories successful",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CourseCategory.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content
            ),
    })
    public List<CourseCategory> getAll() {
        return categoryService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get course category by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get course category by id successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CourseCategory.class)
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
    public CourseCategory getById(@PathVariable("id") Long id) {
        return categoryService.getById(id);
    }

    @PutMapping
    @PreAuthorize("hasAuthority(@DefinedRoles.ROLE_ADMIN)")
    @Operation(summary = "Update a course category")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Course category updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CourseCategory.class)
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
    public CourseCategory update(@RequestBody @Valid CourseCategoryUpdateDto updateDto) {
        return categoryService.update(updateDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(@DefinedRoles.ROLE_ADMIN)")
    @Operation(summary = "Delete course category by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Course category deleted successfully",
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
        categoryService.deleteById(id);
    }
}
