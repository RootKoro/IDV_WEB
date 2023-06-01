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

import com.quest.etna.model.User;
import com.quest.etna.model.Ingredient;
import com.quest.etna.model.Recipe;
import com.quest.etna.model.IngredientDetails;
import com.quest.etna.model.Response;
import com.quest.etna.repositories.IngredientRepository;
import com.quest.etna.repositories.RecipeRepository;
import com.quest.etna.repositories.UserRepository;

@RestController
@CrossOrigin(exposedHeaders="Authorization", origins="*")
@RequestMapping("/ingredient")
public class IngredientController {
				private static UserRepository userRepository;
				private static RecipeRepository recipeRepository;
				private static IngredientRepository ingredientRepository;
	@Autowired	private UserRepository ur;
	@Autowired  private RecipeRepository rr;
	@Autowired	private IngredientRepository ir;
	
	@PostConstruct
	private void init() { 
		userRepository = this.ur;
		recipeRepository = this.rr;
		ingredientRepository = this.ir;
	}
	
	private static Optional<User> getUserFromToken () {
		final String username = SecurityContextHolder.getContext().getAuthentication().getName();
		if (username == null) 
			return null;
		
		final Optional<User> user = IngredientController.userRepository.findByUsername(username);
		return user; 
	}
	
	@GetMapping()
	public ResponseEntity<?> index() {
		try {
			Iterable<Ingredient> ingredients;
			List<IngredientDetails> ingredientDetails = new ArrayList<IngredientDetails>();
			final Optional<User> user = IngredientController.getUserFromToken();
			
			if(user.isEmpty()) {
				final Response resp = new Response("Something went wrong, relogin!", 403);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
			
			ingredients = ingredientRepository.findAll();
			
			for (Ingredient ingredient : ingredients)
				ingredientDetails.add(new IngredientDetails(ingredient));
			
			return ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(ingredientDetails);
		} catch (Exception e) {
			final Response resp = new Response(e.getMessage(), 400);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(resp);
		}
	}
	
	@GetMapping(value="/{id}")
	public ResponseEntity<?> getIngredient(@PathVariable("id") Integer id) {
		try {
			final Optional<User> user = IngredientController.getUserFromToken();
			if (user.isEmpty()) {
				final Response resp = new Response("Something went wrong, log in again!", 403);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
			
			final Optional<Ingredient> ingredient = ingredientRepository.findById(id);
			final IngredientDetails ingredientDetails = new IngredientDetails(ingredient.get());
			if (ingredient.get() == null) {
				final Response resp = new Response("Ingredient not found!", 404);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Content-Type", "application/json").body(resp);
			}
			
			return  ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(ingredientDetails);
		} catch (Exception e) {
			final Response resp= new Response(e.getMessage(), 400);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(resp);
		}
	}
	
	@GetMapping(value="/recipe/{id}")
	public ResponseEntity<?> getIngredientsByRecipe(@PathVariable("id") Integer id) {
		try {
			final Optional<User> user = IngredientController.getUserFromToken();
			if (user.isEmpty()) {
				final Response resp = new Response("Something went wrong, log in again!", 403);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
			
			final Recipe recipe = recipeRepository.findById(id).get();
			if(recipe == null) {
				final Response resp = new Response("Recipe not found!", 404);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Content-Type", "application/json").body(resp);
			}
			
			final Iterable<Ingredient> ingredients = ingredientRepository.findByRecipeId(id);
			List<IngredientDetails> ingredientDetails = new ArrayList<IngredientDetails>();
			for(Ingredient ingredient: ingredients)
				ingredientDetails.add(new IngredientDetails(ingredient));
			
			return  ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(ingredientDetails);
		} catch (Exception e) {
			final Response resp= new Response(e.getMessage(), 400);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(resp);
		}
	}
	
	@PostMapping()
	public ResponseEntity<?> store(@RequestBody IngredientDetails req) {
		try {
			final Optional<User> user = IngredientController.getUserFromToken();
			if (user == null || user.isEmpty()) {
				final Response resp = new Response("Something went wrong, log in again!", 403);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
			
			Ingredient ingredient = new Ingredient(req.getLabel(), req.getType(), req.getDescription());
			IngredientController.ingredientRepository.save(ingredient);
			IngredientDetails ingredientDetails = new IngredientDetails(ingredient);
			return  ResponseEntity.status(HttpStatus.CREATED).header("Content-Type", "application/json").body(ingredientDetails);
		} catch (Exception e) {
			final Response resp = new Response(e.getMessage(), 400);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(resp);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody IngredientDetails req) {
		try {
			final Optional<User> user = IngredientController.getUserFromToken();
			if (user.isEmpty()) {
				final Response resp = new Response("error: Something went wrong, log in again!", 403);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
			
			Ingredient ingredient = ingredientRepository.findById(id).get();
			
			if (ingredient == null) {
				final Response resp = new Response("Ingredient not found!", 404);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Content-Type", "application/json").body(resp);
			}
			
			ingredient.setLabel(req.getLabel());
			ingredient.setType(req.getType());
			ingredient.setDescription(req.getDescription());
			ingredient.setUpdatedDate();
			
			if(user.get().getRole().equals("ROLE_ADMIN")) {
				ingredientRepository.save(ingredient);
				return  ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(new IngredientDetails(ingredient));
			}
			
			final Response resp = new Response("Permisssion denied !", 403);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
		} catch (Exception e) {
			final Response resp = new Response(e.getMessage(), 400);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(resp);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> drop(@PathVariable("id") Integer id) {
		try {
			final Optional<User> user = IngredientController.getUserFromToken();
			if (user.isEmpty()) {
				final Response resp = new Response("Permission denied!", 403);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
			
			Ingredient ingredient = ingredientRepository.findById(id).get();
			
			if(user.get().getRole().equals("ROLE_ADMIN")) {
				ingredientRepository.delete(ingredient);
				final Response resp = new Response(true, "", 200);
				return  ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(resp);
			}
			
			final Response resp = new Response("Permission denied !", 403);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
		} catch(Exception e) {
			final Response resp = new Response(e.getMessage(), 400);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Content-Type", "application/json").body(resp);
		}
	}
}
