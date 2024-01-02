package com.avensys.rts.contactservice.service;

import com.avensys.rts.contactservice.payloadnewrequest.ContactRequestDTO;
import com.avensys.rts.contactservice.payloadnewresponse.ContactResponseDTO;

import java.util.List;

public interface ContactService {

	ContactResponseDTO createContact(ContactRequestDTO contactRequestDTO);

	ContactResponseDTO getContactById(Long id);

	ContactResponseDTO updateContact(Long id, ContactRequestDTO contactRequestDTO);

	void deleteContact(Long id);

	List<ContactResponseDTO> getContactsByEntityTypeAndEntityId(String entityType, Integer entityId);

	void deleteContactsByEntityTypeAndEntityId(String entityType, Integer entityId);
}
