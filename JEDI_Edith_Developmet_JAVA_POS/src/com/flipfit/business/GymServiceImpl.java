package com.flipfit.business;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.Slot;
import com.flipfit.dao.GymOwnerDAO;
import com.flipfit.dao.GymOwnerDAOImpl;

public class GymServiceImpl implements GymService {

	private GymOwnerDAO gymOwnerDAO = new GymOwnerDAOImpl();

	@Override
	public GymCenter registerGymCenter(String ownerId, GymCenter details) {
		// TODO Auto-generated method stub
		String centerId = UUID.randomUUID().toString();
		details.setCenterId(centerId);
		details.setOwnerId(ownerId);
		details.setActive(false); // pending admin approval

		gymOwnerDAO.addGymCenter(details);
		return details;

	}

	@Override
	public void addSlot(String centerId, Slot slotDetails) {
		// TODO Auto-generated method stub
		String slotId = UUID.randomUUID().toString();
		slotDetails.setSlotId(slotId);
		slotDetails.setCenterId(centerId);
		slotDetails.setCurrentBookings(0);

		gymOwnerDAO.addSlot(slotDetails);
		
	}

	@Override
	public List<GymCenter> searchCenters(String city, String filters) {
		// TODO Auto-generated method stub
		return gymOwnerDAO.getAllCenters()
				.stream()
				.filter(center ->
						center.isActive()
								&& center.getCityId().equalsIgnoreCase(city))
				.collect(Collectors.toList());

	}

	@Override
	public List<Slot> getAvailableSlots(String centerId, String date) {
		// TODO Auto-generated method stub
		List<Slot> availableSlots = new ArrayList<>();

		for (Slot slot : gymOwnerDAO.getSlotsByCenterId(centerId)) {
			if (slot.getCurrentBookings() < slot.getMaxCapacity()) {
				availableSlots.add(slot);
			}
		}
		return availableSlots;
	}

	@Override
	public void updateCenterInfo(String centerId, GymCenter details) {
		// TODO Auto-generated method stub
		gymOwnerDAO.addGymCenter(details);

	}

	@Override
	public boolean approveGymCenter(String centerId) {
		// TODO Auto-generated method stub
		for (GymCenter center : gymOwnerDAO.getAllCenters()) {
			if (center.getCenterId().equals(centerId)) {
				center.setActive(true);
				return true;
			}
		}
		return false;
	}

}
