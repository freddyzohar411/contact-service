package com.avensys.rts.contactservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.avensys.rts.contactservice.constant.MessageConstants;
import com.avensys.rts.contactservice.payloadnewrequest.ContactRequestDTO;
import com.avensys.rts.contactservice.payloadnewresponse.ContactResponseDTO;
import com.avensys.rts.contactservice.service.ContactServiceImpl;
import com.avensys.rts.contactservice.util.JwtUtil;
import com.avensys.rts.contactservice.util.ResponseUtil;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ContactController {

	private final Logger log = LoggerFactory.getLogger(ContactController.class);
	private final ContactServiceImpl contactService;
	private final MessageSource messageSource;

	@Autowired
	private JwtUtil jwtUtil;

	public ContactController(ContactServiceImpl contactService, MessageSource messageSource) {
		this.contactService = contactService;
		this.messageSource = messageSource;
	}

	@PostMapping("/contacts")
	public ResponseEntity<Object> createContact(@Valid @RequestBody ContactRequestDTO contactRequest,
			@RequestHeader(name = "Authorization") String token) {
		log.info("Create a contact : Controller ");
		Long userId = jwtUtil.getUserId(token);
		contactRequest.setCreatedBy(userId);
		contactRequest.setUpdatedBy(userId);
		ContactResponseDTO createdContact = contactService.createContact(contactRequest);
		return ResponseUtil.generateSuccessResponse(createdContact, HttpStatus.CREATED,
				messageSource.getMessage(MessageConstants.MESSAGE_CREATED, null, LocaleContextHolder.getLocale()));
	}

	@GetMapping("/contacts/entity/{entityType}/{entityId}")
	public ResponseEntity<Object> getContactsByEntityTypeAndEntityId(@PathVariable String entityType,
			@PathVariable Integer entityId) {
		log.info("Get contacts by entity type and entity id : Controller ");
		return ResponseUtil.generateSuccessResponse(
				contactService.getContactsByEntityTypeAndEntityId(entityType, entityId), HttpStatus.OK,
				messageSource.getMessage(MessageConstants.MESSAGE_SUCCESS, null, LocaleContextHolder.getLocale()));
	}

	@DeleteMapping("/contacts/{id}")
	public ResponseEntity<Object> deleteContact(@PathVariable Long id) {
		log.info("Delete contact : Controller ");
		contactService.deleteContact(id);
		return ResponseUtil.generateSuccessResponse(null, HttpStatus.OK,
				messageSource.getMessage(MessageConstants.MESSAGE_SUCCESS, null, LocaleContextHolder.getLocale()));
	}

	@PutMapping("/contacts/{id}")
	public ResponseEntity<Object> updateContact(@PathVariable Long id,
			@Valid @RequestBody ContactRequestDTO contactRequest,
			@RequestHeader(name = "Authorization") String token) {
		log.info("Update contact : Controller ");
		Long userId = jwtUtil.getUserId(token);
		contactRequest.setUpdatedBy(userId);
		ContactResponseDTO updatedContact = contactService.updateContact(id, contactRequest);
		return ResponseUtil.generateSuccessResponse(updatedContact, HttpStatus.OK,
				messageSource.getMessage(MessageConstants.MESSAGE_SUCCESS, null, LocaleContextHolder.getLocale()));
	}

	/**
	 * This endpoint is to delete contacts by entity type and entity id
	 * 
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	@DeleteMapping("/contacts/entity/{entityType}/{entityId}")
	public ResponseEntity<Object> deleteContactsByEntityTypeAndEntityId(@PathVariable String entityType,
			@PathVariable Integer entityId) {
		log.info("Delete contacts by entity type and entity id : Controller ");
		contactService.deleteContactsByEntityTypeAndEntityId(entityType, entityId);
		return ResponseUtil.generateSuccessResponse(null, HttpStatus.OK,
				messageSource.getMessage(MessageConstants.MESSAGE_SUCCESS, null, LocaleContextHolder.getLocale()));
	}
}
