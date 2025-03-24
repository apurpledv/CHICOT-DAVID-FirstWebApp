package com.openclassrooms.SafetyNetAlerts.model;

import lombok.Data;

@Data
public class FireStation {
	private String address;
	private String station;
	
	@Override
	public int hashCode() {
        return 2;
    }
	
	public boolean equals(FireStation stationToCompare) {
		if (stationToCompare.getAddress().equals(this.address))
			return true;
		
		return false;
	}
	
	public boolean IsValid() {
		if (this.address == null || this.address.isEmpty() || this.station == null || this.station.isEmpty()) {
			return false;
		} else {
			return true; 
		}
	}
}
