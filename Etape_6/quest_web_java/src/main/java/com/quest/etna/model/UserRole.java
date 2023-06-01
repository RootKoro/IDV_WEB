package com.quest.etna.model;

public enum UserRole {
	ROLE_USER("ROLE_USER"),
	ROLE_ADMIN("ROLE_ADMIN");
	
	private final String role;

	private UserRole(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
	}
	
	public static UserRole getByRole(String role) {
		for (UserRole userRole : UserRole.values())
			if (userRole.getRole().equals(role))
				return userRole;
		return null;
	}
}
