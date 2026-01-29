package com.flipfit.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WaitlistDAOImpl implements WaitlistDAO {
    // Key: SlotId, Value: List of UserIds (FIFO Queue)
    private static Map<String, List<String>> waitlistMap = new HashMap<>();

    @Override
    public void addUserToWaitlist(String slotId, String userId) {
        waitlistMap.computeIfAbsent(slotId, k -> new ArrayList<>()).add(userId);
    }

    @Override
    public String getNextUser(String slotId) {
        List<String> queue = waitlistMap.get(slotId);
        if (queue != null && !queue.isEmpty()) {
            return queue.remove(0); // Removes the first person (FIFO)
        }
        return null;
    }

    @Override
    public List<String> getAllWaitlistedUsers(String slotId) {
        return waitlistMap.getOrDefault(slotId, new ArrayList<>());
    }
}