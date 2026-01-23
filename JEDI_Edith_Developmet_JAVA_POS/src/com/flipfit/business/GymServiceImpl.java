package com.flipfit.business;

import java.util.List;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.Slot;

public class GymServiceImpl implements GymService {

	@Override
	public GymCenter registerGymCenter(String ownerId, GymCenter details) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addSlot(String centerId, Slot slotDetails) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<GymCenter> searchCenters(String city, String filters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Slot> getAvailableSlots(String centerId, String date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateCenterInfo(String centerId, GymCenter details) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean approveGymCenter(String centerId) {
		// TODO Auto-generated method stub
		return false;
	}

}
