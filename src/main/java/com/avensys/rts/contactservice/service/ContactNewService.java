package com.avensys.rts.contactservice.service;

import com.avensys.rts.contactservice.payloadnewrequest.ContactNewRequestDTO;
import com.avensys.rts.contactservice.payloadnewresponse.ContactNewResponseDTO;

import java.util.List;

public interface ContactNewService {

	ContactNewResponseDTO createContact(ContactNewRequestDTO contactNewRequestDTO);

	ContactNewResponseDTO getContactById(Long id);

	ContactNewResponseDTO updateContact(Long id, ContactNewRequestDTO contactNewRequestDTO);

	void deleteContact(Long id);

	List<ContactNewResponseDTO> getContactsByEntityTypeAndEntityId(String entityType, Integer entityId);

	void deleteContactsByEntityTypeAndEntityId(String entityType, Integer entityId);
}
