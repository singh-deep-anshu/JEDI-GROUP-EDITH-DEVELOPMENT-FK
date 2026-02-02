package com.flipfit.dao;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.Slot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GymOwnerDAOImpl implements GymOwnerDAO {
    // The "Database" for Gyms and Slots
    private static Map<String, GymCenter> gymCentersMap = new HashMap<>();
    private static Map<String, List<Slot>> slotsMap = new HashMap<>();

    @Override
    public void addGymCenter(GymCenter center) {
        gymCentersMap.put(center.getCenterId(), center);
    }

    @Override
    public List<GymCenter> getGymsByOwner(String ownerId) {
        return gymCentersMap.values().stream()
                .filter(gym -> gym.getOwnerId().equals(ownerId))
                .collect(Collectors.toList());
    }

    @Override
    public List<GymCenter> getAllCenters() {
        return new ArrayList<>(gymCentersMap.values());
    }

    @Override
    public void addSlot(Slot slot) {
        slotsMap.computeIfAbsent(slot.getCenterId(), k -> new ArrayList<>()).add(slot);
    }

    @Override
    public List<Slot> getSlotsByCenterId(String centerId) {
        return slotsMap.getOrDefault(centerId, new ArrayList<>());
    }

    @Override
    public Slot getSlotById(String slotId) {
        for (List<Slot> slots : slotsMap.values()) {
            for (Slot slot : slots) {
                if (slot.getSlotId().equals(slotId)) {
                    return slot;
                }
            }
        }
        return null;
    }


}