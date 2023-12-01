package com.avensys.rts.contactservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contact_new")
public class ContactNewEntity extends BaseEntity {

	private static final long serialVersionUID = 4539091506101115672L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "title", length = 5)
	private String title;

	@Column(name = "first_name", length = 50)
	private String firstName;

	@Column(name = "last_name", length = 50)
	private String lastName;

	@Column(name = "entity_id")
	private Long entityId;

	@Column(name = "entity_type", length = 30)
	private String entityType;

	@Column(name = "form_id")
	private Long formId;

	@Column(name = "form_submission_id")
	private Long formSubmissionId;

}