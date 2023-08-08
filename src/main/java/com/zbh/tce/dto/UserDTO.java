package com.zbh.tce.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zbh.tce.entity.enums.GenderEnum;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

	@NotBlank(message = "Telephone number cannot be empty")
	@Size(max = 20, message = "Telephone number cannot be more than 20")
	private String telephone;

	@NotNull(message = "Gender cannot be empty")
	private GenderEnum gender;

	private String createdDate;

	@NotNull(message = "Role cannot be empty")
	private Set<RoleDTO> roles;

}
