package com.avensys.rts.contactservice.payloadnewresponse;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FormSubmissionsResponseDTO {
	private Long id;
	private String formId;
	private Long userId;
	private JsonNode submissionData;
	private Integer entityId;
	private String entityType;
}
