package com.openclassrooms.SafetyNetAlerts.model;

import java.util.List;

import lombok.Data;

@Data
public class HouseholdFromStationsDTO {
	private String address;
	private List<PersonFromHouseholdDTO> occupants;
	
	public HouseholdFromStationsDTO(String address, List<PersonFromHouseholdDTO> occupants) {
		this.address = address;
		this.occupants = occupants;
	}
}
