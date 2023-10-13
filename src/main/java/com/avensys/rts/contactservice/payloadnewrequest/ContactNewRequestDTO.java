package com.avensys.rts.contactservice.payloadnewrequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactNewRequestDTO {

    private String title;
    private String firstName;
    private String lastName;
    private String designation;
    private String department;
    private String industry;
    private String subIndustry;
    private int mobileCountry;
    private int mobileNumber;
    private int landlineCountry;
    private int landlineNumber;
    private String email;

    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String addressCity;
    private String addressCountry;
    private String addressPostalCode;

    private String contactRemarks;

    private String entityType;
    private Integer entityId;

    // Form Submission
    private String formData;
    private Integer formId;
}
