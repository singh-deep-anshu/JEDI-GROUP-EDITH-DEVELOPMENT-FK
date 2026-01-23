package com.flipfit.business;

import java.util.List;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.Slot;

public interface GymService {
	public GymCenter registerGymCenter(String ownerId, GymCenter details);
    public void addSlot(String centerId, Slot slotDetails);
    public List<GymCenter> searchCenters(String city, String filters);
    public List<Slot> getAvailableSlots(String centerId, String date);
    public void updateCenterInfo(String centerId, GymCenter details);
    public boolean approveGymCenter(String centerId);
}
