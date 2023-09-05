package com.avensys.rts.contactservice.payloadrequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * author: Koh He Xiang
 * This is the DTO class for the Contact request
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactRequestDTO {

    private Integer id;

    @Valid
    private ContactInformationDTO contactInformation;

    private MailingAddressDTO mailingAddress;

    @Length(max = 250)
    private String contactRemarks;

//    @NotEmpty
    @Length(max = 20)
    private String entityType;

//    @NotNull
    private Integer entityId;

}
