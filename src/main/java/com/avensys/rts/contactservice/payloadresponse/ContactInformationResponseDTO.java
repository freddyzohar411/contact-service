package com.avensys.rts.contactservice.payloadresponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author: Koh He Xiang
 * This is the DTO class for the Contact Information response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactInformationResponseDTO {
    private String title;
    private String firstName;
    private String lastName;
    private String designation;
    private String department;
    private String industry;
    private String subIndustry;
    private Integer mobileCountry;
    private Integer mobileNumber;
    private Integer landlineCountry;
    private Integer landlineNumber;
    private String email;
}
