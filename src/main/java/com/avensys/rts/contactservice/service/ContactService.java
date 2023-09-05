package com.avensys.rts.contactservice.service;

import com.avensys.rts.contactservice.payloadrequest.ContactGetAllRequestDTO;
import com.avensys.rts.contactservice.payloadrequest.ContactListRequestDTO;
import com.avensys.rts.contactservice.payloadrequest.ContactRequestDTO;
import com.avensys.rts.contactservice.payloadresponse.ContactResponseDTO;

import java.util.List;

/**
 * @author Koh He Xiang
 * This interface is used to define the methods for the contact service
 */
public interface ContactService {

    /**
     * This method is used to create a new contact
     * @param contactRequest
     * @return
     */
    ContactResponseDTO createContact(ContactRequestDTO contactRequest);

    /**
     * This method is used to create a new contact based on a list
     * @param contactRequests
     * @return
     */
    void createContacts(ContactListRequestDTO contactRequests);

    /**
     * This method is used to update contacts based on a list
     * @param contactRequests
     */
    void updateContacts(ContactListRequestDTO contactRequests);

    /**
     * This method is used to get contact by id
     * @param contactId
     * @return
     */
    ContactResponseDTO getContactById(int contactId);

    /**
     * This method is used to get all contacts by source and entity id
     * @param contactGetAllRequestDTO
     * @return
     */
    List<ContactResponseDTO> getContactsByEntityAndType(ContactGetAllRequestDTO contactGetAllRequestDTO);

    /**
     * This method is used to update contact by id
     * @param contactId
     * @param contactRequest
     * @return
     */
    ContactResponseDTO updateContact(int contactId, ContactRequestDTO contactRequest);

    /**
     * This method is used to delete contact by id
     * @param contactId
     */
    void deleteContact(int contactId);
}
