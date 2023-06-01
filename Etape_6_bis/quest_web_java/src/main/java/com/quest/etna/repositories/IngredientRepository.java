package com.quest.etna.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.quest.etna.model.Ingredient;

@Repository
public interface IngredientRepository extends CrudRepository<Ingredient, Integer>{
	@Query(value="SELECT ingredient.* FROM ingredient join ingredient_recipes on ingredient_recipes.ingredients_id = ingredient.id where recipes_id like :recipeId", nativeQuery=true)
	public Iterable<Ingredient> findByRecipeId(Integer recipeId);
}
