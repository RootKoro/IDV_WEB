package com.quest.etna.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

import com.quest.etna.model.Ingredient;
import com.quest.etna.model.Recipe;
import com.quest.etna.model.RecipeDetails;
import com.quest.etna.model.Response;
import com.quest.etna.model.User;
import com.quest.etna.repositories.IngredientRepository;
import com.quest.etna.repositories.RecipeRepository;
import com.quest.etna.repositories.UserRepository;

@RestController
@CrossOrigin(exposedHeaders="Authorization", origins="*")
@RequestMapping("/recipe")
public class RecipeController {
				private static UserRepository userRepository;
				private static RecipeRepository recipeRepository;
				private static IngredientRepository ingredientRepository;
	@Autowired	private UserRepository ur;
	@Autowired	private RecipeRepository rr;
	@Autowired	private IngredientRepository ir;
	
	@PostConstruct
	private void init() {
		userRepository = this.ur;
		recipeRepository = this.rr;
		ingredientRepository = this.ir;
	}
	
	private static Optional<User> getUserFromToken() {
		final String username = SecurityContextHolder.getContext().getAuthentication().getName();
		if(username == null)
			return null;
		
		final Optional<User> user = RecipeController.userRepository.findByUsername(username);
		return user;
	}
	
	@GetMapping()
	public ResponseEntity<?> index () {
		try {
			Iterable<Recipe> recipes;
			List<RecipeDetails> recipeDetails = new ArrayList<RecipeDetails>();
			final Optional<User> user = RecipeController.getUserFromToken();
			
			if (user.isEmpty()) {
				final Response resp = new Response("Something went wrong! Log in again", 403);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
			
			recipes = recipeRepository.findAll();
			for(Recipe recipe : recipes)
				recipeDetails.add(new RecipeDetails(recipe));
			
			return  ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(recipeDetails);
		} catch (Exception e) {
			final Response resp = new Response(e.getMessage(), 400);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(resp);
		}
	}

	@GetMapping(value="/{id}")
	public ResponseEntity<?> getRecipe(@PathVariable("id") Integer id) {
		try {
			final Optional<User> user = RecipeController.getUserFromToken();
			if(user.isEmpty()) {
				final Response resp = new Response("Something went wrong, log in again!", 403);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
			
			final Optional<Recipe> recipe = recipeRepository.findById(id);
			final RecipeDetails recipeDetails = new RecipeDetails(recipe.get());
			if(recipe.isEmpty()) {
				final Response resp = new Response("Recipe not found!", 404);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Content-Type", "application/json").body(resp);
			}
			
			return  ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(recipeDetails);
		} catch (Exception e) {
			final Response resp = new Response(e.getMessage(), 400);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Content-Type", "application/json").body(resp);
		}
	}
	
	@GetMapping(value="/ingredient/{id}")
	public ResponseEntity<?> getByIngredientId(@PathVariable("id") Integer id) {
		try {
			final Optional<User> user = RecipeController.getUserFromToken();
			if(user.isEmpty()) {
				final Response resp = new Response("Something went wrong, log in again", 403);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
			
			final Optional<Ingredient> ingredient = RecipeController.ingredientRepository.findById(id);
			if(ingredient.isEmpty()) {
				final Response resp = new Response("Ingredient not found!", 404);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Content-Type", "application/json").body(resp);
			}
			
			final Iterable<Recipe> recipes = RecipeController.recipeRepository.findByIngredientId(id);
			List<RecipeDetails> recipeDetails = new ArrayList<RecipeDetails>();
			for(Recipe recipe : recipes)
				recipeDetails.add(new RecipeDetails(recipe));
			
			return ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(recipeDetails);
		} catch (Exception e) {
			final Response resp = new Response(e.getMessage(), 400);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(resp);
		}
	}
	
	@PostMapping()
	public ResponseEntity<?> store(@RequestBody RecipeDetails req) {
		try {
			final Optional<User> user = RecipeController.getUserFromToken();
			if(user.isEmpty()) {
				final Response resp = new Response("Something went wrong, log in again", 403);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
//			Set<Ingredient> ingredients = new Hash
			Recipe recipe = new Recipe(req.getLabel(), req.getDifficulty(), req.getDescription(), new User(user.get()), new HashSet<>());
			RecipeController.userRepository.save(recipe.getOwner());
			RecipeController.recipeRepository.save(recipe);
			RecipeDetails recipeDetails = new RecipeDetails(recipe);
			
			return ResponseEntity.status(HttpStatus.CREATED).header("Content-Type", "application/json").body(recipeDetails);
		} catch (Exception e) {
			final Response resp = new Response("error: " + e.getMessage(), 400);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(resp);
		}
	}
	
	@PutMapping("/id")
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody RecipeDetails req) {
		try {
			final Optional<User> user = RecipeController.getUserFromToken();
			if(user.isEmpty()) {
				final Response resp = new Response("Something went wrong, log in again!", 403);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
			
			Recipe recipe = RecipeController.recipeRepository.findById(id).get();
			if(recipe == null) {
				final Response resp = new Response("Recipe not found!", 404);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Content-Type", "application/json").body(resp);
			}
			
			recipe.setLabel(req.getLabel());
			recipe.setDifficulty(req.getDifficulty());
			recipe.setDescription(req.getDescription());
			recipe.setUpdatedDate();
			RecipeDetails recipeDetails = new RecipeDetails(recipe);
			if(user.get().getRole().equals("ROLE_ADMIN") || recipe.getOwner().equals(user.get())) {
				RecipeController.recipeRepository.save(recipe);
				return ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application.json").body(recipeDetails);
			}
			
			final Response resp = new Response("Permission denied!", 403);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
		} catch (Exception e) {
			final Response resp = new Response(e.getMessage(), 400);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(resp);
		}
	}
	
	@PutMapping("/{rid}/add-ingredient/{iid}")
	public ResponseEntity<?> addIngredient(@PathVariable("rid") Integer recipeID, @PathVariable("iid") Integer ingredientID) {
		try {
			final Optional<User> user = RecipeController.getUserFromToken();
			if(user.isEmpty()) {
				final Response resp = new Response("Something went wrong, log in again!", 403);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
			
			Recipe recipe = RecipeController.recipeRepository.findById(recipeID).get();
			if(recipe == null) {
				final Response resp = new Response("Recipe not found!", 404);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Content-Type", "application/json").body(resp);
			}
			
			Ingredient ingredient = RecipeController.ingredientRepository.findById(ingredientID).get();
			if(ingredient == null) {
				final Response resp = new Response("Ingredient not found!", 404);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Content-Type", "application/json").body(resp);
			}
			
			recipe.addIngredients(ingredient);
			final RecipeDetails recipeDetails = new RecipeDetails(recipe);
			if(user.get().getRole().equals("ROLE_ADMIN") || recipe.getOwner().equals(user.get())) {
				RecipeController.ingredientRepository.save(ingredient);
				RecipeController.recipeRepository.save(recipe);
				return ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(recipeDetails);
			}
			
			final Response resp = new Response("Permission denied !", 403);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
		} catch (Exception e) {
			final Response resp = new Response(e.getMessage(), 400);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(resp);
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> drop(@PathVariable("id") Integer id) {
		try {
			final Optional<User> user = RecipeController.getUserFromToken();
			if (user.isEmpty()) {
				final Response resp = new Response("Permission denied", 403);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
			}
			
			Recipe recipe = RecipeController.recipeRepository.findById(id).get();
			if(user.get().getRole().equals("ROLE_ADMIN") || recipe.getOwner().equals(user.get())) {
				RecipeController.recipeRepository.delete(recipe);
				final Response resp = new Response(true, "", 200);
				return ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(resp);
			}
			
			final Response resp = new Response("Permission denied", 403);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).header("Content-Type", "application/json").body(resp);
		} catch (Exception e) {
			final Response resp = new Response(e.getMessage(), 400);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(resp);
		}
	}
}
