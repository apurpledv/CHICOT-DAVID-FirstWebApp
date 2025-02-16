package com.openclassrooms.SafetyNetAlerts.model;

import java.util.List;

import lombok.Data;

@Data
public class ChildDataFromAddressDTO {
	private String firstName;
	private String lastName;
	private String age;
	private List<Person> otherMembers;
	
	public ChildDataFromAddressDTO(String firstName, String lastName, String age, List<Person> otherMembers) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.otherMembers = otherMembers;
	}
}
