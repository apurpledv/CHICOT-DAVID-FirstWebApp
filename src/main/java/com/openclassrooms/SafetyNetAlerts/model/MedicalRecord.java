package com.openclassrooms.SafetyNetAlerts.model;

import java.util.List;

import lombok.Data;

@Data
public class MedicalRecord {
	private String firstName;
	private String lastName;
	private String birthdate;
	private List<String> medications;
	private List<String> allergies;
	
	@Override
	public int hashCode() {
        return 3;
    }
	
	public boolean equals(MedicalRecord recordToCompare) {
		if (recordToCompare.getFirstName().equals(this.firstName) && recordToCompare.getLastName().equals(this.lastName))
			return true;
		
		return false;
	}
	
	public boolean IsValid() {
		if (this.firstName == null || this.firstName.isEmpty() || this.lastName == null || this.lastName.isEmpty() || this.birthdate == null || this.birthdate.isEmpty()) {
			return false;
		} else {
			return true; 
		}
	}
}
