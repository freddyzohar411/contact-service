package com.avensys.rts.contactservice.payloadrequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * author: Koh He Xiang
 * This is the DTO class for the Mailing Address request
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailingAddressDTO {

    private Integer id;

    @Length(max = 100)
    private String line1;

    @Length(max = 100)
    private String line2;

    @Length(max = 100)
    private String line3;

    @Length(max = 50)
    private String city;

    @Length(max = 20)
    private String country;

    @Length(max = 10)
    private String postalCode;
}
