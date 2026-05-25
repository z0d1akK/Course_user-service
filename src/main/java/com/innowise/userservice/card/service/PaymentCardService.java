package com.innowise.userservice.card.service;

import com.innowise.userservice.card.dto.request.CreatePaymentCardRequestDto;
import com.innowise.userservice.card.dto.request.UpdatePaymentCardRequestDto;
import com.innowise.userservice.card.dto.response.PaymentCardResponseDto;
import com.innowise.userservice.card.dto.response.PaymentCardShortResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface PaymentCardService {

    /**
     * Creates a new payment card for specified user.
     *
     * @param userId owner user identifier
     * @param request request object containing payment card data
     * @return created payment card response
     */
    PaymentCardResponseDto create(UUID userId, CreatePaymentCardRequestDto request);

    /**
     * Returns full payment card information by identifier.
     *
     * @param id payment card identifier
     * @return full payment card response
     */
    PaymentCardResponseDto getById(UUID id);

    /**
     * Returns short payment card information by identifier.
     * <p>
     * Short response contains reduced set of fields
     * compared to the full payment card response.
     *
     * @param id payment card identifier
     * @return short payment card response
     */
    PaymentCardShortResponseDto getShortById(UUID id);

    /**
     * Returns paginated list of payment cards
     * in short representation.
     *
     * @param pageable pagination information
     * @return page of short payment card responses
     */
    Page<PaymentCardShortResponseDto> getAllShort(Pageable pageable);

    /**
     * Returns all payment cards of specified user
     * in short representation.
     *
     * @param userId owner user identifier
     * @return list of short payment card responses
     */
    List<PaymentCardShortResponseDto> getAllShortByUserId(UUID userId);

    /**
     * Returns paginated list of payment cards
     * in full representation.
     *
     * @param pageable pagination information
     * @return page of full payment card responses
     */
    Page<PaymentCardResponseDto> getAll(Pageable pageable);

    /**
     * Returns all payment cards of specified user
     * in full representation.
     *
     * @param userId owner user identifier
     * @return list of full payment card responses
     */
    List<PaymentCardResponseDto> getAllByUserId(UUID userId);

    /**
     * Updates existing payment card data.
     *
     * @param id payment card identifier
     * @param request request object containing updated card data
     * @return updated payment card response
     */
    PaymentCardResponseDto update(UUID id, UpdatePaymentCardRequestDto request);

    /**
     * Activates payment card.
     *
     * @param id payment card identifier
     */
    void activate(UUID id);

    /**
     * Deactivates payment card.
     *
     * @param id payment card identifier
     */
    void deactivate(UUID id);

    /**
     * Deletes payment card by identifier.
     *
     * @param id payment card identifier
     */
    void delete(UUID id);
}