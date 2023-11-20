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
    private String entityType;
    private Integer entityId;

    // Form Submission
    private String formData;
    private Integer formId;
}
