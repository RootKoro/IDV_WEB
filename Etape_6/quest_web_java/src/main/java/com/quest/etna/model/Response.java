package com.quest.etna.model;

public class Response {
	private Boolean success = false;
	private String message;
	private Integer status;
	
	
	public Response() {}
	
	public Response(String message, Integer status) {
		this.setMessage(message);
		this.setStatus(status);
	}
	
	public Response(Boolean success, String message, Integer status) {
		this.setSuccess(success);
		this.setMessage(message);
		this.setStatus(status);
	}
	
	public void setStatus(Integer status) { this.status = status; }
	public void setMessage(String message) { this.message = message; }
	public void setSuccess(Boolean success) { this.success = success; }
	
	public Integer getStatus() { return this.status; }
	public String getMessage() { return this.message; }
	public Boolean getSuccess() { return this.success; }
	
}
