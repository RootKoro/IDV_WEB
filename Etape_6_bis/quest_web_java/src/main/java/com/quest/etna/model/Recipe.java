package com.quest.etna.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Recipe {	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(nullable=false, unique=true, length=255) private String label;
	@Column(nullable=false) private String difficulty;
	@Column() private String description;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	@OnDelete(action=OnDeleteAction.CASCADE)
	@JoinColumn(nullable=false)
	private User owner;
	
	@ManyToMany(
		mappedBy="recipes",
		fetch = FetchType.LAZY,
		cascade = {
	          CascadeType.PERSIST,
	          CascadeType.MERGE
	})
	private Set<Ingredient> ingredients = new HashSet<>();
	
	@Column() private Date creationDate;
	@Column() private Date updatedDate;
	
	public Recipe() {
		this.setCreationDate();
		this.setUpdatedDate();
	}
	
	public Recipe(Integer id, String label, String difficulty, String description, User owner, Set<Ingredient> ingredients) {
		this.setId(id);
		this.setLabel(label);
		this.setDifficulty(difficulty);
		this.setDescription(description);
		this.setOwner(owner);
		this.setIngredients(ingredients);
		this.setCreationDate();
		this.setUpdatedDate();
	}
	
	public Recipe(String label, String difficulty, String description, User owner, Set<Ingredient> ingredients) {
		this.setLabel(label);
		this.setDifficulty(difficulty);
		this.setDescription(description);
		this.setOwner(owner);
		this.setIngredients(ingredients);
		this.setCreationDate();
		this.setUpdatedDate();
	}
	
	public Recipe(String label, String difficulty, String description) {
		this.setLabel(label);
		this.setDifficulty(difficulty);
		this.setDescription(description);
		this.setCreationDate();
		this.setUpdatedDate();
	}
	
	public Recipe(Recipe recipe) {
		this.setLabel(recipe.getLabel());
		this.setDifficulty(recipe.getDifficulty());
		this.setDescription(recipe.getDescription());
		this.setOwner(recipe.getOwner());
		this.setIngredients(recipe.getIngredients());
		this.setCreationDate();
		this.setUpdatedDate();
	}
	
	public Integer getId() { return id; }
	public String getLabel() { return label; }
	public String getDifficulty() { return difficulty; }
	public String getDescription() { return description; }
	public Set<Ingredient> getIngredients() { return ingredients; }
	public User getOwner() { return owner; }
	public Date getCreationDate() { return creationDate; }
	public Date getUpdatedDate() { return updatedDate; }
	
	public void setId(Integer id) { this.id = id > 0? id: this.id; }
	public void setLabel(String label) { this.label = label != ""? label: this.label; }
	public void setDifficulty(String difficulty) { this.difficulty = difficulty != ""? difficulty: this.difficulty; }
	public void setDescription(String description) { this.description = description != ""? description: this.description; }
	public void setOwner(User owner) { this.owner = owner; }
	public void setIngredients(Set<Ingredient> ingredients) { this.ingredients = ingredients; }
	public void addIngredients(Ingredient ingredient) {
		this.ingredients.add(ingredient);
		ingredient.getRecipe().add(this);
	}
	public void removeIngredient(Ingredient ingredient) {
		this.ingredients.remove(ingredient);
		ingredient.getRecipe().remove(this);
	}
	public void setCreationDate() { this.creationDate = new Date(); }
	public void setUpdatedDate() { this.updatedDate = new Date(); }

	@Override
	public String toString() {
		return "Recipe [id=" + id + ", label=" + label + ", difficulty=" + difficulty + ", description=" + description
				+ ", owner=" + owner + ", ingredients=" + ingredients + ", creationDate=" + creationDate
				+ ", updatedDate=" + updatedDate + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, difficulty, ingredients, label);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Recipe other = (Recipe) obj;
		return Objects.equals(description, other.description) && Objects.equals(difficulty, other.difficulty)
				&& Objects.equals(ingredients, other.ingredients) && Objects.equals(label, other.label);
	}
}
