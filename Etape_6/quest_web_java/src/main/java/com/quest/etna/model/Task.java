package com.quest.etna.model;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

public class Task {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(nullable=false, unique=true, length=255) private String label;
	@Column(nullable=false) private Integer level;
	@Column() private String description;
	@Column() private String type;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	@OnDelete(action=OnDeleteAction.CASCADE)
	@JoinColumn(nullable=false)
	private Project project;
	
	@Column() private Date creationDate;
	@Column() private Date updatedDate;
	
	public Task() {
		this.setCreationDate();
		this.setUpdatedDate();
	}
	
	public Task(Integer id, String label, Integer level, String description, String type, Project project) {
		this.setId(id);
		this.setLabel(label);
		this.setLevel(level);
		this.setDescription(description);
		this.setType(type);
		this.setProject(project);
		this.setCreationDate();
		this.setUpdatedDate();
	}
	
	public Task(String label, Integer level, String description, String type, Project project) {
		this.setLabel(label);
		this.setLevel(level);
		this.setDescription(description);
		this.setType(type);
		this.setProject(project);
		this.setCreationDate();
		this.setUpdatedDate();
	}
	
	public Task(String label, Integer level, String description, String type) {
		this.setLabel(label);
		this.setLevel(level);
		this.setDescription(description);
		this.setType(type);
		this.setCreationDate();
		this.setUpdatedDate();
	}
	
	public Integer getId() { return id; }
	public String getLabel() { return label; }
	public Integer getLevel() { return level; }
	public String getDescription() { return description; }
	public String getType() { return type; }
	public Project getProject() { return project; }
	public Date getCreationDate() { return creationDate; }
	public Date getUpdatedDate() { return updatedDate; }
	
	public void setId(Integer id) { this.id = id > 0? id: this.id; }
	public void setLabel(String label) { this.label = label != ""? label: this.label; }
	public void setLevel(Integer level) { this.level = level > 0? level: this.level; }
	public void setDescription(String description) { this.description = description != ""? description: this.description; }
	public void setType(String type) { this.type= type; }
	public void setProject(Project project) { this.project= project; }
	public void setCreationDate() { this.creationDate = new Date(); }
	public void setUpdatedDate() { this.updatedDate = new Date(); }

	@Override
	public String toString() {
		return "Task [id=" + id + ", label=" + label + ", level=" + level + ", description=" + description + ", type="
				+ type + ", project=" + project + ", creationDate=" + creationDate + ", updatedDate=" + updatedDate
				+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, label, level, project, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		return Objects.equals(description, other.description) && Objects.equals(label, other.label)
				&& Objects.equals(level, other.level) && Objects.equals(project, other.project)
				&& Objects.equals(type, other.type);
	}
}
