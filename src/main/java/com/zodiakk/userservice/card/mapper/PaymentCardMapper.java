package com.zodiakk.userservice.card.mapper;

import com.zodiakk.userservice.card.dto.request.CreatePaymentCardRequestDto;
import com.zodiakk.userservice.card.dto.request.UpdatePaymentCardRequestDto;
import com.zodiakk.userservice.card.dto.response.PaymentCardResponseDto;
import com.zodiakk.userservice.card.dto.response.PaymentCardShortResponseDto;
import com.zodiakk.userservice.card.entity.PaymentCard;
import com.zodiakk.userservice.config.MapStructConfig;
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