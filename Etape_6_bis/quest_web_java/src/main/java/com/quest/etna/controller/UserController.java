package com.quest.etna.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quest.etna.model.Address;
import com.quest.etna.model.Response;
import com.quest.etna.model.User;
import com.quest.etna.model.UserMoreDetailed;
import com.quest.etna.model.UserRole;
import com.quest.etna.repositories.AddressRepository;
import com.quest.etna.repositories.UserRepository;

@CrossOrigin(exposedHeaders="Authorization", origins="*")
@RestController
@RequestMapping("/user")
public class UserController {
				private static UserRepository userRepository;
				private static AddressRepository addressRepository;
	@Autowired	private UserRepository ur;
	@Autowired  private AddressRepository ar;
	
	@PostConstruct
	private void init() {
		userRepository = this.ur;
		addressRepository = this.ar;
	}
	
	private static Optional<User> getUserFromToken () {
		final String username = SecurityContextHolder.getContext().getAuthentication().getName();
		if (username == null) 
			return null;
		
		final Optional<User> user = UserController.userRepository.findByUsername(username);
		return user; 
	}

	@GetMapping()
	public ResponseEntity<?> index(){
		try {
			final Optional<User> user = UserController.getUserFromToken();
			if (user == null || user.isEmpty()) {
				final Response resp = new Response("error: You cannot access these data", 403);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
			
			Iterable<User> users = UserController.userRepository.findAll();
			List<UserMoreDetailed> userDetails = new ArrayList<UserMoreDetailed>();
			
			for (User u: users)
				userDetails.add(new UserMoreDetailed(u.getId(), u.getUsername(), UserRole.getByRole(u.getRole())));

			return  ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(userDetails);
		} catch (Exception e) {
			final Response resp = new Response("error: " + e.getMessage(), 400);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(resp);
		}
	}

	@GetMapping(value="/{id}")
	public ResponseEntity<?> getUser(@PathVariable("id") Integer id){
		try {
			final Optional<User> user = UserController.getUserFromToken();
			if (user == null || user.isEmpty()) {
				final Response resp = new Response("error: You can't access these data", 403);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
			
			Optional<User> existing = UserController.userRepository.findById(id);
			if (existing == null || existing.isEmpty()) {
				final Response resp = new Response("error: User not found", 404);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Content-Type", "application/json").body(resp);
			}
			
			UserMoreDetailed userDetails = new UserMoreDetailed(existing.get().getId(), existing.get().getUsername(), UserRole.getByRole(existing.get().getRole()));
			return  ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(userDetails);
		} catch (Exception e) {
			final Response resp = new Response("error: " + e.getMessage(), 400);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(resp);
		}
	}

	@PutMapping(value="/{id}")
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody User req){
		try {
			final Optional<User> user = UserController.getUserFromToken();
			if (user == null || user.isEmpty()) {
				final Response resp = new Response("error: You can't access these data", 403);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
			
			if (user.get().getRole().equals("ROLE_USER") && !user.get().getId().equals(id)) {
				final Response resp = new Response("error: You're not authorized", 403);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
			
			Optional<User> existing = UserController.userRepository.findById(id);
			if (existing == null || existing.isEmpty()) {
				final Response resp = new Response("error: User not found!", 404);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Content-Type", "application/json").body(resp);
			}
			
			User userUpdate = new User(existing.get());
			userUpdate.setUsername(req.getUsername());
			userUpdate.setRole(req.getRole());
			UserController.userRepository.save(userUpdate);
			UserMoreDetailed userDetails = new UserMoreDetailed(userUpdate.getId(), userUpdate.getUsername(), UserRole.getByRole(userUpdate.getRole()));
			return ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(userDetails);
		} catch (Exception e) {
			final Response resp = new Response("error: " + e.getMessage(), 400);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(resp);
		}
	}

	@PutMapping()
	public ResponseEntity<?> self(@RequestBody User req){
		try {
			final Optional<User> user = UserController.getUserFromToken();
			if (user == null || user.isEmpty()) {
				final String resp = "{\"message\": \"error: You're not logged in\", \"status\": 403}";
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
			
			User userUpdate = new User(user.get());
			userUpdate.setUsername(req.getUsername());
			UserController.userRepository.save(userUpdate);
			UserMoreDetailed userDetails = new UserMoreDetailed(userUpdate.getId(), userUpdate.getUsername(), UserRole.getByRole(userUpdate.getRole()));
			return ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(userDetails);
		} catch (Exception e) {
			final String resp = "{\"message\": \"error: " + e.getMessage() + "\", \"status\": 400}";
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(resp);
		}
	}

	@DeleteMapping(value="/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		try {
			final Optional<User> user = UserController.getUserFromToken();
			if (user == null || user.isEmpty()) {
				final Response resp = new Response("error: You're not logged in", 403);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
			
			if (user.get().getRole().equals("ROLE_USER")) {
				final Response resp = new Response("error: You're not authorized", 403);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
			
			Optional<User> existing = UserController.userRepository.findById(id);
			if (existing == null || existing.isEmpty()) {
				final Response resp = new Response("error: User not found", 404);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Content-Type", "application/json").body(resp);
			}
			
			User userDel = new User(existing.get());
			Iterable<Address> addresses = new ArrayList<Address>();
			addresses = UserController.addressRepository.findByUserId(userDel.getId());
			for (Address address : addresses)
				UserController.addressRepository.delete(address);
			UserController.userRepository.delete(userDel);
			final Response resp = new Response(true, "deleted successfully", 200);
			return ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(resp);
		} catch (Exception e) {
			final String resp = "{\"message\": \"error: " + e.getMessage() + "\", \"status\": 400}";
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(resp);
		}
	}

	@DeleteMapping()
	public ResponseEntity<?> autoDestruct(){
		try {
			final Optional<User> user = UserController.getUserFromToken();
			if (user == null || user.isEmpty()) {
				final Response resp = new Response(false, "error: You're not logged in", 403);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
			
			User userDel = new User(user.get());
			Iterable<Address> addresses = new ArrayList<Address>();
			addresses = UserController.addressRepository.findByUserId(userDel.getId());
			for (Address address : addresses)
				UserController.addressRepository.delete(address);
			UserController.userRepository.delete(userDel);
			final Response resp = new Response("", 200);
			return ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(resp);
		} catch (Exception e) {
			final Response resp = new Response(false, "error: " + e.getMessage(), 400);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(resp);
		}
	}
}