package com.avensys.rts.contactservice.payloadnewresponse;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactNewResponseDTO {
	private Long id;
	private String title;
	private String firstName;
	private String lastName;
	private Long formId;
	private String submissionData;
	private Long formSubmissionId;
	private String entityType;
	private Long entityId;
	private String createdBy;
	private String updatedBy;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public String getFullName() {
		return title + " " + firstName + " " + lastName;
	}
}
