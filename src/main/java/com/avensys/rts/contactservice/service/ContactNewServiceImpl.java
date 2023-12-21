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
import com.avensys.rts.contactservice.entity.ContactNewEntity;
import com.avensys.rts.contactservice.entity.UserEntity;
import com.avensys.rts.contactservice.payloadnewrequest.ContactNewRequestDTO;
import com.avensys.rts.contactservice.payloadnewrequest.FormSubmissionsRequestDTO;
import com.avensys.rts.contactservice.payloadnewresponse.ContactNewResponseDTO;
import com.avensys.rts.contactservice.payloadnewresponse.FormSubmissionsResponseDTO;
import com.avensys.rts.contactservice.repository.ContactNewRepository;
import com.avensys.rts.contactservice.repository.UserRepository;
import com.avensys.rts.contactservice.util.MappingUtil;

import jakarta.transaction.Transactional;

@Service
public class ContactNewServiceImpl implements ContactNewService {

	private final Logger log = LoggerFactory.getLogger(ContactNewServiceImpl.class);
	private final ContactNewRepository contactNewRepository;

	@Autowired
	private UserAPIClient userAPIClient;

	@Autowired
	private FormSubmissionAPIClient formSubmissionAPIClient;

	@Autowired
	private UserRepository userRepository;

	public ContactNewServiceImpl(ContactNewRepository contactNewRepository, UserAPIClient userAPIClient,
			FormSubmissionAPIClient formSubmissionAPIClient) {
		this.contactNewRepository = contactNewRepository;
		this.userAPIClient = userAPIClient;
		this.formSubmissionAPIClient = formSubmissionAPIClient;
	}

	@Override
	@Transactional
	public ContactNewResponseDTO createContact(ContactNewRequestDTO contactNewRequestDTO) {
		log.info("Creating contact: service");
		System.out.println("Contact: " + contactNewRequestDTO);
		ContactNewEntity savedContactEntity = contactNewRequestDTOToContactNewEntity(contactNewRequestDTO);

		// Save form data to form submission microservice
		FormSubmissionsRequestDTO formSubmissionsRequestDTO = new FormSubmissionsRequestDTO();
		formSubmissionsRequestDTO.setUserId(contactNewRequestDTO.getCreatedBy());
		formSubmissionsRequestDTO.setFormId(contactNewRequestDTO.getFormId());
		formSubmissionsRequestDTO
				.setSubmissionData(MappingUtil.convertJSONStringToJsonNode(contactNewRequestDTO.getFormData()));
		formSubmissionsRequestDTO.setEntityId(savedContactEntity.getId());
		formSubmissionsRequestDTO.setEntityType(contactNewRequestDTO.getEntityType());
		HttpResponse formSubmissionResponse = formSubmissionAPIClient.addFormSubmission(formSubmissionsRequestDTO);
		FormSubmissionsResponseDTO formSubmissionData = MappingUtil
				.mapClientBodyToClass(formSubmissionResponse.getData(), FormSubmissionsResponseDTO.class);

		savedContactEntity.setFormSubmissionId(formSubmissionData.getId());

		return contactNewEntityToContactNewResponseDTO(savedContactEntity);
	}

	@Override
	public ContactNewResponseDTO getContactById(Long id) {
		ContactNewEntity contactEntityFound = contactNewRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Contact not found"));
		return contactNewEntityToContactNewResponseDTO(contactEntityFound);
	}

	@Override
	@Transactional
	public ContactNewResponseDTO updateContact(Long id, ContactNewRequestDTO contactNewRequestDTO) {
		ContactNewEntity contactEntityFound = contactNewRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Contact not found"));
		ContactNewEntity updatedContactEntity = updateContactNewRequestDTOToContactNewEntity(contactEntityFound,
				contactNewRequestDTO);

		// Update form submission
		FormSubmissionsRequestDTO formSubmissionsRequestDTO = new FormSubmissionsRequestDTO();
		formSubmissionsRequestDTO.setUserId(contactNewRequestDTO.getUpdatedBy());
		formSubmissionsRequestDTO.setFormId(contactNewRequestDTO.getFormId());
		formSubmissionsRequestDTO
				.setSubmissionData(MappingUtil.convertJSONStringToJsonNode(contactNewRequestDTO.getFormData()));
		formSubmissionsRequestDTO.setEntityId(updatedContactEntity.getId());
		formSubmissionsRequestDTO.setEntityType(contactNewRequestDTO.getEntityType());
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
		ContactNewEntity contactEntityFound = contactNewRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Contact not found"));
		contactNewRepository.delete(contactEntityFound);
	}

	@Override
	public List<ContactNewResponseDTO> getContactsByEntityTypeAndEntityId(String entityType, Integer entityId) {
		List<ContactNewEntity> contactNewEntityList = contactNewRepository.findByEntityTypeAndEntityId(entityType,
				entityId);
		List<ContactNewResponseDTO> contactNewResponseDTOList = contactNewEntityList.stream()
				.map(this::contactNewEntityToContactNewResponseDTO).toList();
		return contactNewResponseDTOList;
	}

	@Override
	@Transactional
	public void deleteContactsByEntityTypeAndEntityId(String entityType, Integer entityId) {
		List<ContactNewEntity> contactNewEntityList = contactNewRepository.findByEntityTypeAndEntityId(entityType,
				entityId);
		if (!contactNewEntityList.isEmpty()) {
			// Delete each contact form submission before deleting
			contactNewEntityList.forEach(contactNewEntity -> {
				formSubmissionAPIClient.deleteFormSubmission(contactNewEntity.getFormSubmissionId());
				contactNewRepository.delete(contactNewEntity);
			});
		}
	}

	private ContactNewResponseDTO contactNewEntityToContactNewResponseDTO(ContactNewEntity contactNewEntity) {
		ContactNewResponseDTO contactResponseDTO = new ContactNewResponseDTO();
		contactResponseDTO.setId(contactNewEntity.getId());
		contactResponseDTO.setTitle(contactNewEntity.getTitle());
		contactResponseDTO.setFirstName(contactNewEntity.getFirstName());
		contactResponseDTO.setLastName(contactNewEntity.getLastName());
		contactResponseDTO.setCreatedAt(contactNewEntity.getCreatedAt());
		contactResponseDTO.setUpdatedAt(contactNewEntity.getUpdatedAt());
		contactResponseDTO.setEntityType(contactNewEntity.getEntityType());
		contactResponseDTO.setEntityId(contactNewEntity.getEntityId());
		contactResponseDTO.setFormId(contactNewEntity.getFormId());
		contactResponseDTO.setFormSubmissionId(contactNewEntity.getFormSubmissionId());

		// Get created by User data from user microservice
		Optional<UserEntity> userEntity = userRepository.findById(contactNewEntity.getCreatedBy());
		UserEntity userData = userEntity.get();
		contactResponseDTO.setCreatedBy(userData.getFirstName() + " " + userData.getLastName());

		// Get updated by user data from user microservice
		if (contactNewEntity.getUpdatedBy() == contactNewEntity.getCreatedBy()) {
			contactResponseDTO.setUpdatedBy(userData.getFirstName() + " " + userData.getLastName());
		} else {
			userEntity = userRepository.findById(contactNewEntity.getUpdatedBy());
			userData = userEntity.get();
			contactResponseDTO.setUpdatedBy(userData.getFirstName() + " " + userData.getLastName());
		}

		// Get form submission data
		HttpResponse formSubmissionResponse = formSubmissionAPIClient
				.getFormSubmission(contactNewEntity.getFormSubmissionId());
		FormSubmissionsResponseDTO formSubmissionData = MappingUtil
				.mapClientBodyToClass(formSubmissionResponse.getData(), FormSubmissionsResponseDTO.class);
		contactResponseDTO
				.setSubmissionData(MappingUtil.convertJsonNodeToJSONString(formSubmissionData.getSubmissionData()));
		return contactResponseDTO;
	}

	private ContactNewEntity updateContactNewRequestDTOToContactNewEntity(ContactNewEntity contactNewEntity,
			ContactNewRequestDTO contactNewRequestDTO) {
		contactNewEntity.setTitle(contactNewRequestDTO.getTitle());
		contactNewEntity.setFirstName(contactNewRequestDTO.getFirstName());
		contactNewEntity.setLastName(contactNewRequestDTO.getLastName());
		contactNewEntity.setEntityType(contactNewRequestDTO.getEntityType());
		contactNewEntity.setEntityId(contactNewRequestDTO.getEntityId());
		contactNewEntity.setUpdatedBy(contactNewRequestDTO.getUpdatedBy());
		contactNewEntity.setFormId(contactNewRequestDTO.getFormId());
		return contactNewRepository.save(contactNewEntity);
	}

	private ContactNewEntity contactNewRequestDTOToContactNewEntity(ContactNewRequestDTO contactNewRequestDTO) {
		ContactNewEntity contactNewEntity = new ContactNewEntity();
		contactNewEntity.setTitle(contactNewRequestDTO.getTitle());
		contactNewEntity.setFirstName(contactNewRequestDTO.getFirstName());
		contactNewEntity.setLastName(contactNewRequestDTO.getLastName());
		contactNewEntity.setEntityType(contactNewRequestDTO.getEntityType());
		contactNewEntity.setEntityId(contactNewRequestDTO.getEntityId());
		contactNewEntity.setCreatedBy(contactNewRequestDTO.getCreatedBy());
		contactNewEntity.setUpdatedBy(contactNewRequestDTO.getUpdatedBy());
		contactNewEntity.setFormId(contactNewRequestDTO.getFormId());
		return contactNewRepository.save(contactNewEntity);
	}

}
