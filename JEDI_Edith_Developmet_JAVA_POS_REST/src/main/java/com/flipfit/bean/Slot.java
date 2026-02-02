/**
 * 
 */
package com.flipfit.bean;

import java.time.LocalTime;

/**
 * 
 */
public class Slot {
	private String slotId;
	private String centerId;
    private LocalTime startTime;
    private LocalTime endTime;
    private int maxCapacity;
    private int currentBookings;
    
    public String getSlotId() {
		return slotId;
	}
	public void setSlotId(String slotId) {
		this.slotId = slotId;
	}
	
	public String getCenterId() {
		return centerId;
	}
	public void setCenterId(String centerId) {
		this.centerId = centerId;
	}
	
	public LocalTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}
	
	public LocalTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}
	
	public int getMaxCapacity() {
		return maxCapacity;
	}
	public void setMaxCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}
	
	public int getCurrentBookings() {
		return currentBookings;
	}
	public void setCurrentBookings(int currentBookings) {
		this.currentBookings = currentBookings;
	}
}
