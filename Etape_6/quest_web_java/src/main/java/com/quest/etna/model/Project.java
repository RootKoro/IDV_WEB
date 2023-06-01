package com.quest.etna.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

public class Project {	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(nullable=false, unique=true, length=255) private String label;
	@Column(nullable=false) private Integer level;
	@Column() private String description;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	@OnDelete(action=OnDeleteAction.CASCADE)
	@JoinColumn(nullable=false)
	private User owner;
	
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
	private Set<User> coworkers = new HashSet<>();
	
	@Column() private Date creationDate;
	@Column() private Date updatedDate;
	
	public Project() {
		this.setCreationDate();
		this.setUpdatedDate();
	}
	
	public Project(Integer id, String label, Integer level, String description, User owner, Set<User> coworkers) {
		this.setId(id);
		this.setLabel(label);
		this.setLevel(level);
		this.setDescription(description);
		this.setOwner(owner);
		this.setCoworkers(coworkers);
		this.setCreationDate();
		this.setUpdatedDate();
	}
	
	public Project(String label, Integer level, String description, User owner, Set<User> coworkers) {
		this.setLabel(label);
		this.setLevel(level);
		this.setDescription(description);
		this.setOwner(owner);
		this.setCoworkers(coworkers);
		this.setCreationDate();
		this.setUpdatedDate();
	}
	
	public Project(String label, Integer level, String description) {
		this.setLabel(label);
		this.setLevel(level);
		this.setDescription(description);
		this.setCreationDate();
		this.setUpdatedDate();
	}
	
	public Project(Project project) {
		this.setLabel(project.getLabel());
		this.setLevel(project.getLevel());
		this.setDescription(project.getDescription());
		this.setOwner(project.getOwner());
		this.setCoworkers(project.getCoworkers());
		this.setCreationDate();
		this.setUpdatedDate();
	}
	
	public Integer getId() { return id; }
	public String getLabel() { return label; }
	public Integer getLevel() { return level; }
	public String getDescription() { return description; }
	public Set<User> getCoworkers() { return coworkers; }
	public User getOwner() { return owner; }
	public Date getCreationDate() { return creationDate; }
	public Date getUpdatedDate() { return updatedDate; }
	
	public void setId(Integer id) { this.id = id > 0? id: this.id; }
	public void setLabel(String label) { this.label = label != ""? label: this.label; }
	public void setLevel(Integer level) { this.level = level > 0? level: this.level; }
	public void setDescription(String description) { this.description = description != ""? description: this.description; }
	public void setOwner(User owner) { this.owner = owner; }
	public void setCoworkers(Set<User> coworkers) { this.coworkers = coworkers; }
	public void addCoworkers(User coworker) {this.coworkers.add(coworker); }
	public void setCreationDate() { this.creationDate = new Date(); }
	public void setUpdatedDate() { this.updatedDate = new Date(); }
	
	@Override
	public String toString() {
		return "Project [label=" + label + ", level=" + level + ", description=" + description + ", owner=" + owner.getUsername()
				+ ", coworkers=" + coworkers + ", creationDate=" + creationDate + ", updatedDate=" + updatedDate + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(description, label, level);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Project other = (Project) obj;
		return Objects.equals(description, other.description)
				&& Objects.equals(label, other.label)
				&& Objects.equals(level, other.level);
	}	
}
