package com.avensys.rts.contactservice.payloadnewrequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContactRequestDTO {

	private String title;
	private String firstName;
	private String lastName;
	private String entityType;
	private Long entityId;

	// Form Submission
	private String formData;
	private Long formId;

	private Long createdBy;
	private Long updatedBy;
}
