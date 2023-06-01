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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class Ingredient {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(nullable=false, unique=true, length=255) private String label;
	@Column() private String type;
	@Column() private String description;
	
	@ManyToMany(
		fetch = FetchType.LAZY,
		cascade = {
	          CascadeType.PERSIST,
	          CascadeType.MERGE
	})
	@JoinTable(
        joinColumns = { @JoinColumn(nullable=false) },
        inverseJoinColumns = { @JoinColumn(nullable=false)
    })
	private Set<Recipe> recipes = new HashSet<>();
	
	@Column() private Date creationDate;
	@Column() private Date updatedDate;
	
	public Ingredient() {
		this.setCreationDate();
		this.setUpdatedDate();
	}
	
	public Ingredient(Integer id, String label, String description, String type, Recipe recipe) {
		this.setId(id);
		this.setLabel(label);
		this.setType(type);
		this.setDescription(description);
		this.setRecipe(recipes);
		this.setCreationDate();
		this.setUpdatedDate();
	}
	
	public Ingredient(String label, String description, String type, Recipe recipe) {
		this.setLabel(label);
		this.setType(type);
		this.setDescription(description);
		this.setRecipe(recipes);
		this.setCreationDate();
		this.setUpdatedDate();
	}
	
	public Ingredient(String label, String description, String type) {
		this.setLabel(label);
		this.setType(type);
		this.setDescription(description);
		this.setCreationDate();
		this.setUpdatedDate();
	}
	
	public Integer getId() { return id; }
	public String getLabel() { return label; }
	public String getDescription() { return description; }
	public String getType() { return type; }
	public Set<Recipe> getRecipe() { return recipes; }
	public Date getCreationDate() { return creationDate; }
	public Date getUpdatedDate() { return updatedDate; }
	
	public void setId(Integer id) { this.id = id > 0? id: this.id; }
	public void setLabel(String label) { this.label = label != ""? label: this.label; }
	public void setDescription(String description) { this.description = description != ""? description: this.description; }
	public void setType(String type) { this.type= type; }
	public void setRecipe(Set<Recipe> recipes) { this.recipes = recipes; }
	public void setCreationDate() { this.creationDate = new Date(); }
	public void setUpdatedDate() { this.updatedDate = new Date(); }

	@Override
	public String toString() {
		return "Ingredient [id=" + id + ", label=" + label + ", type=" + type + ", description=" + description
				+ ", recipes=" + recipes + ", creationDate=" + creationDate + ", updatedDate=" + updatedDate + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, label, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ingredient other = (Ingredient) obj;
		return Objects.equals(description, other.description) && Objects.equals(label, other.label)
				&& Objects.equals(type, other.type);
	}
}
