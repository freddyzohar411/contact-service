package com.avensys.rts.contactservice.payloadrequest;

import com.avensys.rts.contactservice.annotation.ValidatePhoneNumber;
import com.avensys.rts.contactservice.annotation.ValidateString;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * author: Koh He Xiang
 * This is the DTO class for the Contact Information request
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ValidatePhoneNumber
public class ContactInformationDTO {

    @NotEmpty
    @ValidateString(acceptedValues = {"Mr", "Mrs", "Ms"}, message = "Title must be either Mr, Mrs or Ms")
    @Length(max = 5)
    private String title;

    @NotEmpty
    @Length(max = 50)
    private String firstName;

    @NotEmpty
    @Length(max = 50)
    private String lastName;

    @Length(max = 20)
    private String designation;

    @Length(max = 20)
    private String department;

    @Length(max = 20)
    private String industry;

    @Length(max = 20)
    private String subIndustry;

    private Integer mobileCountry;

    private Integer mobileNumber;

    private Integer landlineCountry;

    private Integer landlineNumber;

    @Email
    @Length(max = 250)
    private String email;

}
