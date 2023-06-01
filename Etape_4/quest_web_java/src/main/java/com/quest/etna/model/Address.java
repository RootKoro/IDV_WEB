package com.quest.etna.model;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Address {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(nullable=false, length=100) private String street;
	@Column(nullable=false, length=30) private String postalCode;
	@Column(nullable=false, length=50) private String city;
	@Column(nullable=false, length=50) private String country;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(nullable=false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;
	
	@Column() private Date creationDate;
	@Column() private Date updatedDate;

	public Address() {
		this.setCreationDate();
		this.setUpdatedDate();
	}
	
	public Address(Integer id, String street, String postalCode, String city, String country, User user,
			Date creationDate, Date updatedDate) {
		this.id = id;
		this.street = street;
		this.postalCode = postalCode;
		this.city = city;
		this.country = country;
		this.user = user;
		this.creationDate = creationDate;
		this.updatedDate = updatedDate;
	}

	public Address(String street, String postalCode, String city, String country) {
		this.setCity(city);
		this.setCountry(country);
		this.setPostalCode(postalCode);
		this.setStreet(street);
		this.setCreationDate();
		this.setUpdatedDate();
	}

	public Address(String street, String postalCode, String city, String country, User user) {
		this.setCity(city);
		this.setCountry(country);
		this.setPostalCode(postalCode);
		this.setStreet(street);
		this.setUser(user);
		this.setCreationDate();
		this.setUpdatedDate();
	}
	
	public Address(Address address) {
		this.setCity(address.getCity());
		this.setCountry(address.getCountry());
		this.setPostalCode(address.getPostalCode());
		this.setStreet(address.getStreet());
		this.setUser(address.getUser());
		this.setCreationDate();
		this.setUpdatedDate();
	}

	public Integer getId() { return id; }

	public void setId(Integer id) {
		if (id > 0)
			this.id = id;
	}

	public String getStreet() { return street; }

	public void setStreet(String street) {
		if (street != "" && street != null)
			this.street = street;
	}

	public String getPostalCode() { return postalCode; }

	public void setPostalCode(String postalCode) {
		if (postalCode != "" && postalCode != null)
			this.postalCode = postalCode;
	}

	public String getCity() { return city; }

	public void setCity(String city) {
		if (city != "" && city != null)
			this.city = city;
	}

	public String getCountry() { return country; }

	public void setCountry(String country) {
		if (country != "" && country != null)
			this.country = country;
	}

	public User getUser() { return user; }

	public void setUser(User user) {
		this.user = new User(user);
	}

	public Date getCreationDate() { return creationDate; }

	public void setCreationDate() { this.creationDate = new Date(); }

	public Date getUpdatedDate() { return updatedDate; }

	public void setUpdatedDate() { this.updatedDate = new Date(); }

	@Override
	public String toString() {
		return "Address [street=" + street + ", postalCode=" + postalCode + ", city=" + city + ", country=" + country
				+ ", user=" + user + ", creationDate=" + creationDate + ", updatedDate=" + updatedDate + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(city, country, creationDate, postalCode, street, updatedDate, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		return Objects.equals(city, other.city) && Objects.equals(country, other.country)
				&& Objects.equals(creationDate, other.creationDate) && Objects.equals(postalCode, other.postalCode)
				&& Objects.equals(street, other.street) && Objects.equals(updatedDate, other.updatedDate)
				&& Objects.equals(user, other.user);
	}
}