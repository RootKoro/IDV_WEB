package com.quest.etna.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.quest.etna.model.Recipe;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Integer>{
	@Query(value="SELECT recipe.* FROM recipe join ingredient_recipes on ingredient_recipes.recipes_id = recipe.id where ingredients_id like :ingredientId", nativeQuery=true)
	public Iterable<Recipe> findByIngredientId(Integer ingredientId);
}
