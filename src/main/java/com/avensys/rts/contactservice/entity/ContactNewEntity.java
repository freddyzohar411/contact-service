package com.avensys.rts.contactservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity(name = "contactNew")
@Table(name = "contact_new")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactNewEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title", length = 5 )
    private String title;

    @Column(name = "first_name", length = 50 )
    private String firstName;

    @Column(name = "last_name", length = 50 )
    private String lastName;

//    @Column(name = "designation", length = 30 )
//    private String designation;
//
//    @Column(name = "department", length = 30 )
//    private String department;
//
//    @Column(name = "industry", length = 30 )
//    private String industry;
//
//    @Column(name = "sub_industry", length = 30)
//    private String subIndustry;
//
//    @Column(name = "mobile_country")
//    private int mobileCountry;
//
//    @Column(name = "mobile_number")
//    private int mobileNumber;
//
//    @Column(name = "landline_country")
//    private int landlineCountry;
//
//    @Column(name = "landline_number")
//    private int landlineNumber;
//
//    @Column(name = "email", length = 250)
//    private String email;
//
//    @Column(name = "address")
//    private Integer address;
//
//    @Column(name = "remarks", length = 250)
//    private String remarks;

    @Column(name = "entity_id")
    private int entityId;

    @Column(name="entity_type", length = 30)
    private String entityType;

    @Column (name = "created_by")
    private Integer createdBy;

    @CreationTimestamp
    @Column (name = "created_at")
    private LocalDateTime createdAt;

    @Column (name = "updated_by")
    private Integer updatedBy;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name= "form_id")
    private Integer formId;

    @Column(name = "form_submission_id")
    private Integer formSubmissionId;

}