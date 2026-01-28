package com.flipfit.business;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.Slot;
import com.flipfit.dao.GymOwnerDAO;
import com.flipfit.dao.GymOwnerDAOImpl;
import com.flipfit.dao.UserDAO;
import com.flipfit.dao.UserDAOImpl;
import com.flipfit.exception.GymNotFoundException;
import com.flipfit.exception.GymOwnerNotVerifiedException;
import com.flipfit.exception.InvalidSlotException;
import com.flipfit.exception.UnauthorizedAccessException;
// import com.flipfit.validation.GymValidator;
// import com.flipfit.validation.SlotValidator;
// import com.flipfit.validation.ValidationResult;
import com.flipfit.dao.*;

public class GymServiceImpl implements GymService {

	private GymOwnerDAO gymOwnerDAO = new GymOwnerDAOImpl();
	private UserDAO userDAO = new UserDAOImpl();
	// private GymValidator gymValidator = new GymValidator();
	// private SlotValidator slotValidator = new SlotValidator();
	private GymCenterDAO gymCenterDAO = new GymCenterDAOImpl();
	private SlotDAO slotDAO = new SlotDAOImpl();

	/**
	 * Registers a new gym center after validating the gym owner and gym details.
	 * 
	 * @param ownerId The ID of the gym owner registering the center
	 * @param gym     The gym center details to register
	 * @return The registered GymCenter with a generated ID
	 * @throws UnauthorizedAccessException  if the user is not a gym owner
	 * @throws GymOwnerNotVerifiedException if the gym owner is not verified
	 * @throws IllegalArgumentException     if gym details fail validation
	 */
	@Override
	public GymCenter registerGymCenter(String ownerId, GymCenter gym) throws GymNotFoundException, UnauthorizedAccessException, GymOwnerNotVerifiedException {
		// Validate that ownerId exists and is a GymOwner
		GymOwner owner = validateOwnerExists(ownerId);

		// Check if owner is verified
		if (!owner.isVerified()) {
			throw new GymOwnerNotVerifiedException(ownerId, "register gym center");
		}

		// Validate gym details
		// ValidationResult validationResult = gymValidator.validate(gym);
		// if (!validationResult.isValid()) {
		// 	throw new IllegalArgumentException("Gym validation failed: " + validationResult.getErrorsAsString());
		// }

		// Set owner ID and generate unique center ID
		gym.setOwnerId(ownerId);
		gym.setCenterId(UUID.randomUUID().toString());
		gym.setActive(false); // Pending admin approval by default

		// Save to DAO
		gymCenterDAO.addGymCenter(gym);

		return gym;
	}

	/**
	 * Adds a new slot to a gym center with validation and overlap detection.
	 * 
	 * @param centerId    The gym center ID
	 * @param slotDetails The slot details to add
	 * @return The created Slot with generated ID
	 */
	public Slot addSlot(String centerId, Slot slotDetails) throws InvalidSlotException, GymNotFoundException {
		if (centerId == null || centerId.trim().isEmpty()) {
			throw new IllegalArgumentException("Center ID cannot be null or empty");
		}

		GymCenter gym = getGymCenterById(centerId);
		if (gym == null) {
			throw new GymNotFoundException(centerId);
		}

		slotDetails.setCenterId(centerId);
		// ValidationResult validationResult = slotValidator.validate(slotDetails);
		// if (!validationResult.isValid()) {
		// 	throw new IllegalArgumentException("Slot validation failed: " + validationResult.getErrorsAsString());
		// }

		List<Slot> existingSlots = slotDAO.getSlotsByCenterId(centerId);
		if (hasOverlappingSlots(slotDetails, existingSlots)) {
			throw new InvalidSlotException(slotDetails.getStartTime(), slotDetails.getEndTime(),
					"This time slot overlaps with an existing slot. Please choose a different time.");
		}

		slotDetails.setSlotId(UUID.randomUUID().toString());
		slotDetails.setCurrentBookings(0);
		slotDAO.addSlot(slotDetails);

		return slotDetails;
	}

	/**
	 * Simplified version of addSlot where centerId is included in slot object.
	 * Creates a new time slot for a gym center (e.g., 6AM-7AM).
	 * 
	 * @param slot The slot to add (must have centerId set)
	 * @return The created Slot with generated ID
	 * @throws IllegalArgumentException if slot validation fails
	 * @throws GymNotFoundException     if gym center not found
	 * @throws InvalidSlotException     if slot overlaps with existing slots
	 */
	@Override
	public Slot addSlot(Slot slot) throws InvalidSlotException, GymNotFoundException {
		// Validate slot object
		if (slot == null) {
			throw new IllegalArgumentException("Slot cannot be null");
		}

		// Validate centerId in slot
		String centerId = slot.getCenterId();
		if (centerId == null || centerId.trim().isEmpty()) {
			throw new IllegalArgumentException("Center ID in slot cannot be null or empty");
		}

		// Delegate to the other addSlot method
		return addSlot(centerId, slot);
	}

	/**
	 * Searches for gym centers in a specific city.
	 * Returns only active (approved) gym centers.
	 * 
	 * @param city    The city to search in
	 * @param filters Optional filters for the search
	 * @return List of active gym centers in the specified city
	 * @throws IllegalArgumentException if city is null or invalid
	 */
	@Override
	public List<GymCenter> searchCenters(String city, String filters) {
		// Validate city
		if (city == null || city.trim().isEmpty()) {
			throw new IllegalArgumentException("City cannot be null or empty");
		}

		// Validate city is a valid Flipfit location
		// if (!gymValidator.isValidCity(city)) {
		// 	throw new IllegalArgumentException("City '" + city + "' is not a valid Flipfit location");
		// }

		// Fetch all gym centers and filter by city and active status
		List<GymCenter> allCenters = gymCenterDAO.getAllGymCenters();

		List<GymCenter> filteredCenters = allCenters.stream()
				.filter(gym -> gym.getCityId() != null && gym.getCityId().equalsIgnoreCase(city.trim()))
				.filter(gym -> gym.isActive()) // Only return approved gyms
				.collect(Collectors.toList());

		return filteredCenters;
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

	/**
	 * Verifies (approves) a gym center for operation by admin.
	 * Changes gym status from inactive to active.
	 * 
	 * @param centerId The ID of the gym center to approve
	 * @return true if verification successful
	 * @throws GymNotFoundException  if gym center with given ID doesn't exist
	 * @throws IllegalStateException if gym is already verified
	 */
	@Override
	public boolean verifyGym(String centerId) throws GymNotFoundException {
		// Validate centerId
		if (centerId == null || centerId.trim().isEmpty()) {
			throw new IllegalArgumentException("Center ID cannot be null or empty");
		}

		// Fetch the gym center
		GymCenter gym = getGymCenterById(centerId);
		if (gym == null) {
			throw new GymNotFoundException(centerId);
		}

		// Check if already verified
		if (gym.isActive()) {
			throw new IllegalStateException("Gym center '" + centerId + "' is already verified");
		}

		// Mark gym as active
		gym.setActive(true);

		return true;
	}

	@Override
	public boolean approveGymCenter(String centerId) throws GymNotFoundException {
		// Delegate to verifyGym for consistency
		return verifyGym(centerId);
	}

	/**
	 * Gets a gym center by its ID.
	 * 
	 * @param centerId The gym center ID
	 * @return The GymCenter if found, null otherwise
	 */
	private GymCenter getGymCenterById(String centerId) {
		List<GymCenter> allCenters = gymCenterDAO.getAllGymCenters();
		return allCenters.stream()
				.filter(gym -> gym.getCenterId().equals(centerId))
				.findFirst()
				.orElse(null);
	}

	/**
	 * Checks if a slot overlaps with any existing slots for the same center.
	 * Prevents double-booking of time slots.
	 * 
	 * @param newSlot       The new slot to check
	 * @param existingSlots The existing slots for the center
	 * @return true if there's an overlap, false otherwise
	 */
	private boolean hasOverlappingSlots(Slot newSlot, List<Slot> existingSlots) {
		return existingSlots.stream()
				.anyMatch(slot -> slotsOverlap(newSlot, slot));
	}

	/**
	 * Checks if two slots overlap in time.
	 * Slots overlap if: newSlot.startTime < existingSlot.endTime AND
	 * newSlot.endTime > existingSlot.startTime
	 * 
	 * @param newSlot      The new slot
	 * @param existingSlot The existing slot to compare
	 * @return true if slots overlap, false otherwise
	 */
	private boolean slotsOverlap(Slot newSlot, Slot existingSlot) {
		return newSlot.getStartTime().isBefore(existingSlot.getEndTime()) &&
				newSlot.getEndTime().isAfter(existingSlot.getStartTime());
	}

	/**
	 * Validates that the owner exists in the system and is a gym owner.
	 * 
	 * @param ownerId The owner ID to validate
	 * @return The GymOwner object
	 * @throws UnauthorizedAccessException if user is not a gym owner
	 * @throws GymNotFoundException        if owner does not exist
	 */
	private GymOwner validateOwnerExists(String ownerId) throws GymNotFoundException, UnauthorizedAccessException {
		// Fetch user by ID from DAO
		Object user = userDAO.getUserById(ownerId);

		if (user == null) {
			throw new GymNotFoundException("Gym owner with ID " + ownerId + " not found");
		}

		if (!(user instanceof GymOwner)) {
			throw new UnauthorizedAccessException(
					"Only gym owners can register centers",
					"User must have GYM_OWNER role");
		}

		return (GymOwner) user;
	}

	/**
	 * Adds a new gym center with validation of city and owner.
	 * Simple admin operation to add gym centers.
	 * 
	 * @param gym The gym center to add
	 * @return The added GymCenter with generated ID
	 * @throws IllegalArgumentException if gym validation fails
	 * @throws GymNotFoundException     if owner does not exist
	 */
	@Override
	public GymCenter addCenter(GymCenter gym) throws GymNotFoundException, UnauthorizedAccessException {
		// Validate gym details (name, city, capacity, address)
		// ValidationResult validationResult = gymValidator.validate(gym);
		// if (!validationResult.isValid()) {
		// 	throw new IllegalArgumentException("Gym validation failed: " + validationResult.getErrorsAsString());
		// }

		// Validate owner exists
		if (gym.getOwnerId() == null || gym.getOwnerId().trim().isEmpty()) {
			throw new IllegalArgumentException("Owner ID is required");
		}

		GymOwner owner = validateOwnerExists(gym.getOwnerId());

		// Generate unique center ID
		gym.setCenterId(UUID.randomUUID().toString());
		gym.setActive(false); // Pending admin approval by default

		// Save to DAO
		gymCenterDAO.addGymCenter(gym);

		return gym;
	}

	/**
	 * Views all gym centers in a specific city.
	 * Admin operation to search gyms by city. Returns both active and pending gyms.
	 * 
	 * @param city The city to search in
	 * @return List of all gym centers in the city
	 * @throws IllegalArgumentException if city is null or empty
	 */
	@Override
	public List<GymCenter> viewCenters(String city) {
		// Validate city input
		if (city == null || city.trim().isEmpty()) {
			throw new IllegalArgumentException("City cannot be null or empty");
		}

		// Fetch all gym centers and filter by city
		List<GymCenter> allCenters = gymCenterDAO.getAllGymCenters();

		List<GymCenter> centersByCity = allCenters.stream()
				.filter(gym -> gym.getCityId() != null && gym.getCityId().equalsIgnoreCase(city.trim()))
				.collect(Collectors.toList());

		return centersByCity;
	}

}
