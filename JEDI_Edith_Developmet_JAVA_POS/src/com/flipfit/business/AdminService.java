package com.flipfit.business;
import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.dao.GymOwnerDAO;
import com.flipfit.dao.GymOwnerDAOImpl;
import java.util.List;
import java.util.stream.Collectors;
import com.flipfit.bean.GymCenter;

import java.util.List;

public class AdminService implements IInventoryManager, IReportViewer {
	private GymOwnerDAO gymOwnerDAO = new GymOwnerDAOImpl();
	@Override
	public void viewAnalytics() {
		// TODO Auto-generated method stub
		int totalCenters = gymOwnerDAO.getAllCenters().size();
		System.out.println("Total Registered Gym Centers: " + totalCenters);
	}

	@Override
	public void viewCustomerGrowth() {
		// TODO Auto-generated method stub
//		List<GymCustomer> customers = FlipFitDataStorage.gymCustomers;

//		System.out.println("--- Customer Growth Analytics ---");
//		System.out.println("Total Registered Customers: " + customers.size());
//
//		// Example of counting active vs inactive users
//		long activeCount = customers.stream().filter(GymCustomer::isActive).count();
//		System.out.println("Active Customers: " + activeCount);
//		System.out.println("Inactive Customers: " + (customers.size() - activeCount));
	}

	@Override
	public void approveGymCenter(String centerId) {
		for (GymCenter center : gymOwnerDAO.getAllCenters()) {
			if (center.getCenterId().equals(centerId)) {
				center.setActive(true);
				System.out.println("Gym Center " + centerId + " has been approved and is now active.");
				return;
			}
		}
		System.out.println("Gym Center with ID " + centerId + " not found.");
	}

	@Override
	public void approveGymOwner(String ownerId) {
		List<GymOwner> owners = null; // FlipFitDataStorage.gymOwners; // add this line when created the dummy database named as FlipFitDataStorage

		boolean found = false;
		for (GymOwner owner : owners) {
			if (owner.getUserId().equals(ownerId)) {
				// 2. Business Logic: Update status
				owner.setVerified(true);
				found = true;
				System.out.println("Successfully verified Gym Owner: " + owner.getName());
				break;
			}
		}

		if (!found) {
			System.out.println("Error: Gym Owner with ID " + ownerId + " not found.");
		}
	}

	@Override
	public void manageCityData() {
		// TODO Auto-generated method stub

//		System.out.println("--- City-wise Gym Distribution ---");
//
//		// Using Java Streams to group by City ID and count
//		Map<String, Long> cityGymCount = gymOwnerDAO.getAllCenters().stream()
//				.collect(Collectors.groupingBy(GymCenter::getCity, Collectors.counting()));
//
//		if (cityGymCount.isEmpty()) {
//			System.out.println("No gym data available for any city.");
//		} else {
//			cityGymCount.forEach((city, count) ->
//					System.out.println("City: " + city + " | Total Gyms: " + count));
//		}	
	}

	@Override
	public List<GymCenter> viewPendingGyms() {
		return gymOwnerDAO.getAllCenters()
				.stream()
				.filter(center -> !center.isActive())
				.collect(Collectors.toList());
	}

	@Override
	public void viewSystemAnalytics() {
		// TODO Auto-generated method stub
		System.out.println("System Health: Healthy | Total Centers: " + gymOwnerDAO.getAllCenters().size());
	}

}
