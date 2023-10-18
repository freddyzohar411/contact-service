package com.avensys.rts.contactservice.payloadnewresponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactNewResponseDTO {
    private Integer id;
    private String title;
    private String firstName;
    private String lastName;
    private Integer formId;
    private String submissionData;
    private Integer formSubmissionId;
    private String entityType;
    private Integer entityId;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
