package com.openclassrooms.SafetyNetAlerts.model;

import lombok.Data;

@Data
public class Person {
	private String firstName;
	private String lastName;
	private String address;
	private String city;
	private String zip;
	private String phone;
	private String email;
	private MedicalRecord record;
	
	public boolean equals(Person personToCompare) {
		boolean result = false;
		
		if (personToCompare.getFirstName().equals(this.firstName) && personToCompare.getLastName().equals(this.lastName))
			result = true;
		
		return result;
	}
}
