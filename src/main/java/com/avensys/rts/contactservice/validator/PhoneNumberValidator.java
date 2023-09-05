package com.avensys.rts.contactservice.validator;

import com.avensys.rts.contactservice.APIClient.ContactAPIClient;
import com.avensys.rts.contactservice.annotation.ValidatePhoneNumber;
import com.avensys.rts.contactservice.payloadrequest.ContactInformationDTO;
import com.avensys.rts.contactservice.payloadresponse.CountriesCurrencyDTO;
import com.avensys.rts.contactservice.payloadresponse.MailingAddressResponseDTO;
import com.avensys.rts.contactservice.util.MappingUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class PhoneNumberValidator implements ConstraintValidator<ValidatePhoneNumber, ContactInformationDTO> {
    @Autowired
    private ContactAPIClient contactAPIClient;

    @Override
    public void initialize(ValidatePhoneNumber constraintAnnotation) {
    }

    @Override
    public boolean isValid(ContactInformationDTO contactInformation, ConstraintValidatorContext context) {
        if (contactInformation.getLandlineNumber() == null) {
            return true;
        }

        Integer landlineNumber = contactInformation.getLandlineNumber();
        Integer landlineCountry = contactInformation.getLandlineCountry();
        CountriesCurrencyDTO countryData = MappingUtil.mapClientBodyToClass(contactAPIClient.getCountryCurrencyById(landlineCountry).getData(), CountriesCurrencyDTO.class);

        String phoneNumberStr = landlineNumber.toString();
        String countryCode = countryData.getIso2();

        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber phoneNumber = phoneUtil.parse(phoneNumberStr, countryCode);

            if (phoneUtil.isValidNumber(phoneNumber)) {
                if (countryCode.equals(phoneUtil.getRegionCodeForNumber(phoneNumber))) {
                    System.out.println("This is a valid number.");
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return false;
    }
}
