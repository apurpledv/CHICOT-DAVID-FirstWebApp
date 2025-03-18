package com.openclassrooms.SafetyNetAlerts.model;

import java.util.List;

import lombok.Data;

@Data
public class PersonsDataFromStationDTO {
	private int adults;
	private int children;
	private List<PersonFromStationNumberDTO> persons;
	
	public PersonsDataFromStationDTO(int adults, int children, List<PersonFromStationNumberDTO> persons) {
		this.adults = adults;
		this.children = children;
		this.persons = persons;
	}
}
