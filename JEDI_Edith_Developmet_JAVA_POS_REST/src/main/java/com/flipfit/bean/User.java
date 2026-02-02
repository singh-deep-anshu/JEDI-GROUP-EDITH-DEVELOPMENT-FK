package com.flipfit.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
	private String userId;
	private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String city;
    private Role role; // "ADMIN", "CUSTOMER", or "OWNER"

	public User(String userId, String userName, String email, String password, Role role, String phoneNumber, String city) {
		this.userId = userId;
		this.name = userName;
		this.email = email;
		this.password = password;
		this.role = role;
		this.phoneNumber=phoneNumber;
		this.city=city;

	}


	
	/**
	 * Protected no-arg constructor for subclasses that use builder pattern.
	 */
	protected User() {
		// Default constructor for subclasses
	}
	@JsonProperty
    public String getUserId() {
		return userId;
	}

	@JsonProperty
	public void setUserId(String userId) {
		this.userId = userId;
	}

	@JsonProperty
	public String getName() {
		return name;
	}

	@JsonProperty
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty
	public String getEmail() {
		return email;
	}

	@JsonProperty
	public void setEmail(String email) {
		this.email = email;
	}

	@JsonProperty
	public String getPassword() {
		return password;
	}

	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	@JsonProperty
	public String getPhoneNumber() {
		return phoneNumber;
	}

	@JsonProperty
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@JsonProperty
	public String getCity() {
		return city;
	}

	@JsonProperty
	public void setCity(String city) {
		this.city = city;
	}

	@JsonProperty
	public Role getRole() {
		return role;
	}

	@JsonProperty
	public void setRole(Role role) {
		this.role = role;
	}
}
