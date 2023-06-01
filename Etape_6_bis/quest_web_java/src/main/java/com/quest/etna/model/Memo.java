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
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Memo {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(nullable=false, unique=true, length=255) private String label;
	@Column() private String content;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	@OnDelete(action=OnDeleteAction.CASCADE)
	@JoinColumn(nullable=false)
	private User owner;
	
	@ManyToMany(
//		mappedBy="memories",
		fetch = FetchType.LAZY,
		cascade = {
	          CascadeType.PERSIST,
	          CascadeType.MERGE
	})
	@JoinTable(
        joinColumns = { @JoinColumn(nullable=false) },
        inverseJoinColumns = { @JoinColumn(nullable=false)
    })
	private Set<User> sharedWith = new HashSet<>();
	
	@Column() private Date creationDate;
	@Column() private Date updatedDate;
	
	public Memo () {}
	
	public Memo (Integer id, String label, String content, User owner) {
		this.setId(id);
		this.setLabel(label);
		this.setContent(content);
		this.setOwner(owner);
	}
	
	public Memo (String label, String content, User owner) {
		this.setLabel(label);
		this.setContent(content);
		this.setOwner(owner);
	}
	
	public Memo(Memo memo) {
		this.setId(memo.getId());
		this.setLabel(memo.getLabel());
		this.setContent(memo.getContent());
		this.setOwner(memo.getOwner());
		this.setSharedWith(memo.getSahredWith());
	}
	
	public Integer getId() { return id; }
	public User getOwner() { return owner; }
	public String getLabel() { return label; }
	public String getContent() { return content; }
	public Set<User> getSahredWith() { return sharedWith; }
	public Date getCreationDate() { return creationDate; }
	public Date getUpdatedDate() { return updatedDate; }
	
	public void setId(Integer id) { this.id = id > 0? id: this.id; }
	public void setOwner(User owner) { this.owner = owner; }
	public void setLabel(String label) { this.label = label != ""? label: this.label; }
	public void setContent(String content) { this.content= content != ""? content: this.content; }
	public void setSharedWith(Set<User> sharedWith) { this.sharedWith = sharedWith; }
	public void addSharedWith(User user) {this.sharedWith.add(user); }
	public void setCreationDate() { this.creationDate = new Date(); }
	public void setUpdatedDate() { this.updatedDate = new Date(); }
	
	@Override
	public String toString() {
		return "Memo [id=" + id + ", label=" + label + ", content=" + content + ", owner=" + owner + ", sharedWith="
				+ sharedWith + ", creationDate=" + creationDate + ", updatedDate=" + updatedDate + "]";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(content, label, owner);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Memo other = (Memo) obj;
		return Objects.equals(content, other.content) && Objects.equals(label, other.label)
				&& Objects.equals(owner, other.owner);
	}
}
