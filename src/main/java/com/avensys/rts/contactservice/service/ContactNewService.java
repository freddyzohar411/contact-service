package com.avensys.rts.contactservice.service;

import com.avensys.rts.contactservice.payloadnewrequest.ContactNewRequestDTO;
import com.avensys.rts.contactservice.payloadnewresponse.ContactNewResponseDTO;

import java.util.List;

public interface ContactNewService {

    ContactNewResponseDTO createContact(ContactNewRequestDTO contactNewRequestDTO);

    ContactNewResponseDTO getContactById(Integer id);

    ContactNewResponseDTO updateContact(Integer id, ContactNewRequestDTO contactNewRequestDTO);

    void deleteContact(Integer id);

    List<ContactNewResponseDTO> getContactsByEntityTypeAndEntityId(String entityType, Integer entityId);

}
