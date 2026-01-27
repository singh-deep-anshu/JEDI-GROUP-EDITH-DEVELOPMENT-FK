package com.flipfit.bean;

public class GymAdmin extends User {
    public GymAdmin(String userId, String userName, String email, String password, String phoneNumber, String city) {
        super(userId, userName, email, password, Role.ADMIN, phoneNumber, city);
    }
}
