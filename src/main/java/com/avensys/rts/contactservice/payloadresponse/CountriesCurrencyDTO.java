package com.avensys.rts.contactservice.payloadresponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author: Koh He Xiang
 * This is the DTO class for the countries currency response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountriesCurrencyDTO {
    private int id;

    private String name;

    private String iso2;

    private String iso3;

    private String phoneCode;

    private String currency;

    private String currencySymbol;
}
