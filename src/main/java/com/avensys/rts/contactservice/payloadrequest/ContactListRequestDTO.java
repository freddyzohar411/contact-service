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
 * This is the DTO class for the Contact request for a list of contacts
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactListRequestDTO {

    @Valid
    List<ContactRequestDTO> contactList;

    @NotNull
    private Integer entityId;

    @NotEmpty
    @Length(max = 20)
    private String entityType;
}
