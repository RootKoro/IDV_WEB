package com.quest.etna.model;

import java.util.HashSet;
import java.util.Set;

public class RecipeDetails {
	private Integer id;
	private String label;
	private String difficulty;
	private String description;
	private String owner;
	private Set<IngredientDetails> ingredients = new HashSet<>();
	
	public RecipeDetails() {}
	public RecipeDetails(Recipe recipe) {
		this.setId(recipe.getId());
		this.setLabel(recipe.getLabel());
		this.setDifficulty(recipe.getDifficulty());
		this.setDescription(recipe.getDescription());
		this.setOwner(recipe.getOwner().getUsername());
		this.setIngredients(recipe.getIngredients());
	}
	public RecipeDetails(Integer id, String label, String difficulty, String description, String owner) {
		this.setId(id);
		this.setLabel(label);
		this.setDifficulty(difficulty);
		this.setDescription(description);
		this.setOwner(owner);
	}
	
	public Integer getId() { return id; }
	public String getLabel() { return label; }
	public String getDifficulty() { return difficulty; }
	public String getDescription() { return description; }
	public Set<IngredientDetails> getIngredients() { return ingredients; }
	public String getOwner() { return owner; }

	public void setId(Integer id) { this.id = id; }
	public void setLabel(String label) { this.label = label; }
	public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
	public void setDescription(String description) { this.description = description; }
	public void setIngredients(Set<Ingredient> ingredients) { 
		for(Ingredient ingredient: ingredients)
			this.ingredients.add(new IngredientDetails(ingredient));
	}
	public void setOwner(String owner) { this.owner = owner; }
}
