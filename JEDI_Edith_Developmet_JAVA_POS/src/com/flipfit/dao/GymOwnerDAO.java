package com.flipfit.dao;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.Slot;
import java.util.List;

public interface GymOwnerDAO {
    void addGymCenter(GymCenter center);
    List<GymCenter> getGymsByOwner(String ownerId);
    List<GymCenter> getAllCenters();
    void addSlot(Slot slot);
    List<Slot> getSlotsByCenterId(String centerId);
}