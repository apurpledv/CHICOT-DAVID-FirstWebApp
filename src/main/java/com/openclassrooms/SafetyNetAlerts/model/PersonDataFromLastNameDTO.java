package com.openclassrooms.SafetyNetAlerts.model;

import java.util.List;

import lombok.Data;

@Data
public class PersonDataFromLastNameDTO {
	private String lastName;
	private String address;
	private int age;
	private String email;
	private List<String> medication;
	private List<String> allergies;
	
	public PersonDataFromLastNameDTO(String lastName, String address, int age, String email, List<String> medication, List<String> allergies) {
		this.lastName = lastName;
		this.address = address;
		this.age = age;
		this.email = email;
		this.medication = medication;
		this.allergies = allergies;
	}
}
