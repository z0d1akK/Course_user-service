package com.innowise.userservice.user.controller;

import com.innowise.userservice.common.dto.response.ApiErrorResponse;
import com.innowise.userservice.common.dto.response.ValidationErrorResponse;
import com.innowise.userservice.user.dto.request.CreateUserRequestDto;
import com.innowise.userservice.user.dto.response.UserResponseDto;
import com.innowise.userservice.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/users")
@Tag(name = "User Internal", description = "Internal endpoints (accessible only via API Gateway)")
public class UserInternalController {

    private final UserService userService;

    @Operation(summary = "Create user", description = "Creates a new user")
    @ApiResponse(responseCode = "201", description = "User successfully created")
    @ApiResponse(responseCode = "400", description = "Validation error or business error",
            content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))
    )
    @PreAuthorize("hasRole('INTERNAL_SERVICE')")
    @PostMapping
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody CreateUserRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }

    @Operation(summary = "Delete user", description = "Deletes user by id with all related payment cards")
    @ApiResponse(responseCode = "204", description = "User successfully deleted")
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    @PreAuthorize("hasRole('INTERNAL_SERVICE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
