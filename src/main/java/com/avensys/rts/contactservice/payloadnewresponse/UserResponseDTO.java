package com.avensys.rts.contactservice.payloadnewresponse;

import com.avensys.rts.contactservice.payloadnewrequest.user.UserGroupResponseDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * UserResponseDTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponseDTO {
    private Long id;
    private String keycloackId;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String mobile;
    private String employeeId;
    private Boolean locked;
    private Boolean enabled;
    private List<UserGroupResponseDTO> userGroup;
}
