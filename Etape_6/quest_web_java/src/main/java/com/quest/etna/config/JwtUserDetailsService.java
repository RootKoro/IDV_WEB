package com.quest.etna.config;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.User;
import com.quest.etna.repositories.UserRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService{
	private static UserRepository userRepository;
	
	@Autowired
	private UserRepository ur;
	
	@PostConstruct
	private void init() {
		userRepository = this.ur;
	}	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByUsername(username.toLowerCase());
		return new JwtUserDetails(user.get());
	}
}
