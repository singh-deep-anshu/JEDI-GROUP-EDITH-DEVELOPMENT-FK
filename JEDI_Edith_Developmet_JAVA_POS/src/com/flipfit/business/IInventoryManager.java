package com.flipfit.business;

import com.flipfit.bean.GymCenter;
import com.flipfit.exception.GymNotFoundException;

import java.util.List;

public interface IInventoryManager {
    void approveGymCenter(String centerId) throws GymNotFoundException;
    void approveGymOwner(String ownerId);
    public void manageCityData();
    List<GymCenter> viewPendingGyms();
    public void viewSystemAnalytics();
}
