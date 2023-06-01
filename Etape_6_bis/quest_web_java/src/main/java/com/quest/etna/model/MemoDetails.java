package com.quest.etna.model;

public class MemoDetails {
	private Integer id;
	private String label;
	private String content;
	private String owner;
	
	public MemoDetails() {}
	
	public MemoDetails(Memo memo) {
		this.setId(memo.getId());
		this.setLabel(memo.getLabel());
		this.setContent(memo.getContent());
		this.setOwner(memo.getOwner().getUsername());
	}
	
	public MemoDetails(Integer id, String label, String content, String Owner) {
		this.setId(id);
		this.setLabel(label);
		this.setContent(content);
		this.setOwner(Owner);
	}
	
	public Integer getId() { return id; }
	public String getLabel() { return label; }
	public String getContent() { return content; }
	public String getOwner() { return owner; }
	
	public void setId(Integer id) { this.id = id; }
	public void setLabel(String label) { this.label = label; }
	public void setContent(String content) { this.content = content; }
	public void setOwner(String owner) { this.owner = owner; }
}
