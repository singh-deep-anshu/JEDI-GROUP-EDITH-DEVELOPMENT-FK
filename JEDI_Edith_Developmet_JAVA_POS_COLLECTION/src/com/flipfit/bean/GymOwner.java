package com.flipfit.bean;

public class GymOwner extends User {
	private String panNumber;
	private String gstNumber;
    private boolean isVerified;
    
    /**
     * Creates a new GymOwner with all required User fields.
     */
    public GymOwner(String userId, String userName, String email, String password, String phoneNumber, String city) {
        super(userId, userName, email, password, Role.GYM_OWNER, phoneNumber, city);
    }
    
    /**
     * No-arg constructor for builder pattern.
     */
    public GymOwner() {
        super();
        setRole(Role.GYM_OWNER);
    }
    
    public String getPanNumber() {
		return panNumber;
	}
	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}
	
	public String getGstNumber() {
		return gstNumber;
	}
	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}
	
	public boolean isVerified() {
		return isVerified;
	}
	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}
}
