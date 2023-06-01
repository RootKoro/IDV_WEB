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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quest.etna.model.Address;
import com.quest.etna.model.AddressDetails;
import com.quest.etna.model.Response;
import com.quest.etna.model.User;
import com.quest.etna.repositories.AddressRepository;
import com.quest.etna.repositories.UserRepository;

@RestController
@CrossOrigin(exposedHeaders="Authorization", origins="*")
@RequestMapping("/address")
public class AddressController {
				private static UserRepository userRepository;
				private static AddressRepository addressRepository;
	@Autowired	private UserRepository ur;
	@Autowired	private AddressRepository ar;

	@PostConstruct
	private void init() { 
		userRepository = this.ur;
		addressRepository = this.ar;
	}
	
	private static Optional<User> getUserFromToken () {
		final String username = SecurityContextHolder.getContext().getAuthentication().getName();
		if (username == null) 
			return null;
		
		final Optional<User> user = AddressController.userRepository.findByUsername(username);
		return user; 
	}

	@GetMapping()
	public ResponseEntity<?> index(){
		try {
			Iterable<Address> addresses;
			List<AddressDetails> addressDetails = new ArrayList<AddressDetails>();
			final Optional<User> user = AddressController.getUserFromToken();
			if (user.isEmpty()) {
				final Response resp = new Response("error: Something went wrong! Log in again", 403);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
			
			if(user.get().getRole().equals("ROLE_USER"))
				addresses = addressRepository.findByUserId(user.get().getId());
			else
				addresses = addressRepository.findAll();
			
			for (Address address : addresses)
				addressDetails.add(new AddressDetails(address));
			return  ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(addressDetails);
		} catch (Exception e) {
			final Response resp = new Response("error: " + e.getMessage(), 400);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(resp);
		}
	}

	@GetMapping(value="/{id}")
	public ResponseEntity<?> getAddress(@PathVariable("id") Integer id){
		try {
			final Optional<User> user = AddressController.getUserFromToken();
			if (user.isEmpty()) {
				final Response resp = new Response("error: Something went wrong, log in again!", 403);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
			
			final Optional<Address> address = addressRepository.findById(id);
			final AddressDetails addressDetails = new AddressDetails(address.get());
			if (address.get() == null) {
				final Response resp = new Response("error: address not found!", 404);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Content-Type", "application/json").body(resp);
			}
			
			if(user.get().getRole().equals("ROLE_USER")) {
				if (address.get().getUser().equals(user.get()))
					return  ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(addressDetails);
			} else
				return  ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(addressDetails);
			
			final Response resp = new Response("error: Permission denied", 403);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
		} catch (Exception e) {
			final Response resp = new Response("error: " + e.getMessage(), 400);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Content-Type", "application/json").body(resp);
		}
	}

	@PostMapping()
	public ResponseEntity<?> store(@RequestBody AddressDetails req) {
		try {
			final Optional<User> user = AddressController.getUserFromToken();
			if (user == null || user.isEmpty()) {
				final Response resp = new Response("error: Something went wrong, log in again", 403);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
			
			User useer = new User(user.get());
			Address address = new Address(req.getStreet(), req.getPostalCode(), req.getCity(), req.getCountry(), useer);
			AddressController.userRepository.save(address.getUser());
			AddressController.addressRepository.save(address);
			AddressDetails addressDetails = new AddressDetails(address);
			return  ResponseEntity.status(HttpStatus.CREATED).header("Content-Type", "application/json").body(addressDetails);
		} catch (Exception e) {
			final Response resp = new Response("error: " + e.getMessage(), 400);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(resp);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Address req) {
		try {
			final Optional<User> user = AddressController.getUserFromToken();
			if (user.isEmpty()) {
				final Response resp = new Response("error: Something went wrong, log in again!", 403);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
			
			Address address = addressRepository.findById(id).get();
			
			if (address == null) {
				final Response resp = new Response("error: Address not found!", 404);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Content-Type", "application/json").body(resp);
			}
			
			address.setCity(req.getCity());
			address.setCountry(req.getCountry());
			address.setPostalCode(req.getPostalCode());
			address.setStreet(req.getStreet());
			AddressDetails addressDetails = new AddressDetails(address);
			if(user.get().getRole().equals("ROLE_USER")) {
				if (address.getUser().equals(user.get())) {
					addressRepository.save(address);
					return  ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(addressDetails);
				}
			}
			else {
				addressRepository.save(address);
				return  ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(addressDetails);
			}
			
			final Response resp = new Response("error: Permisssion denied", 403);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
		} catch (Exception e) {
			final Response resp = new Response("error: " + e.getMessage(), 400);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(resp);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> drop(@PathVariable("id") Integer id) {
		try {
			final Optional<User> user = AddressController.getUserFromToken();
			if (user.isEmpty()) {
				final Response resp = new Response("error: Permission denied", 403);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
			
			Address address = addressRepository.findById(id).get();
			if(user.get().getRole().equals("ROLE_USER")) {
				if (address.getUser().equals(user.get())) {
					addressRepository.delete(address);
					final Response resp = new Response(true, "", 200);
					return  ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(resp);
				}
			} else {
				addressRepository.delete(address);
				final Response resp = new Response(true, "", 200);
				return  ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(resp);
			}
			
			final Response resp = new Response("Permission denied", 403);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
		} catch(Exception e) {
			final Response resp = new Response("error: " + e.getMessage(), 400);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Content-Type", "application/json").body(resp);
		}
	}
}
