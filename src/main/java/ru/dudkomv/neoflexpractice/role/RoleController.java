package ru.dudkomv.neoflexpractice.role;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dudkomv.neoflexpractice.advice.ExceptionControllerAdvice.ApiError;

import java.util.List;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@Tag(name = "Role", description = "API for role management")
public class RoleController {
    private final RoleService roleService;

    @GetMapping("/{userId}")
    @PreAuthorize("@RolePermissionEvaluator.canView(#userId, principal)")
    @Operation(summary = "Get roles of the user by user id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get roles by user id successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Role.class)
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
    public List<Role> getAllByUserId(@PathVariable("userId") Long userId) {
        return roleService.getAllByUserId(userId);
    }
}
