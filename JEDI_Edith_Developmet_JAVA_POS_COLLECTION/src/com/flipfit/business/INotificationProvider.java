package com.flipfit.business;

public interface INotificationProvider {
	public boolean sendNotification(String customerId, String message);
}
