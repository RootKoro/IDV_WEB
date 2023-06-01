package com.quest.etna.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class JwtUserDetails implements org.springframework.security.core.userdetails.UserDetails {
	private static final long serialVersionUID = 1L;
	private User user;
	private UserRole role = UserRole.ROLE_USER;
	
	public JwtUserDetails(User user) {
		this.user = user;
		//
	}
	
	public String getUsername() { return user.getUsername(); }
	public void setUsername(String username) { this.user.setUsername(username); }
	public UserRole getRole() { return role; }
	public void setRole(String role) { this.role = UserRole.getByRole(role); }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(this.user.getRole());
		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		authorities.add(authority);
		return authorities;
	}

	@Override
	public String getPassword() { return user.getPassword(); }

	@Override
	public boolean isAccountNonExpired() { return true; }

	@Override
	public boolean isAccountNonLocked() { return true; }

	@Override
	public boolean isCredentialsNonExpired() { return true; }

	@Override
	public boolean isEnabled() { return true; }
}
