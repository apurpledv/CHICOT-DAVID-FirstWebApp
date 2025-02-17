package com.openclassrooms.SafetyNetAlerts.model;

import lombok.Data;

@Data
public class PersonFromStationNumberDTO {
	private String firstName;
	private String lastName;
	private String phone;
	private String age;
	
	public PersonFromStationNumberDTO(String firstName, String lastName, String phone, String age) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.age = age;
	}
}
