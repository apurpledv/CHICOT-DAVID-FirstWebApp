package com.openclassrooms.SafetyNetAlerts.model;

import java.util.List;

import lombok.Data;

@Data
public class PersonDataFromAddressDTO {
	private String lastName;
	private String phone;
	private String age;
	private String stationNumber;
	private List<String> medication;
	private List<String> allergies;
	
	public PersonDataFromAddressDTO(String lastName, String phone, String age, String stationNumber, List<String> medication, List<String> allergies) {
		this.lastName = lastName;
		this.phone = phone;
		this.age = age;
		this.stationNumber = stationNumber;
		this.medication = medication;
		this.allergies = allergies;
	}
}
