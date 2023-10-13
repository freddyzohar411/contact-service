package com.avensys.rts.contactservice.payloadresponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author: Koh He Xiang
 * This is the DTO class for the contact response
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactResponseDTO {
    private Integer id;
    private ContactInformationResponseDTO contactInformation;
    private MailingAddressResponseDTO mailingAddress;
    private String contactRemarks;
}
