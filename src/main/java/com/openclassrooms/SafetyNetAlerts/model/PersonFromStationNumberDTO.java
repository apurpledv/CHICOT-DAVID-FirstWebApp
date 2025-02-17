package com.openclassrooms.SafetyNetAlerts.model;

import lombok.Data;

@Data
public class PersonFromStationNumberDTO {
	private String firstName;
	private String lastName;
	private String address;
	private String phone;
	
	public PersonFromStationNumberDTO(String firstName, String lastName, String address, String phone) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.phone = phone;
	}
}
