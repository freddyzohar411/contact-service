package com.avensys.rts.contactservice.payloadnewresponse;

import java.util.List;

import com.avensys.rts.contactservice.payloadnewrequest.user.UserGroupResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * UserResponseDTO
 */
@Setter
@Getter
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
