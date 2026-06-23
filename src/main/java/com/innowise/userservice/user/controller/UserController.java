package com.innowise.userservice.user.controller;

import com.innowise.userservice.common.dto.response.ApiErrorResponse;
import com.innowise.userservice.common.dto.response.ValidationErrorResponse;
import com.innowise.userservice.user.dto.request.UpdateUserRequestDto;
import com.innowise.userservice.user.dto.request.UserFilterRequestDto;
import com.innowise.userservice.user.dto.response.UserResponseDto;
import com.innowise.userservice.user.dto.response.UserShortResponseDto;
import com.innowise.userservice.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get user by id", description = "Returns full user information")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    @PreAuthorize("@ownershipService.isOwnerOrAdmin(#id)")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @Operation(summary = "Get user by id in short format", description = "Returns short user information")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    @PreAuthorize("@ownershipService.isOwnerOrAdmin(#id)")
    @GetMapping("/{id}/short")
    public ResponseEntity<UserShortResponseDto> getShortById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getShortById(id));
    }

    @Operation(summary = "Get all users in short format",
            description = "Returns paginated list of short users with optional filtering")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "Short users successfully retrieved")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/short")
    public ResponseEntity<Page<UserShortResponseDto>> getAllShort(UserFilterRequestDto filter,
                                                                  Pageable pageable) {
        return ResponseEntity.ok(userService.getAllShort(filter, pageable));
    }

    @Operation(summary = "Get all users",
            description = "Returns paginated list of users with optional filtering")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "Users successfully retrieved")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> getAll(UserFilterRequestDto filter,
                                                        Pageable pageable) {
        return ResponseEntity.ok(userService.getAll(filter, pageable));
    }

    @Operation(summary = "Update user", description = "Updates existing user information")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "User successfully updated")
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    @ApiResponse(responseCode = "400", description = "Validation error",
            content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))
    )
    @PreAuthorize("@ownershipService.isOwnerOrAdmin(#id)")
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(@PathVariable UUID id,
                                                  @Valid @RequestBody UpdateUserRequestDto request) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    @Operation(summary = "Activate user")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "204", description = "User activated")
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        userService.activate(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Deactivate user")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "204", description = "User deactivated")
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        userService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete user", description = "Deletes user by id with all related payment cards")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "204", description = "User successfully deleted")
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.delete(id);

        return ResponseEntity.noContent().build();
    }
}