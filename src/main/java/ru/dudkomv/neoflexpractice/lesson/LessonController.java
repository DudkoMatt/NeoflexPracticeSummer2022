package ru.dudkomv.neoflexpractice.lesson;

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
import ru.dudkomv.neoflexpractice.lesson.dto.LessonCreationDto;
import ru.dudkomv.neoflexpractice.lesson.dto.LessonUpdateDto;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/lesson")
@RequiredArgsConstructor
@Tag(name = "Lesson", description = "API for lesson management")
public class LessonController {
    private final LessonService lessonService;

    @PostMapping
    @PreAuthorize("@LessonPermissionEvaluator.canCreate(#creationDto, principal)")
    @Operation(summary = "Create a new lesson")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lesson created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LessonUpdateDto.class)
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
    public LessonUpdateDto create(@RequestBody @Valid LessonCreationDto creationDto) {
        return lessonService.create(creationDto);
    }

    @GetMapping
    @Operation(summary = "Get all lessons of current logged in user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get all lessons of current logged in user successful",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = LessonUpdateDto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content
            )
    })
    public List<LessonUpdateDto> getAllForUser(Principal principal) {
        return lessonService.getAllForUser(principal.getName());
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get lessons by user id")
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
    public List<LessonUpdateDto> getAllForUserByUserId(@PathVariable("userId") Long userId) {
        return lessonService.getAllForUser(userId);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all existing lessons")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get all existing lessons successful",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = LessonUpdateDto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content
            )
    })
    public List<LessonUpdateDto> getAll() {
        return lessonService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get lesson by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get lesson by id successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LessonUpdateDto.class)
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
    public LessonUpdateDto getById(@PathVariable("id") Long id) {
        return lessonService.getById(id);
    }

    @PutMapping
    @PreAuthorize("@LessonPermissionEvaluator.canUpdate(#updateDto, principal)")
    @Operation(summary = "Update a lesson")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lesson updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LessonUpdateDto.class)
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
    public LessonUpdateDto update(@RequestBody @Valid LessonUpdateDto updateDto) {
        return lessonService.update(updateDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@LessonPermissionEvaluator.canDelete(#id, principal)")
    @Operation(summary = "Delete lesson by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lesson deleted successfully",
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
        lessonService.deleteById(id);
    }
}
