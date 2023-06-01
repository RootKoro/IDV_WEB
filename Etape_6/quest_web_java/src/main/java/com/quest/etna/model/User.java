package com.quest.etna.model;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(nullable=false, unique=true, length=255) private String username;
	@Column(nullable=false, length=255) private String password;
	@Column() private String role="ROLE_USER";
	@Column() private Date creationDate;
	@Column() private Date updatedDate;
	
	
//	@OneToMany(mappedBy="user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	Set<Address> address = new HashSet<Address>();

	
	public Integer getId() { return id; }
	public String getUsername() { return username; }
	public String getPassword() { return password; }
	public String getRole() { return role; }
	public Date getCreationDate() { return creationDate; }
	public Date getUpdatedDate() { return updatedDate; }

	public void setId(Integer id) { this.id = id; }
	public void setUsername(String username) { this.username = username.trim().length() > 0 ? username : this.username; }
	public void setPassword(String password) { this.password = password.trim().length() > 0? password: this.password; }
	public void setRole(String role) { this.role = role; }
	public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }
	public void setUpdatedDate(Date updatedDate) { this.updatedDate = updatedDate; }

	
//	public Set<Address> getAddress() { return address; }
//	
//	public void setAddress(Set<Address> address) { this.address = address; }
	
	
	public User() {
		this.setCreationDate(new Date());
		this.setUpdatedDate(new Date());
	}
	
	public User(String username, String password) {
		this.setUsername(username);
		this.setPassword(password);
		this.setCreationDate(new Date());
		this.setUpdatedDate(new Date());
	}
	
	public User(String username, String password, String role) {
		this.setUsername(username);
		this.setPassword(password);
		this.setRole(role);
		this.setCreationDate(new Date());
		this.setUpdatedDate(new Date());
	}
	
	public User(User user) {
		this.setId(user.getId());
		this.setUsername(user.getUsername());
		this.setPassword(user.getPassword());
		this.setRole(user.getRole());
		this.setCreationDate(user.getCreationDate());
		this.setUpdatedDate(new Date());
	}
	
	public void updateUser(String username, String password) {
		this.setUsername(username);
		this.setPassword(password);
		this.setUpdatedDate(new Date());
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", role=" + role
				+ ", creationDate=" + creationDate + ", updatedDate=" + updatedDate + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(creationDate, id, password, role, updatedDate, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(creationDate, other.creationDate) && Objects.equals(id, other.id)
				&& Objects.equals(password, other.password) && Objects.equals(role, other.role)
				&& Objects.equals(updatedDate, other.updatedDate) && Objects.equals(username, other.username);
	}
}
