package com.quest.etna.model;

public class IngredientDetails {
	private Integer id;
	private String label;
	private String type;
	private String description;
	
	public IngredientDetails() {}
	
	public IngredientDetails(Integer id, String label, String type, String description) {
		this.setId(id);
		this.setLabel(label);
		this.setType(type);
		this.setDescription(description);
	}
	
	public IngredientDetails(Ingredient ingredient) {
		this.setId(ingredient.getId());
		this.setLabel(ingredient.getLabel());
		this.setType(ingredient.getType());
		this.setDescription(ingredient.getDescription());
	}
	
	public Integer getId() { return id; }
	public String getLabel() { return label; }
	public String getType() { return type; }
	public String getDescription() { return description; }
	
	public void setId(Integer id) { this.id = id; }
	public void setLabel(String label) { this.label = label; }
	public void setType(String type) { this.type = type; }
	public void setDescription(String description) { this.description = description; }
}
