/**
 * 
 */
package com.flipfit.bean;

import java.util.Date;

/**
 * 
 */
public class GymCustomer extends User {
    private Date registrationDate;
    private boolean isActive;

	public GymCustomer(String userId, String userName, String email, String password, Role role, String phoneNumber, String city) {
		super(userId, userName, email, password, role, phoneNumber, city);
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}
	
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
