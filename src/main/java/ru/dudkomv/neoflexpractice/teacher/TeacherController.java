package ru.dudkomv.neoflexpractice.teacher;

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
import ru.dudkomv.neoflexpractice.teacher.dto.TeacherCreationDto;
import ru.dudkomv.neoflexpractice.teacher.dto.TeacherDto;
import ru.dudkomv.neoflexpractice.teacher.dto.TeacherUpdateDto;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
@Tag(name = "Teacher", description = "API for teacher management")
public class TeacherController {
    private final TeacherService teacherService;

    @PostMapping
    @PreAuthorize("hasAuthority(@DefinedRoles.ROLE_ADMIN)")
    @Operation(summary = "Create a new teacher")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Teacher created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TeacherDto.class)
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
    public TeacherDto create(@RequestBody @Valid TeacherCreationDto creationDto) {
        return teacherService.create(creationDto);
    }

    @GetMapping
    @Operation(summary = "Teacher info of current logged in user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Teacher info of current logged in user successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TeacherDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content
            )
    })
    public TeacherDto getForUser(Principal principal) {
        return teacherService.getForUser(principal.getName());
    }

    @GetMapping("/all")
    @Operation(summary = "Get all existing teachers")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get all existing teachers successful",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TeacherDto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content
            )
    })
    public List<TeacherDto> getAll() {
        return teacherService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get teacher by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get teacher by id successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TeacherDto.class)
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
    public TeacherDto getById(@PathVariable("id") Long id) {
        return teacherService.getById(id);
    }

    @PutMapping
    @PreAuthorize("@TeacherPermissionEvaluator.canUpdate(#updateDto, principal)")
    @Operation(summary = "Update a teacher")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Teacher updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TeacherDto.class)
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
    public TeacherDto update(@RequestBody @Valid TeacherUpdateDto updateDto) {
        return teacherService.update(updateDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(@DefinedRoles.ROLE_ADMIN)")
    @Operation(summary = "Delete teacher by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Teacher deleted successfully",
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
        teacherService.deleteById(id);
    }
}
