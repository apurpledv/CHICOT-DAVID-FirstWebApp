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
	
	@Override
	public int hashCode() {
        return 1;
    }
	
	public boolean equals(Person personToCompare) {
		if (personToCompare.getFirstName().equals(this.firstName) && personToCompare.getLastName().equals(this.lastName))
			return true;
		
		return false;
	}
	
	public boolean IsValid() {
		if (this.firstName == null || this.firstName.isEmpty() || this.lastName == null || this.lastName.isEmpty() || this.address == null || this.address.isEmpty() || this.city == null || this.city.isEmpty() || this.zip == null || this.zip.isEmpty() || this.email == null || this.email.isEmpty() || this.phone == null || this.phone.isEmpty()) {
			return false;
		} else {
			return true; 
		}
	}
}
