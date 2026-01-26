package com.flipfit.business;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.Slot;
import com.flipfit.dao.GymOwnerDAO;
import com.flipfit.dao.GymOwnerDAOImpl;
import com.flipfit.dao.UserDAO;
import com.flipfit.dao.UserDAOImpl;
import com.flipfit.exception.GymNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Admin service for managing gym centers and owners.
 * Handles approval workflows and system analytics.
 */
public class AdminService implements IInventoryManager, IReportViewer {

	private GymOwnerDAO gymOwnerDAO = new GymOwnerDAOImpl();
	private UserDAO userDAO = new UserDAOImpl();
	private GymService gymService = new GymServiceImpl();

	/**
	 * Approves a gym center for operation.
	 * Changes the gym status from pending to active.
	 * 
	 * @param centerId The gym center ID to approve
	 * @throws GymNotFoundException  if gym doesn't exist
	 * @throws IllegalStateException if gym is already approved
	 */
	@Override
	public void approveGymCenter(String centerId) {
		if (centerId == null || centerId.trim().isEmpty()) {
			throw new IllegalArgumentException("Center ID cannot be null or empty");
		}

		try {
			gymService.verifyGym(centerId);
			System.out.println("✓ Gym center '" + centerId + "' has been approved successfully.");
		} catch (GymNotFoundException e) {
			throw new GymNotFoundException(centerId);
		} catch (IllegalStateException e) {
			throw e;
		}
	}

	/**
	 * Approves a gym owner for operation.
	 * Sets the owner's verified status to true.
	 * 
	 * @param ownerId The gym owner ID to approve
	 * @throws IllegalArgumentException if owner doesn't exist
	 */
	@Override
	public void approveGymOwner(String ownerId) {
		if (ownerId == null || ownerId.trim().isEmpty()) {
			throw new IllegalArgumentException("Owner ID cannot be null or empty");
		}

		// Fetch the user
		Object user = userDAO.getUserById(ownerId);

		if (user == null || !(user instanceof GymOwner)) {
			throw new IllegalArgumentException("Gym owner with ID '" + ownerId + "' not found");
		}

		GymOwner owner = (GymOwner) user;

		// Check if already verified
		if (owner.isVerified()) {
			throw new IllegalStateException("Gym owner '" + ownerId + "' is already verified");
		}

		// Mark owner as verified
		owner.setVerified(true);

		// Update in DAO
		userDAO.updateUser(owner);

		System.out.println("✓ Gym owner '" + ownerId + "' has been verified successfully.");
	}

	/**
	 * Gets all pending gym centers awaiting admin approval.
	 * 
	 * @return List of inactive (pending) gym centers
	 */
	@Override
	public List<GymCenter> viewPendingGyms() {
		return gymOwnerDAO.getAllCenters()
				.stream()
				.filter(gym -> !gym.isActive()) // Inactive = pending approval
				.collect(Collectors.toList());
	}

	/**
	 * Manages city data (placeholder for future implementation).
	 * Can be used to add/remove/update valid Flipfit cities.
	 */
	@Override
	public void manageCityData() {
		System.out.println("\n===== CITY MANAGEMENT =====");
		System.out.println(
				"Current valid cities: Bangalore, Mumbai, Delhi, Chennai, Hyderabad, Pune, Kolkata, Ahmedabad, Jaipur, Lucknow");
		System.out.println("City management features coming soon...");
	}

	/**
	 * Views system analytics showing gym statistics.
	 */
	@Override
	public void viewSystemAnalytics() {
		System.out.println("\n===== SYSTEM ANALYTICS =====");

		List<GymCenter> allGyms = gymOwnerDAO.getAllCenters();
		long approvedGyms = allGyms.stream().filter(GymCenter::isActive).count();
		long pendingGyms = allGyms.stream().filter(gym -> !gym.isActive()).count();

		System.out.println("Total Gym Centers: " + allGyms.size());
		System.out.println("Approved Gyms: " + approvedGyms);
		System.out.println("Pending Approval: " + pendingGyms);
	}

	@Override
	public void viewAnalytics() {
		viewSystemAnalytics();
	}

	@Override
	public void viewCustomerGrowth() {
		System.out.println("Customer growth analytics coming soon...");
	}

	/**
	 * Adds a new gym center with validation of city and owner.
	 * Simple admin operation to add gym centers to the system.
	 * 
	 * @param gym The gym center to add
	 * @return The added GymCenter with generated ID
	 * @throws IllegalArgumentException if gym validation fails
	 * @throws GymNotFoundException     if owner does not exist
	 */
	public GymCenter addCenter(GymCenter gym) {
		GymCenter addedGym = gymService.addCenter(gym);
		System.out.println("✓ Gym center '" + gym.getName() + "' has been added successfully.");
		System.out.println("  Center ID: " + addedGym.getCenterId());
		System.out.println("  Status: Pending Admin Approval");
		return addedGym;
	}

	/**
	 * Views all gym centers in a specific city.
	 * Admin operation to search gyms by city.
	 * 
	 * @param city The city to search in
	 * @return List of all gym centers in the city
	 * @throws IllegalArgumentException if city is null or empty
	 */
	public List<GymCenter> viewCenters(String city) {
		return gymService.viewCenters(city);
	}

	/**
	 * Adds a new time slot to a gym center.
	 * Creates slots like 6AM-7AM, 7AM-8AM, etc.
	 * 
	 * @param slot The slot to add (must have centerId, startTime, endTime,
	 *             maxCapacity set)
	 * @return The created Slot with generated ID
	 * @throws IllegalArgumentException if slot validation fails
	 * @throws GymNotFoundException     if gym center not found
	 */
	public Slot addSlot(Slot slot) {
		Slot createdSlot = gymService.addSlot(slot);
		System.out.println("✓ Slot created successfully!");
		System.out.println("  Slot ID: " + createdSlot.getSlotId());
		System.out.println("  Center ID: " + createdSlot.getCenterId());
		System.out.println("  Time: " + createdSlot.getStartTime() + " - " + createdSlot.getEndTime());
		System.out.println("  Max Capacity: " + createdSlot.getMaxCapacity());
		return createdSlot;
	}

	/**
	 * Checks if a new slot would overlap with existing slots for a gym center.
	 * Prevents double-booking and conflicts.
	 * 
	 * @param centerId  The gym center ID
	 * @param startTime The start time of the new slot
	 * @param endTime   The end time of the new slot
	 * @return true if conflict exists, false if slot can be added
	 */
	public boolean checkSlotConflict(String centerId, java.time.LocalTime startTime, java.time.LocalTime endTime) {
		if (centerId == null || centerId.trim().isEmpty()) {
			throw new IllegalArgumentException("Center ID cannot be null or empty");
		}

		List<Slot> existingSlots = gymOwnerDAO.getSlotsByCenterId(centerId);

		return existingSlots.stream()
				.anyMatch(slot -> slotsOverlap(startTime, endTime, slot.getStartTime(), slot.getEndTime()));
	}

	/**
	 * Helper method to check if two time ranges overlap.
	 */
	private boolean slotsOverlap(java.time.LocalTime newStart, java.time.LocalTime newEnd,
			java.time.LocalTime existingStart, java.time.LocalTime existingEnd) {
		return newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart);
	}

	/**
	 * Views all slots for a gym center with conflict warnings.
	 * 
	 * @param centerId The gym center ID
	 * @return List of slots for the center
	 */
	public List<Slot> viewSlotsForCenter(String centerId) {
		if (centerId == null || centerId.trim().isEmpty()) {
			throw new IllegalArgumentException("Center ID cannot be null or empty");
		}

		List<Slot> slots = gymOwnerDAO.getSlotsByCenterId(centerId);
		return slots;
	}
}
