package com.example.myShop.domain.dto.producer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import static lombok.AccessLevel.PRIVATE;

/**
 * @author nafis
 * @since 29.12.2021
 */

@Value
@Builder
@Jacksonized
@AllArgsConstructor(access = PRIVATE)
public class ProducerUpdateDto {
    String name;
    String country;
}