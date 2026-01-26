package com.flipfit.bean;

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
    public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
}
