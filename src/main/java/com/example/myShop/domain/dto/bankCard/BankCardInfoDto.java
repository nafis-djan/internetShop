package com.example.myShop.domain.dto.bankCard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import static lombok.AccessLevel.PRIVATE;

/**
 * @author nafis
 * @since 09.02.2022
 */

@Value
@Builder
@Jacksonized
@AllArgsConstructor(access = PRIVATE)
@Schema(name = "BankCardInfo", description = "Info about bank card of user")
public class BankCardInfoDto {
    @Schema(description = "Bank card id",
            required = true)
    Integer id;
    @Schema(description = "Bank card number",
            required = true,
            minLength = 13,
            maxLength = 19)
    String number;
    @Schema(description = "Bank card month of expiration",
            required = true)
    Byte month;
    @Schema(description = "Bank card year of expiration",
            required = true)
    Byte year;
    @Schema(description = "Bank card back number",
            required = true,
            minLength = 3,
            maxLength = 3)
    Short backNumber;

    @Schema(description = "User id of bank card",
            required = true)
    Integer userId;
}
