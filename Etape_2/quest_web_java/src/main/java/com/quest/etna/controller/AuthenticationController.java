package com.quest.etna.controller;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.quest.etna.model.User;
import com.quest.etna.model.UserDetails;
import com.quest.etna.repositories.UserRepository;

@RestController
public class AuthenticationController {
	private static UserRepository userRepository;
	
	@Autowired
	private UserRepository ur;
	
	@PostConstruct
	private void init() {
		userRepository = this.ur;
	}	

	@PostMapping(value="/register")
	public ResponseEntity<?> register(@RequestBody User req) {
		try {
			User user = new User(req.getUsername().toLowerCase(), req.getPassword());
			Optional<User> existing = userRepository.findByUsername(req.getUsername().toLowerCase());
			if (existing.isEmpty()) {
				UserDetails resp = new UserDetails(req.getUsername());
				AuthenticationController.userRepository.save(user);
				return ResponseEntity.status(HttpStatus.CREATED).header("Content-Type", "application/json").body(resp);
			} else {
				String resp = "{\"message\": \"This user already exists!\", \"status\": 409}";
				return ResponseEntity.status(HttpStatus.CONFLICT).header("Content-Type", "application/json").body(resp);
			}
		} catch (Exception e) {
			String resp = "{\"message\": \"Try again with some valid username and password!\", \"status\": 400}";
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(resp);
		}
	}
}
