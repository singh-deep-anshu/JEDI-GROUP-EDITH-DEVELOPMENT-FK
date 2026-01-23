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
