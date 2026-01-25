package com.flipfit.business;

import com.flipfit.bean.GymCenter;

import java.util.List;

public interface IInventoryManager {
    void approveGymCenter(String centerId);
    void approveGymOwner(String ownerId);
    public void manageCityData();
    List<GymCenter> viewPendingGyms();
    public void viewSystemAnalytics();
}
