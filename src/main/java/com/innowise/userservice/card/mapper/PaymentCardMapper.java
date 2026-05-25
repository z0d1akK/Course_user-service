package com.innowise.userservice.card.mapper;

import com.innowise.userservice.card.dto.request.CreatePaymentCardRequestDto;
import com.innowise.userservice.card.dto.request.UpdatePaymentCardRequestDto;
import com.innowise.userservice.card.dto.response.PaymentCardResponseDto;
import com.innowise.userservice.card.dto.response.PaymentCardShortResponseDto;
import com.innowise.userservice.card.entity.PaymentCard;
import com.innowise.userservice.config.MapStructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapStructConfig.class)
public interface PaymentCardMapper {

    @Mapping(target = "active", constant = "true")
    PaymentCard toEntity(CreatePaymentCardRequestDto dto);

    void updateToEntity(UpdatePaymentCardRequestDto dto, @MappingTarget PaymentCard paymentCard);

    PaymentCardResponseDto toResponse(PaymentCard paymentCard);

    PaymentCardShortResponseDto toShortResponse(PaymentCard paymentCard);
}