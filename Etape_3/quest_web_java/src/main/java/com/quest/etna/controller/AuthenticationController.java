package com.quest.etna.controller;

import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.quest.etna.config.JwtTokenUtil;
import com.quest.etna.config.JwtUserDetailsService;
import com.quest.etna.config.WebSecurityConfig;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.User;
import com.quest.etna.model.UserDetails;
import com.quest.etna.repositories.UserRepository;

@RestController
public class AuthenticationController {
				private static UserRepository userRepository;
	@Autowired	private JwtUserDetailsService jwtUserDetailsService;
	@Autowired	private AuthenticationManager authenticationManager;
	@Autowired	private JwtTokenUtil jwtTokenUtil;
	@Autowired	private JwtUserDetailsService userDetailsService;
	@Autowired	private WebSecurityConfig webSecurityConfig;
	@Autowired	private UserRepository ur;
	
	@PostConstruct
	private void init() { userRepository = this.ur; }

	@PostMapping(value="/register")
	public ResponseEntity<?> register(@RequestBody User req) {
		try {
			final String password = webSecurityConfig.passwordEncoder().encode(req.getPassword());
			User user = new User(req.getUsername().toLowerCase(), password);
			Optional<User> existing = userRepository.findByUsername(req.getUsername().toLowerCase());
			if (existing.isEmpty()) {
				final UserDetails resp = new UserDetails(req.getUsername());
				AuthenticationController.userRepository.save(user);
				return ResponseEntity.status(HttpStatus.CREATED).header("Content-Type", "application/json").body(resp);
			} else {
				String resp = "{\"message\": \"This user already exists!\", \"status\": 409}";
				return ResponseEntity.status(HttpStatus.CONFLICT).header("Content-Type", "application/json").body(resp);
			}
		} catch (Exception e) {
			String resp = "{\"message\": \"Try again with some username and password!\", \"status\": 400}";
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(resp);
		}
	}	

	@PostMapping(value="/authenticate")
	public ResponseEntity<?> authenticate(@RequestBody User req) {
		try {
			final JwtUserDetails userDetails = (JwtUserDetails) userDetailsService.loadUserByUsername(req.getUsername().toLowerCase());
			if (userDetails.getUsername() != null) {
				if (webSecurityConfig.passwordEncoder().matches(req.getPassword(), userDetails.getPassword())) {
					authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDetails.getUsername(), req.getPassword(), userDetails.getAuthorities()));
					final String token = jwtTokenUtil.generateToken(userDetails);
					HttpHeaders header = new HttpHeaders();
					header.setContentType(MediaType.APPLICATION_JSON);
					header.setBearerAuth(token);
					final String resp = "{\"token\": \"" + token + "\"}";
					return ResponseEntity.status(HttpStatus.OK).headers(header).body(resp);
				}
				String resp = "{\"message\": \"Wrong password\", \"status\": 401}";
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).header("Content-Type", "application/json").body(resp);
			}
			String resp = "{\"message\": \"Wrong Username\", \"status\": 401}";
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).header("Content-Type", "application/json").body(resp);
		} catch (Exception except) {
			String resp = "{\"message\": \"error: " + except.getMessage() + "\", \"status\": 400}";
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(resp);
		}
	}
	
	@GetMapping(value="/me")
	public ResponseEntity<?> me(HttpServletRequest request) {
		try {
			final String username = SecurityContextHolder.getContext().getAuthentication().getName();
			if (username == null) {
				final String resp = "{\"message\": \"error: Unable to get Username from Token\", \"status\": 401}";
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).header("Content-Type", "application/json").body(resp);
			}
			final JwtUserDetails ud = (JwtUserDetails) this.jwtUserDetailsService.loadUserByUsername(username);
			final UserDetails userDetails = new UserDetails(ud.getUsername(), ud.getRole());
			if (userDetails.getUsername() == null) {
				final String resp = "{\"message\": \"error: You're not authorized to access this route\", \"status\": 401}";
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).header("Content-Type", "application/json").body(resp);	
			}
			return  ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(userDetails);
		} catch (Exception ex) {
			final String resp = "{\"message\": \"error: " + ex.getMessage() + "\", \"status\": 400}";
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(resp);
		}
	}
}
