/**
 * 
 */
package com.flipfit.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * 
 */
public class GymCustomer extends User {
    private Date registrationDate;
    private boolean isActive;

	public GymCustomer() { }

	public GymCustomer(String userId, String userName, String email, String password, Role role, String phoneNumber, String city) {
		super(userId, userName, email, password, role, phoneNumber, city);
	}

	@JsonProperty
	public Date getRegistrationDate() {
		return registrationDate;
	}

	@JsonProperty
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	@JsonProperty
	public boolean isActive() {
		return isActive;
	}

	@JsonProperty
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
