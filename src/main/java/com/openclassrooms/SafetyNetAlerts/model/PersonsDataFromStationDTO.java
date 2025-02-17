package com.openclassrooms.SafetyNetAlerts.model;

import java.util.List;

import lombok.Data;

@Data
public class PersonsDataFromStationDTO {
	private String adults;
	private String children;
	private List<PersonFromStationNumberDTO> persons;
	
	public PersonsDataFromStationDTO(String adults, String children, List<PersonFromStationNumberDTO> persons) {
		this.adults = adults;
		this.children = children;
		this.persons = persons;
	}
}
