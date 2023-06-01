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

import com.quest.etna.model.Memo;
import com.quest.etna.model.MemoDetails;
import com.quest.etna.model.Response;
import com.quest.etna.model.User;
import com.quest.etna.repositories.MemoRepository;
import com.quest.etna.repositories.UserRepository;

@RestController
@CrossOrigin(exposedHeaders="Authorization", origins="*")
@RequestMapping("/memo")
public class MemoController {
				private static MemoRepository memoRepository;
				private static UserRepository userRepository;
	@Autowired	private MemoRepository mr;
	@Autowired	private UserRepository ur;
	
	@PostConstruct
	private void init() {
		userRepository = this.ur;
		memoRepository = this.mr;
	}
	
	private static Optional<User> getUserFromToken () {
		final String username = SecurityContextHolder.getContext().getAuthentication().getName();
		if (username == null) 
			return null;
		
		final Optional<User> user = MemoController.userRepository.findByUsername(username);
		return user; 
	}
	
	@GetMapping()
	public ResponseEntity<?> index() {
		try {
			final Optional<User> user = MemoController.getUserFromToken();
			if (user.isEmpty()) {
				final Response resp = new Response("error: Something went wrong! Log in again", 403);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
			
			Iterable<Memo> memories;
			if(user.get().getRole().equals("ROLE_ADMIN"))
				memories = MemoController.memoRepository.findAll();
			else
				memories = MemoController.memoRepository.findByUserId(user.get().getId());
			
			List<MemoDetails> memoDetails = new ArrayList<MemoDetails>();
			for(Memo memo: memories)
				memoDetails.add(new MemoDetails(memo));
			
			return ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(memoDetails);
		} catch (Exception e) {
			final Response resp = new Response(e.getMessage(), 400);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(resp);
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getMemo(@PathVariable("id") Integer id) {
		try {
			final Optional<User> user = MemoController.getUserFromToken();
			if (user.isEmpty()) {
				final Response resp = new Response("error: Something went wrong! Log in again", 403);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
			
			final Memo memo = MemoController.memoRepository.findById(id).get();
			final MemoDetails memoDetails = new MemoDetails(memo);
			if(memo == null || !user.get().getRole().equals("ROLE_ADMIN") || !memo.getOwner().equals(user.get())) {
				final Response resp = new Response("Permission denied!", 403);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
			return ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(memoDetails);
		} catch (Exception e) {
			final Response resp = new Response(e.getMessage(), 400);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(resp);
		}
	}
	
	@PostMapping()
	public ResponseEntity<?> store(@RequestBody MemoDetails req) {
		try {
			final Optional<User> user = MemoController.getUserFromToken();
			if (user.isEmpty()) {
				final Response resp = new Response("error: Something went wrong! Log in again", 403);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
			
			Memo memo = new Memo(req.getLabel(), req.getContent(), new User(user.get()));
			MemoDetails memoDetails = new MemoDetails(memo);
			MemoController.userRepository.save(memo.getOwner());
			MemoController.memoRepository.save(memo);
			return ResponseEntity.status(HttpStatus.CREATED).header("Content-Type", "application/json").body(memoDetails);
		} catch (Exception e) {
			final Response resp = new Response(e.getMessage(), 400);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(resp);
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody MemoDetails req) {
		try {
			final Optional<User> user = MemoController.getUserFromToken();
			if (user.isEmpty()) {
				final Response resp = new Response("error: Something went wrong! Log in again", 403);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
			
			Memo memo = MemoController.memoRepository.findById(id).get();
			if(memo == null || !user.get().getRole().equals("ROLE_ADMIN") || !memo.getOwner().equals(user.get())) {
				final Response resp = new Response("Permission denied!", 403);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
			
			memo.setLabel(req.getLabel());
			memo.setContent(req.getContent());
			MemoController.memoRepository.save(memo);
			final MemoDetails memoDetails = new MemoDetails(memo);
			return ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(memoDetails);
		} catch (Exception e) {
			final Response resp = new Response(e.getMessage(), 400);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(resp);
		}
	}

	@PutMapping("/{memoID}/user/{userID}")
	public ResponseEntity<?> ShareWith(@PathVariable("memoID") Integer memoID, @PathVariable("userID") Integer userID) {
		try {
			final Optional<User> user = MemoController.getUserFromToken();
			if (user.isEmpty()) {
				final Response resp = new Response("error: Something went wrong! Log in again", 403);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
			
			Memo memo = MemoController.memoRepository.findById(memoID).get();
			User beneficier = MemoController.userRepository.findById(userID).get();
			if(memo == null || beneficier == null || !user.get().getRole().equals("ROLE_ADMIN") || !memo.getOwner().equals(user.get())) {
				final Response resp = new Response("Permission denied!", 403);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
			
			memo.addSharedWith(beneficier);
			MemoController.userRepository.save(memo.getOwner());
			MemoController.memoRepository.save(memo);
			return ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(new MemoDetails(memo));
		} catch (Exception e) {
			final Response resp = new Response(e.getMessage(), 400);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(resp);
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> drop(@PathVariable("id") Integer id) {
		try {
			final Optional<User> user = MemoController.getUserFromToken();
			if (user.isEmpty()) {
				final Response resp = new Response("error: Something went wrong! Log in again", 403);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
			
			Memo memo = MemoController.memoRepository.findById(id).get();
			if(memo == null || !user.get().getRole().equals("ROLE_ADMIN") || !memo.getOwner().equals(user.get())) {
				final Response resp = new Response("Permission denied!", 403);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
			
			MemoController.memoRepository.delete(memo);
			return ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(new Response(true, "", 200));
		} catch (Exception e) {
			final Response resp = new Response(e.getMessage(), 400);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(resp);
		}
	}
}
