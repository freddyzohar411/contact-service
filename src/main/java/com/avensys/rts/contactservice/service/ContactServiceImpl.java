package com.avensys.rts.contactservice.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avensys.rts.contactservice.APIClient.FormSubmissionAPIClient;
import com.avensys.rts.contactservice.APIClient.UserAPIClient;
import com.avensys.rts.contactservice.customresponse.HttpResponse;
import com.avensys.rts.contactservice.entity.ContactEntity;
import com.avensys.rts.contactservice.entity.UserEntity;
import com.avensys.rts.contactservice.payloadnewrequest.ContactRequestDTO;
import com.avensys.rts.contactservice.payloadnewrequest.FormSubmissionsRequestDTO;
import com.avensys.rts.contactservice.payloadnewresponse.ContactResponseDTO;
import com.avensys.rts.contactservice.payloadnewresponse.FormSubmissionsResponseDTO;
import com.avensys.rts.contactservice.repository.ContactRepository;
import com.avensys.rts.contactservice.repository.UserRepository;
import com.avensys.rts.contactservice.util.MappingUtil;

import jakarta.transaction.Transactional;

@Service
public class ContactServiceImpl implements ContactService {

	private final Logger log = LoggerFactory.getLogger(ContactServiceImpl.class);
	private final ContactRepository contactRepository;

	@Autowired
	private UserAPIClient userAPIClient;

	@Autowired
	private FormSubmissionAPIClient formSubmissionAPIClient;

	@Autowired
	private UserRepository userRepository;

	public ContactServiceImpl(ContactRepository contactRepository, UserAPIClient userAPIClient,
			FormSubmissionAPIClient formSubmissionAPIClient) {
		this.contactRepository = contactRepository;
		this.userAPIClient = userAPIClient;
		this.formSubmissionAPIClient = formSubmissionAPIClient;
	}

	@Override
	@Transactional
	public ContactResponseDTO createContact(ContactRequestDTO contactRequestDTO) {
		log.info("Creating contact: service");
		System.out.println("Contact: " + contactRequestDTO);
		ContactEntity savedContactEntity = contactNewRequestDTOToContactNewEntity(contactRequestDTO);

		// Save form data to form submission microservice
		FormSubmissionsRequestDTO formSubmissionsRequestDTO = new FormSubmissionsRequestDTO();
		formSubmissionsRequestDTO.setUserId(contactRequestDTO.getCreatedBy());
		formSubmissionsRequestDTO.setFormId(contactRequestDTO.getFormId());
		formSubmissionsRequestDTO
				.setSubmissionData(MappingUtil.convertJSONStringToJsonNode(contactRequestDTO.getFormData()));
		formSubmissionsRequestDTO.setEntityId(savedContactEntity.getId());
		formSubmissionsRequestDTO.setEntityType(contactRequestDTO.getEntityType());
		HttpResponse formSubmissionResponse = formSubmissionAPIClient.addFormSubmission(formSubmissionsRequestDTO);
		FormSubmissionsResponseDTO formSubmissionData = MappingUtil
				.mapClientBodyToClass(formSubmissionResponse.getData(), FormSubmissionsResponseDTO.class);

		savedContactEntity.setFormSubmissionId(formSubmissionData.getId());

		return contactNewEntityToContactNewResponseDTO(savedContactEntity);
	}

	@Override
	public ContactResponseDTO getContactById(Long id) {
		ContactEntity contactEntityFound = contactRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Contact not found"));
		return contactNewEntityToContactNewResponseDTO(contactEntityFound);
	}

	@Override
	@Transactional
	public ContactResponseDTO updateContact(Long id, ContactRequestDTO contactRequestDTO) {
		ContactEntity contactEntityFound = contactRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Contact not found"));
		ContactEntity updatedContactEntity = updateContactNewRequestDTOToContactNewEntity(contactEntityFound,
				contactRequestDTO);

		// Update form submission
		FormSubmissionsRequestDTO formSubmissionsRequestDTO = new FormSubmissionsRequestDTO();
		formSubmissionsRequestDTO.setUserId(contactRequestDTO.getUpdatedBy());
		formSubmissionsRequestDTO.setFormId(contactRequestDTO.getFormId());
		formSubmissionsRequestDTO
				.setSubmissionData(MappingUtil.convertJSONStringToJsonNode(contactRequestDTO.getFormData()));
		formSubmissionsRequestDTO.setEntityId(updatedContactEntity.getId());
		formSubmissionsRequestDTO.setEntityType(contactRequestDTO.getEntityType());
		HttpResponse formSubmissionResponse = formSubmissionAPIClient
				.updateFormSubmission(updatedContactEntity.getFormSubmissionId(), formSubmissionsRequestDTO);
		FormSubmissionsResponseDTO formSubmissionData = MappingUtil
				.mapClientBodyToClass(formSubmissionResponse.getData(), FormSubmissionsResponseDTO.class);

		updatedContactEntity.setFormSubmissionId(formSubmissionData.getId());
		return contactNewEntityToContactNewResponseDTO(updatedContactEntity);
	}

	@Override
	@Transactional
	public void deleteContact(Long id) {
		ContactEntity contactEntityFound = contactRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Contact not found"));
		contactRepository.delete(contactEntityFound);
	}

	@Override
	public List<ContactResponseDTO> getContactsByEntityTypeAndEntityId(String entityType, Integer entityId) {
		List<ContactEntity> contactEntityList = contactRepository.findByEntityTypeAndEntityId(entityType, entityId);
		List<ContactResponseDTO> contactResponseDTOList = contactEntityList.stream()
				.map(this::contactNewEntityToContactNewResponseDTO).toList();
		return contactResponseDTOList;
	}

	@Override
	@Transactional
	public void deleteContactsByEntityTypeAndEntityId(String entityType, Integer entityId) {
		List<ContactEntity> contactEntityList = contactRepository.findByEntityTypeAndEntityId(entityType, entityId);
		if (!contactEntityList.isEmpty()) {
			// Delete each contact form submission before deleting
			contactEntityList.forEach(contactEntity -> {
				formSubmissionAPIClient.deleteFormSubmission(contactEntity.getFormSubmissionId());
				contactRepository.delete(contactEntity);
			});
		}
	}

	private ContactResponseDTO contactNewEntityToContactNewResponseDTO(ContactEntity contactEntity) {
		ContactResponseDTO contactResponseDTO = new ContactResponseDTO();
		contactResponseDTO.setId(contactEntity.getId());
		contactResponseDTO.setTitle(contactEntity.getTitle());
		contactResponseDTO.setFirstName(contactEntity.getFirstName());
		contactResponseDTO.setLastName(contactEntity.getLastName());
		contactResponseDTO.setCreatedAt(contactEntity.getCreatedAt());
		contactResponseDTO.setUpdatedAt(contactEntity.getUpdatedAt());
		contactResponseDTO.setEntityType(contactEntity.getEntityType());
		contactResponseDTO.setEntityId(contactEntity.getEntityId());
		contactResponseDTO.setFormId(contactEntity.getFormId());
		contactResponseDTO.setFormSubmissionId(contactEntity.getFormSubmissionId());

		// Get created by User data from user microservice
		Optional<UserEntity> userEntity = userRepository.findById(contactEntity.getCreatedBy());
		UserEntity userData = userEntity.get();
		contactResponseDTO.setCreatedBy(userData.getFirstName() + " " + userData.getLastName());

		// Get updated by user data from user microservice
		if (contactEntity.getUpdatedBy() == contactEntity.getCreatedBy()) {
			contactResponseDTO.setUpdatedBy(userData.getFirstName() + " " + userData.getLastName());
		} else {
			userEntity = userRepository.findById(contactEntity.getUpdatedBy());
			userData = userEntity.get();
			contactResponseDTO.setUpdatedBy(userData.getFirstName() + " " + userData.getLastName());
		}

		// Get form submission data
		if (contactEntity.getFormSubmissionId() != null) {
			HttpResponse formSubmissionResponse = formSubmissionAPIClient
					.getFormSubmission(contactEntity.getFormSubmissionId());
			FormSubmissionsResponseDTO formSubmissionData = MappingUtil
					.mapClientBodyToClass(formSubmissionResponse.getData(), FormSubmissionsResponseDTO.class);
			contactResponseDTO
					.setSubmissionData(MappingUtil.convertJsonNodeToJSONString(formSubmissionData.getSubmissionData()));
		}
		return contactResponseDTO;
	}

	private ContactEntity updateContactNewRequestDTOToContactNewEntity(ContactEntity contactEntity,
			ContactRequestDTO contactRequestDTO) {
		contactEntity.setTitle(contactRequestDTO.getTitle());
		contactEntity.setFirstName(contactRequestDTO.getFirstName());
		contactEntity.setLastName(contactRequestDTO.getLastName());
		contactEntity.setEntityType(contactRequestDTO.getEntityType());
		contactEntity.setEntityId(contactRequestDTO.getEntityId());
		contactEntity.setUpdatedBy(contactRequestDTO.getUpdatedBy());
		contactEntity.setFormId(contactRequestDTO.getFormId());
		return contactRepository.save(contactEntity);
	}

	private ContactEntity contactNewRequestDTOToContactNewEntity(ContactRequestDTO contactRequestDTO) {
		ContactEntity contactEntity = new ContactEntity();
		contactEntity.setTitle(contactRequestDTO.getTitle());
		contactEntity.setFirstName(contactRequestDTO.getFirstName());
		contactEntity.setLastName(contactRequestDTO.getLastName());
		contactEntity.setEntityType(contactRequestDTO.getEntityType());
		contactEntity.setEntityId(contactRequestDTO.getEntityId());
		contactEntity.setCreatedBy(contactRequestDTO.getCreatedBy());
		contactEntity.setUpdatedBy(contactRequestDTO.getUpdatedBy());
		contactEntity.setFormId(contactRequestDTO.getFormId());
		return contactRepository.save(contactEntity);
	}

}
