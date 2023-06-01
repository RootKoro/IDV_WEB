package com.quest.etna.model;

public class UserDetails {
	private String username;
	private UserRole role = UserRole.ROLE_USER;
	
	public UserDetails() {}
	
	public UserDetails(String username) {
		this.setUsername(username);
	}
	
	public UserDetails(String username, UserRole role) {
		this.setUsername(username);
		this.setRole(role);
	}
	
	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }
	public UserRole getRole() { return role; }
	public void setRole(UserRole role) { this.role = role; }
}
