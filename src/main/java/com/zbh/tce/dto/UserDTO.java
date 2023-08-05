package com.zbh.tce.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zbh.tce.entity.audit.Audit;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * 
 * @author ZinBhoneHtut
 *
 */
@Data
@JsonIgnoreProperties(value = {"password", "confirmPassword"}, allowSetters = true)
public class UserDTO {

	private long id;
	@NotBlank(message = "Username cannot be empty")
	@Size(min = 2, message = "Username should have at least 2 characters")
	@Size(max = 60, message = "Username cannot be more than 60 characters")
	private String name;

	@Email
	@NotBlank(message = "Email cannot be empty")
	private String email;

	private Audit audit;

	@NotBlank(message = "Password cannot be empty")
	private String password;

	@NotBlank(message = "Confirm password cannot be empty")
	private String confirmPassword;

	private Set<RoleDTO> roles;
}
