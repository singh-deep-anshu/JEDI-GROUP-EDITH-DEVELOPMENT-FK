package com.flipfit.business;

import com.flipfit.bean.GymCustomer;

public interface WaitlistService {
	public void addToWaitlist(String customerId, String slotId);
    public GymCustomer promoteNextUser(String slotId);
}
