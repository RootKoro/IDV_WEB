package com.quest.etna.model;

public class AddressDetails {
	private Integer id;
	private String street;
	private String postalCode;
	private String city;
	private String country;
	private String owner;
	
	public AddressDetails () { }
	
	public AddressDetails (Address address) {
		this.setId(address.getId());
		this.setStreet(address.getStreet());
		this.setPostalCode(address.getPostalCode());
		this.setCity(address.getCity());
		this.setCountry(address.getCountry());
		this.setOwner(address.getUser().getUsername());
	}
	
	public AddressDetails (Integer id, String street, String postalCode, String city, String country, String owner) {
		this.setId(id);
		this.setStreet(street);
		this.setPostalCode(postalCode);
		this.setCity(city);
		this.setCountry(country);
		this.setOwner(owner);
	}

	public Integer getId() { return id; }

	public void setId(Integer id) { this.id = id; }

	public String getStreet() { return street; }

	public void setStreet(String street) { this.street = street; }

	public String getPostalCode() { return postalCode; }

	public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

	public String getCity() { return city; }

	public void setCity(String city) { this.city = city; }

	public String getCountry() { return country; }

	public void setCountry(String country) { this.country = country; }

	public String getOwner() { return owner; }

	public void setOwner(String owner) { this.owner = owner; }
}
