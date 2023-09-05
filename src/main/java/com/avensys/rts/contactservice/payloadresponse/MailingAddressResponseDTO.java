package com.avensys.rts.contactservice.payloadresponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * author: Koh He Xiang
 * This is the DTO class for the Mailing Address response
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MailingAddressResponseDTO {
    private Integer id;
    private String line1;
    private String line2;
    private String line3;
    private String city;
    private String country;
    private String postalCode;
}
