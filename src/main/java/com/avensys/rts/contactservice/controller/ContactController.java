package com.avensys.rts.contactservice.controller;

import com.avensys.rts.contactservice.constant.MessageConstants;
import com.avensys.rts.contactservice.payloadrequest.ContactGetAllRequestDTO;
import com.avensys.rts.contactservice.payloadrequest.ContactListRequestDTO;
import com.avensys.rts.contactservice.payloadrequest.ContactRequestDTO;
import com.avensys.rts.contactservice.payloadresponse.ContactResponseDTO;
import com.avensys.rts.contactservice.service.ContactServiceImpl;
import com.avensys.rts.contactservice.util.ResponseUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/***
 * @author Koh He Xiang
 * This class is used to define the endpoints for the Contact controller
 */
//@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ContactController {

    private final Logger log = LoggerFactory.getLogger(ContactController.class);
    private final ContactServiceImpl contactService;
    private final MessageSource messageSource;

    public ContactController(ContactServiceImpl contactService, MessageSource messageSource) {
        this.contactService = contactService;
        this.messageSource = messageSource;
    }

    /**
     * This method is used to create a new contact
     * @param contactRequest
     * @return
     */
    @PostMapping("/contacts")
    public ResponseEntity<Object> createContact(@Valid @RequestBody ContactRequestDTO contactRequest) {
        log.info("Create a contact : Controller ");
        ContactResponseDTO createdContact = contactService.createContact(contactRequest);
        return ResponseUtil.generateSuccessResponse(createdContact, HttpStatus.CREATED, messageSource.getMessage(MessageConstants.MESSAGE_CREATED, null, LocaleContextHolder.getLocale()));
    }

    /**
     * This method is used to create a new contact based on a list
     * @param contactListRequest
     * @return
     */
    @PostMapping("/contactsList")
    public ResponseEntity<Object> createContacts(@Valid @RequestBody ContactListRequestDTO contactListRequest) {
        log.info("Create contact list : Controller ");
        contactService.createContacts(contactListRequest);
        return ResponseUtil.generateSuccessResponse(null, HttpStatus.CREATED, messageSource.getMessage(MessageConstants.MESSAGE_CREATED, null, LocaleContextHolder.getLocale()));
    }

    /**
     * This method is used to update a list of contacts
     * @param contactListRequest
     * @return
     */
    @PutMapping("/contactsList")
    public ResponseEntity<Object> updateContacts(@Valid @RequestBody ContactListRequestDTO contactListRequest) {
        log.info("Update list of contacts : Controller ");
        contactService.updateContacts(contactListRequest);
        return ResponseUtil.generateSuccessResponse(null, HttpStatus.CREATED, messageSource.getMessage(MessageConstants.MESSAGE_CREATED, null, LocaleContextHolder.getLocale()));
    }

    /**
     * This method is used to get contact by id
     * @param contactId
     * @return
     */
    @GetMapping("/contacts/{contactId}")
    public ResponseEntity<Object> getContacts(@PathVariable int contactId) {
        log.info("Get contact by Id : Controller ");
        ContactResponseDTO contact = contactService.getContactById(contactId);
        return ResponseUtil.generateSuccessResponse(contact, HttpStatus.OK, messageSource.getMessage(MessageConstants.MESSAGE_SUCCESS, null, LocaleContextHolder.getLocale()));
    }

    /**
     * This method is used to get all contacts by source and entity id
     * @param contactGetAllRequest
     * @return
     */
    @PostMapping("/contacts-by-entity-and-type")
    public ResponseEntity<Object> getContactsByEntityAndType(@RequestBody ContactGetAllRequestDTO contactGetAllRequest) {
        log.info("Get contact by entity type and entity Id : Controller ");
        List<ContactResponseDTO> contacts = contactService.getContactsByEntityAndType(contactGetAllRequest);
        return ResponseUtil.generateSuccessResponse(contacts, HttpStatus.OK, messageSource.getMessage(MessageConstants.MESSAGE_SUCCESS, null, LocaleContextHolder.getLocale()));
    }

    /**
     * This method is used to update contact by id
     * @param contactId
     * @param contact
     * @return
     */
    @PutMapping("/contacts/{contactId}")
    public ResponseEntity<Object> updateContact(@PathVariable int contactId, @Valid @RequestBody ContactRequestDTO contact) {
        log.info("Update contact by Id : Controller ");
        ContactResponseDTO updatedContact = contactService.updateContact(contactId, contact);
        return ResponseUtil.generateSuccessResponse(updatedContact, HttpStatus.OK, "Contact updated successfully");
    }

    /**
     * This method is used to delete contact by id
     * @param contactId
     * @return
     */
    @DeleteMapping("/contacts/{contactId}")
    public ResponseEntity<Object> deleteContact(@PathVariable int contactId) {
        log.info("Delete contact by Id : Controller ");
        contactService.deleteContact(contactId);
        return ResponseUtil.generateSuccessResponse(null, HttpStatus.OK, "Contact deleted successfully");
    }


}
