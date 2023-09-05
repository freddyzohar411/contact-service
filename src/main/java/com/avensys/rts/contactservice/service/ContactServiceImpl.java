package com.avensys.rts.contactservice.service;

import com.avensys.rts.contactservice.APIClient.AddressAPIClient;
import com.avensys.rts.contactservice.APIClient.ContactAPIClient;
import com.avensys.rts.contactservice.customresponse.HttpResponse;
import com.avensys.rts.contactservice.entity.ContactEntity;
import com.avensys.rts.contactservice.payloadrequest.*;
import com.avensys.rts.contactservice.payloadresponse.AddressResponseDTO;
import com.avensys.rts.contactservice.payloadresponse.ContactInformationResponseDTO;
import com.avensys.rts.contactservice.payloadresponse.ContactResponseDTO;
import com.avensys.rts.contactservice.payloadresponse.MailingAddressResponseDTO;
import com.avensys.rts.contactservice.repository.ContactRepository;
import com.avensys.rts.contactservice.util.MappingUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Koh He Xiang
 * This class is used to implement the methods for the AddressService
 */
@Service
public class ContactServiceImpl implements ContactService {

    private final Logger log = LoggerFactory.getLogger(ContactServiceImpl.class);
    private final ContactRepository contactRepository;

    @Autowired
    private AddressAPIClient addressAPIClient;

    @Autowired
    private ContactAPIClient contactAPIClient;

    public ContactServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    /**
     * This method is used to create a contact
     *
     * @param contactRequest
     * @return ContactResponseDTO
     */
    @Override
    @Transactional
    public ContactResponseDTO createContact(ContactRequestDTO contactRequest) {

        ContactEntity contactEntity = new ContactEntity();

        // Set contact information
        ContactInformationDTO contactInformationDTO = contactRequest.getContactInformation();
        setContactInformationToContactEntity(contactEntity, contactInformationDTO);

        // Set contact source
        contactEntity.setEntityType(contactRequest.getEntityType());
        contactEntity.setEntityId(contactRequest.getEntityId());

        // Set contact remarks
        contactEntity.setRemarks(contactRequest.getContactRemarks());

        // Save contact and get id
        ContactEntity savedContact = contactRepository.save(contactEntity);

        //Set Address
        MailingAddressDTO addressRequest = contactRequest.getMailingAddress();
        AddressRequestDTO addressRequestDTO = contactRequestToAddressRequest(addressRequest, 0, "job", savedContact.getId());
        HttpResponse mailAddressResponse = addressAPIClient.createAddress(addressRequestDTO);
        AddressResponseDTO mailAddressData = MappingUtil.mapClientBodyToClass(mailAddressResponse.getData(), AddressResponseDTO.class);
        savedContact.setAddress(mailAddressData.getId());

        log.info("Contact created : Service");
        return contactEntityToContactResponseDTO(savedContact);
    }

    /**
     * This method is used to create a list of contacts
     *
     * @param contactRequests
     */
    @Override
    public void createContacts(ContactListRequestDTO contactRequests) {

        List<ContactRequestDTO> contactRequestList = contactRequests.getContactList();
        int entityId = contactRequests.getEntityId();
        String entityType = contactRequests.getEntityType();

        contactRequestList.forEach(contactRequest -> {
            ContactEntity contactEntity = new ContactEntity();

            // Set contact information
            setContactInformationToContactEntity(contactEntity, contactRequest.getContactInformation());

            // Set contact remarks
            contactEntity.setRemarks(contactRequest.getContactRemarks());

            // Save contact and get id
            ContactEntity savedContact = contactRepository.save(contactEntity);

            //Set Address
            MailingAddressDTO addressRequest = new MailingAddressDTO();
            addressRequest = contactRequest.getMailingAddress();
            AddressRequestDTO addressRequestDTO = contactRequestToAddressRequest(addressRequest, 0, "job", savedContact.getId());
            HttpResponse mailAddressResponse = addressAPIClient.createAddress(addressRequestDTO);
            AddressResponseDTO mailAddressData = MappingUtil.mapClientBodyToClass(mailAddressResponse.getData(), AddressResponseDTO.class);

            // Save Entity id and source
            savedContact.setEntityId(entityId);
            savedContact.setEntityType(entityType);

            savedContact.setAddress(mailAddressData.getId());
            contactRepository.save(contactEntity);
        });
    }

    @Override
    @Transactional
    public void updateContacts(ContactListRequestDTO contactRequests) {
        List<ContactRequestDTO> contactRequestList = contactRequests.getContactList();
        int entityId = contactRequests.getEntityId();
        String entityType = contactRequests.getEntityType();

        contactRequestList.forEach(contactRequest -> {
            // If no id, create new contact
            if (contactRequest.getId() == null) {
                ContactEntity contactEntity = new ContactEntity();
                // Set contact information
                setContactInformationToContactEntity(contactEntity, contactRequest.getContactInformation());

                // Set contact remarks
                contactEntity.setRemarks(contactRequest.getContactRemarks());

                // Save contact and get id
                ContactEntity savedContact = contactRepository.save(contactEntity);

                //Set Address
                MailingAddressDTO addressRequest = new MailingAddressDTO();
                addressRequest = contactRequest.getMailingAddress();
                AddressRequestDTO addressRequestDTO = contactRequestToAddressRequest(addressRequest, 0, "job", savedContact.getId());
                HttpResponse mailAddressResponse = addressAPIClient.createAddress(addressRequestDTO);
            } else {
                // Update contact
                ContactEntity contactUpdate = contactRepository.findById(contactRequest.getId()).orElseThrow(
                        () -> new EntityNotFoundException("Contact with id " + contactRequest.getId() + " not found")
                );

                // Set contact information
                setContactInformationToContactEntity(contactUpdate, contactRequest.getContactInformation());

                // Set contact remarks
                contactUpdate.setRemarks(contactRequest.getContactRemarks());

                //Set Address
                MailingAddressDTO addressRequest = contactRequest.getMailingAddress();
                AddressRequestDTO addressRequestDTO = contactRequestToAddressRequest(addressRequest, 0, "job", contactUpdate.getId());
                HttpResponse mailAddressResponse = addressAPIClient.updateAddress(contactUpdate.getAddress(), addressRequestDTO);
                AddressResponseDTO mailAddressData = MappingUtil.mapClientBodyToClass(mailAddressResponse.getData(), AddressResponseDTO.class);
                contactUpdate.setAddress(mailAddressData.getId());

                contactRepository.save(contactUpdate);
            }
        });

    }

    /**
     * This method is used to get contact by id
     * @param id
     * @return ContactEntity
     */
    @Override
    public ContactResponseDTO getContactById(int id) {
        ContactEntity contactEntity = contactRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Address with id " + id + " not found")
        );
        log.info("Contact retrieved : Service");
        return contactEntityToContactResponseDTO(contactEntity);
    }

    /**
     * This method is used to get all contacts by source and entity id
     * @param contactGetAllRequestDTO
     * @return
     */
    @Override
    public List<ContactResponseDTO> getContactsByEntityAndType(ContactGetAllRequestDTO contactGetAllRequestDTO) {
        List<ContactEntity> contactEntityList = contactRepository.findByEntityIdAndEntityType(contactGetAllRequestDTO.getEntityId(), contactGetAllRequestDTO.getEntityType());
        List<ContactResponseDTO> contactResponseDTOList = null;
        return contactEntityList.stream().map(this::contactEntityToContactResponseDTO).toList();
    }

    /**
     * This method is used to update contact by id
     * @param contactId
     * @param contactRequest
     * @return ContactResponseDTO
     */
    @Override
    public ContactResponseDTO updateContact(int contactId, ContactRequestDTO contactRequest) {
        ContactEntity contactUpdate = contactRepository.findById(contactId).orElseThrow(
                () -> new EntityNotFoundException("Contact with id " + contactId + " not found")
        );

        // Set contact information
        ContactInformationDTO contactInformationDTO = contactRequest.getContactInformation();
        setContactInformationToContactEntity(contactUpdate, contactInformationDTO);

        // Set contact remarks
        contactUpdate.setRemarks(contactRequest.getContactRemarks());

        //Set Address
        MailingAddressDTO addressRequest = contactRequest.getMailingAddress();
        AddressRequestDTO addressRequestDTO = contactRequestToAddressRequest(addressRequest, 0, "job", contactUpdate.getId());
        HttpResponse mailAddressResponse = addressAPIClient.updateAddress(contactUpdate.getAddress(), addressRequestDTO);
        AddressResponseDTO mailAddressData = MappingUtil.mapClientBodyToClass(mailAddressResponse.getData(), AddressResponseDTO.class);
        contactUpdate.setAddress(mailAddressData.getId());

        contactRepository.save(contactUpdate);

        log.info("Contact updated : Service");
        return contactEntityToContactResponseDTO(contactUpdate);
    }

    @Override
    public void deleteContact(int contactId) {
        ContactEntity contactFound = contactRepository.findById(contactId).orElseThrow(
                () -> new EntityNotFoundException("Contact with id " + contactId + " not found")
        );

        // Delete address
        addressAPIClient.deleteAddressById(contactFound.getAddress());

        //Delete contact
        contactRepository.delete(contactFound);

        log.info("Address deleted : Service");
    }

    /**
     * This method is used to convert ContactEntity to ContactResponseDTO
     * @param contactEntity
     * @return
     */
    private ContactResponseDTO contactEntityToContactResponseDTO(ContactEntity contactEntity) {
        ContactResponseDTO contactResponseDTO = new ContactResponseDTO();

        // Get contact information
        ContactInformationResponseDTO contactInformation = new ContactInformationResponseDTO();
        contactInformation.setTitle(contactEntity.getTitle());
        contactInformation.setFirstName(contactEntity.getFirstName());
        contactInformation.setLastName(contactEntity.getLastName());
        contactInformation.setDesignation(contactEntity.getDesignation());
        contactInformation.setDepartment(contactEntity.getDepartment());
        contactInformation.setIndustry(contactEntity.getIndustry());
        contactInformation.setSubIndustry(contactEntity.getSubIndustry());
        contactInformation.setMobileCountry(contactEntity.getMobileCountry());
        contactInformation.setMobileNumber(contactEntity.getMobileNumber());
        contactInformation.setLandlineCountry(contactEntity.getLandlineCountry());
        contactInformation.setLandlineNumber(contactEntity.getLandlineNumber());
        contactInformation.setEmail(contactEntity.getEmail());

        contactResponseDTO.setContactInformation(contactInformation);

        // Get address Information
        if (contactEntity.getAddress() != null) {
            MailingAddressResponseDTO mailingAddressData = MappingUtil.mapClientBodyToClass(addressAPIClient.getAddressById(contactEntity.getAddress()).getData(), MailingAddressResponseDTO.class);
            contactResponseDTO.setMailingAddress(mailingAddressData);
        }

        // Get contact remarks
        contactResponseDTO.setContactRemarks(contactEntity.getRemarks());

        return contactResponseDTO;
    }


    /**
     * This method is used to set contact information to contact entity
     * @param contactEntity
     * @param contactInformationDTO
     */
    private void setContactInformationToContactEntity(ContactEntity contactEntity, ContactInformationDTO contactInformationDTO) {
        // Set contact information
        contactEntity.setTitle(contactInformationDTO.getTitle());
        contactEntity.setFirstName(contactInformationDTO.getFirstName());
        contactEntity.setLastName(contactInformationDTO.getLastName());
        contactEntity.setDesignation(contactInformationDTO.getDesignation());
        contactEntity.setDepartment(contactInformationDTO.getDepartment());
        contactEntity.setIndustry(contactInformationDTO.getIndustry());
        contactEntity.setSubIndustry(contactInformationDTO.getSubIndustry());
        contactEntity.setMobileCountry(contactInformationDTO.getMobileCountry());
        contactEntity.setMobileNumber(contactInformationDTO.getMobileNumber());
        contactEntity.setLandlineCountry(contactInformationDTO.getLandlineCountry());
        contactEntity.setLandlineNumber(contactInformationDTO.getLandlineNumber());
        contactEntity.setEmail(contactInformationDTO.getEmail());
    }

    /**
     * Internal method is used to convert ContactRequestDTO to AddressRequestDTO
     * @param addressRequest
     * @param type
     * @param EntityType
     * @param entityId
     * @return
     */
    private AddressRequestDTO contactRequestToAddressRequest(MailingAddressDTO addressRequest, int type, String EntityType, int entityId) {
        AddressRequestDTO addressRequestDTO = new AddressRequestDTO();
        addressRequestDTO.setLine1(addressRequest.getLine1());
        addressRequestDTO.setLine2(addressRequest.getLine2());
        addressRequestDTO.setLine3(addressRequest.getLine3());
        addressRequestDTO.setCity(addressRequest.getCity());
        addressRequestDTO.setCountry(addressRequest.getCountry());
        addressRequestDTO.setPostalCode(addressRequest.getPostalCode());
        addressRequestDTO.setType((short) type);
        addressRequestDTO.setEntityId(entityId);
        addressRequestDTO.setEntityType(EntityType);
        return addressRequestDTO;
    }


}
