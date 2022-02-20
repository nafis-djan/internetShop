package com.example.myShop.domain.dto.receiving;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import static lombok.AccessLevel.PRIVATE;

/**
 * @author nafis
 * @since 22.12.2021
 */

@Value
@Builder
@Jacksonized
@AllArgsConstructor(access = PRIVATE)
@Schema(name = "ReceivingInfo", description = "Info about receiving of order")
public class ReceivingInfoDto {
    @Schema(description = "Receiving id",
            required = true)
    Integer id;
    @Schema(description = "Method of receiving order",
            required = true)
    String receiveMethod;
    @Schema(description = "Receiving address",
            required = true)
    String address;
}
