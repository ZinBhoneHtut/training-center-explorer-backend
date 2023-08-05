package com.zbh.tce.common.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {
	
	ADMIN("Admin", "Highest role in the system."),
	MODERATOR("Moderator", "For content editors."),
	USER("User",  "For end users.");
	
	private final String roleName;
	private final String description;
	
	RoleEnum(String roleName, String description) {
		this.roleName = roleName;
		this.description = description;
	}
}
