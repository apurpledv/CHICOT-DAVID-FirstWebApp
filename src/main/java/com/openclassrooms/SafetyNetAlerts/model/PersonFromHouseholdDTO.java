package com.openclassrooms.SafetyNetAlerts.model;

import java.util.List;

import lombok.Data;

@Data
public class PersonFromHouseholdDTO {
	private String lastName;
	private String phone;
	private String age;
	private List<String> medication;
	private List<String> allergies;
	
	public PersonFromHouseholdDTO(String lastName, String phone, String age, List<String> medication, List<String> allergies) {
		this.lastName = lastName;
		this.phone = phone;
		this.age = age;
		this.medication = medication;
		this.allergies = allergies;
	}
}
