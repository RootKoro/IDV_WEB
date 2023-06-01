package com.quest.etna.model;

public class UserMoreDetailed extends UserDetails {

	private Integer id;
	
	public Integer getId() { return id;}
	public void setId(Integer id) {
		if (id >= 0)
			this.id = id;
	}
	
	public UserMoreDetailed () {
		super();
	}
	
	public UserMoreDetailed (Integer id, String username) {
		super(username);
		this.setId(id);
	}
	
	public UserMoreDetailed (Integer id, String username, UserRole role) {
		super (username, role);
		this.setId(id);
	}
}
