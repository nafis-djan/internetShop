package com.example.myShop.domain.dto.producer;

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
@Schema(name = "ProducerInfo", description = "Info about producer of goods")
public class ProducerInfoDto {
    @Schema(description = "Producer id",
            required = true)
    Integer id;
    @Schema(description = "Producer name",
            required = true)
    String name;
    @Schema(description = "Producer country",
            required = true)
    String country;
}
