package com.openclassrooms.SafetyNetAlerts.model;

import java.util.List;

import lombok.Data;

@Data
public class PersonDataFromAddressDTO {
	private String lastName;
	private String phone;
	private String age;
	private List<String> medicalHistory;
	
	public PersonDataFromAddressDTO(String lastName, String phone, String age, List<String> medicalHistory) {
		this.lastName = lastName;
		this.phone = phone;
		this.age = age;
		this.medicalHistory = medicalHistory;
	}
}
