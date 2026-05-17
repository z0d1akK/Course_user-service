package com.zodiakk.userservice.card.controller;

import com.zodiakk.userservice.card.dto.request.CreatePaymentCardRequestDto;
import com.zodiakk.userservice.card.dto.request.UpdatePaymentCardRequestDto;
import com.zodiakk.userservice.card.dto.response.PaymentCardResponseDto;
import com.zodiakk.userservice.card.dto.response.PaymentCardShortResponseDto;
import com.zodiakk.userservice.card.service.PaymentCardService;
import com.zodiakk.userservice.common.dto.response.ApiErrorResponse;
import com.zodiakk.userservice.common.dto.response.ValidationErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment-cards")
public class PaymentCardController {

    private final PaymentCardService paymentCardService;

    @Operation(summary = "Create payment card", description = "Creates a new payment card for user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Payment card successfully created"),
            @ApiResponse(responseCode = "400", description = "Validation error or max cards limit exceeded",
                    content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @PostMapping
    public ResponseEntity<PaymentCardResponseDto> create(@RequestParam UUID userId, @Valid @RequestBody CreatePaymentCardRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentCardService.create(userId, request));
    }

    @Operation(summary = "Get payment card by id", description = "Returns full payment card information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment card found"),
            @ApiResponse(responseCode = "404", description = "Payment card not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<PaymentCardResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(paymentCardService.getById(id));
    }

    @Operation(summary = "Get short payment card by id", description = "Returns short payment card information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment card found"),
            @ApiResponse(responseCode = "404", description = "Payment card not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @GetMapping("/{id}/short")
    public ResponseEntity<PaymentCardShortResponseDto> getShortById(@PathVariable UUID id) {
        return ResponseEntity.ok(paymentCardService.getShortById(id));
    }

    @Operation(summary = "Get all short payment cards", description = "Returns paginated short payment cards list")
    @ApiResponse(responseCode = "200", description = "Payment cards successfully retrieved")
    @GetMapping("/short")
    public ResponseEntity<Page<PaymentCardShortResponseDto>> getAllShort(Pageable pageable) {
        return ResponseEntity.ok(paymentCardService.getAllShort(pageable));
    }

    @Operation(summary = "Get all payment cards", description = "Returns paginated full payment cards list")
    @ApiResponse(responseCode = "200", description = "Payment cards successfully retrieved")
    @GetMapping
    public ResponseEntity<Page<PaymentCardResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(paymentCardService.getAll(pageable));
    }

    @Operation(summary = "Get all short payment cards by user id", description = "Returns short payment cards list by user id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment cards successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @GetMapping("/short/by-user/{userId}")
    public ResponseEntity<List<PaymentCardShortResponseDto>> getAllShortByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(paymentCardService.getAllShortByUserId(userId));
    }

    @Operation(summary = "Get all payment cards by user id", description = "Returns full payment cards list by user id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment cards successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<PaymentCardResponseDto>> getAllByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(paymentCardService.getAllByUserId(userId));
    }

    @Operation(summary = "Update payment card", description = "Updates payment card information"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment card successfully updated"),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Payment card not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @PatchMapping("/{id}")
    public ResponseEntity<PaymentCardResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePaymentCardRequestDto request) {
        return ResponseEntity.ok(paymentCardService.update(id, request));
    }

    @Operation(summary = "Activate payment card")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Payment card activated"),
            @ApiResponse(responseCode = "404", description = "Payment card not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        paymentCardService.activate(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Deactivate payment card")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Payment card deactivated"),
            @ApiResponse(responseCode = "404", description = "Payment card not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        paymentCardService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}