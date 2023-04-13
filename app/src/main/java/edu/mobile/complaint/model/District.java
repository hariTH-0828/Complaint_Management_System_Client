package edu.mobile.complaint.model;

public class District {
	private int id;
	private int stateId;
	private String district;
	public int getStateId() {
		return stateId;
	}

	public void setStateId(int stateId) {
		this.stateId = stateId;
	}
	
	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return district;
	}
}