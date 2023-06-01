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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.quest.etna.config.JwtTokenUtil;
import com.quest.etna.config.JwtUserDetailsService;
import com.quest.etna.config.WebSecurityConfig;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.Response;
import com.quest.etna.model.User;
import com.quest.etna.model.UserDetails;
import com.quest.etna.model.UserRole;
import com.quest.etna.repositories.UserRepository;

@CrossOrigin(exposedHeaders="Authorization", origins="*")
@RestController
public class AuthenticationController {
				private static UserRepository userRepository;
	@Autowired	private JwtUserDetailsService jwtUserDetailsService;
	@Autowired	private AuthenticationManager authenticationManager;
	@Autowired	private JwtUserDetailsService userDetailsService;
	@Autowired	private WebSecurityConfig webSecurityConfig;
	@Autowired	private JwtTokenUtil jwtTokenUtil;
	@Autowired	private UserRepository ur;
	
	@PostConstruct
	private void init() {
		userRepository = this.ur;
	}

	@PostMapping(value="/register")
	public ResponseEntity<?> register(@RequestBody User req) {
		try {
			if (req.getUsername() == null || req.getPassword() == null)
				throw new Exception();
			final String password = webSecurityConfig.passwordEncoder().encode(req.getPassword());
			User user = new User(req.getUsername().toLowerCase(), password);
			user.setRole(req.getRole());
			Optional<User> existing = AuthenticationController.userRepository.findByUsername(req.getUsername().toLowerCase());
			if (existing.isEmpty()) {
				User savedUser = AuthenticationController.userRepository.save(user);
				final UserDetails resp = new UserDetails(savedUser.getUsername(), UserRole.getByRole(savedUser.getRole()));
				return ResponseEntity.status(HttpStatus.CREATED).header("Content-Type", "application/json").body(resp);
			} else {
				final Response resp = new Response("This user already exists", 409);
				return ResponseEntity.status(HttpStatus.CONFLICT).header("Content-Type", "application/json").body(resp);
			}
		} catch (Exception e) {
			final Response resp = new Response(e.getMessage(), 400);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(resp);
		}
	}	

	@PostMapping(value="/authenticate")
	public ResponseEntity<?> authenticate(@RequestBody User req) {
		try {
			if (req.getUsername() == null || req.getPassword() == null)
				throw new Exception();
			final JwtUserDetails userDetails = (JwtUserDetails) userDetailsService.loadUserByUsername(req.getUsername().toLowerCase());
			if (userDetails.getUsername() != null) {
				if (webSecurityConfig.passwordEncoder().matches(req.getPassword(), userDetails.getPassword())) {
					authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDetails.getUsername(), req.getPassword(), userDetails.getAuthorities()));
					final String token = jwtTokenUtil.generateToken(userDetails);
					HttpHeaders header = new HttpHeaders();
					header.setContentType(MediaType.APPLICATION_JSON);
					header.setBearerAuth(token);
					final String resp = "{\"token\": \"" + token + "\", \"success\": \"" + true + "\", \"status\": \"" + 200 + "\"}";
					return ResponseEntity.status(HttpStatus.OK).headers(header).body(resp);
				}
				final Response resp = new Response("Wrong password", 401);
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).header("Content-Type", "application/json").body(resp);
			}
			final Response resp = new Response("Wrong username", 401);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).header("Content-Type", "application/json").body(resp);
		} catch (Exception except) {
			final Response resp = new Response(except.getMessage(), 400);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(resp);
		}
	}
	
	@GetMapping(value="/me")
	public ResponseEntity<?> me(HttpServletRequest request) {
		try {
			final String username = SecurityContextHolder.getContext().getAuthentication().getName();
			if (username == null) {
				final Response resp = new Response("error: Unable to get Username from Token", 401);
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).header("Content-Type", "application/json").body(resp);
			}
			final JwtUserDetails ud = (JwtUserDetails) this.jwtUserDetailsService.loadUserByUsername(username);
			final UserDetails userDetails = new UserDetails(ud.getUsername(), ud.getRole());
			if (userDetails.getUsername() == null) {
				final Response resp = new Response("error: UNAUTHORIZED", 401);
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).header("Content-Type", "application/json").body(resp);	
			}
			return  ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(userDetails);
		} catch (Exception ex) {
			final Response resp = new Response("error: " + ex.getMessage(), 400);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(resp);
		}
	}
}