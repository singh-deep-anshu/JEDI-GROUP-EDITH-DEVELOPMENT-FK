package com.flipfit.business;

import com.flipfit.bean.GymCustomer;
import com.flipfit.bean.GymOwner;

public interface AccountService {
	public GymCustomer registerCustomer(GymCustomer customerDetails);
    public GymOwner registerOwner(GymOwner ownerDetails);
    public String login(String email, String password);
    public boolean resetPassword(String email);
}
