package com.flipfit.dao;

import java.util.List;

public interface WaitlistDAO {
    void addUserToWaitlist(String slotId, String userId);
    String getNextUser(String slotId); // Retrieves and removes the first user
    List<String> getAllWaitlistedUsers(String slotId);
}