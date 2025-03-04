package com.openclassrooms.SafetyNetAlerts.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.openclassrooms.SafetyNetAlerts.util.SNAUtil;

import lombok.Data;

@Component
@Data
public class Mapper {
	public PersonDataFromAddressDTO toPersonDataFromAddressDto(Person person, String stationNumber) {
		String lastName = person.getLastName();
		String phone = person.getPhone();
		
		String age = "?";
		List<String> medication = new ArrayList<String>();
		List<String> allergies = new ArrayList<String>();
		MedicalRecord record = person.getRecord();
		if (record != null) {
			age = String.valueOf(SNAUtil.getAge(record.getBirthdate()));
			medication = record.getMedications();
			allergies = record.getAllergies();
		}
		
		return new PersonDataFromAddressDTO(lastName, phone, age, stationNumber, medication, allergies);
	}
	
	public PersonDataFromLastNameDTO toPersonDataFromLastNameDto(Person person) {
		String lastName = person.getLastName();
		String address = person.getAddress();
		
		String age = "?";
		List<String> medication = new ArrayList<String>();
		List<String> allergies = new ArrayList<String>();
		MedicalRecord record = person.getRecord();
		if (record != null) {
			age = String.valueOf(SNAUtil.getAge(record.getBirthdate()));
			medication = record.getMedications();
			allergies = record.getAllergies();
		}
		
		String email = person.getEmail();
		
		
		return new PersonDataFromLastNameDTO(lastName, address, age, email, medication, allergies);
	}
	
	public ChildDataFromAddressDTO toChildDataFromAddressDto(Person person, List<Person> otherMembers) {
		String firstName = person.getFirstName();
		String lastName = person.getLastName();
		
		String age = "?";
		MedicalRecord record = person.getRecord();
		if (record != null)
			age = String.valueOf(SNAUtil.getAge(record.getBirthdate()));
		
		return new ChildDataFromAddressDTO(firstName, lastName, age, otherMembers);
	}
	
	public PersonFromHouseholdDTO toPersonFromHouseholdDto(Person person) {
		String lastName = person.getLastName();
		String phone = person.getPhone();
		
		String age = "?";
		List<String> medication = new ArrayList<String>();
		List<String> allergies = new ArrayList<String>();
		MedicalRecord record = person.getRecord();
		if (record != null) {
			age = String.valueOf(SNAUtil.getAge(record.getBirthdate()));
			medication = record.getMedications();
			allergies = record.getAllergies();
		}
		
		return new PersonFromHouseholdDTO(lastName, phone, age, medication, allergies);
	}
	
	public HouseholdFromStationsDTO toHouseholdFromStationsDto(String address, List<PersonFromHouseholdDTO> occupants) {
		return new HouseholdFromStationsDTO(address, occupants);
	}
	
	public PersonFromStationNumberDTO toPersonFromStationDto(Person person) {
		String firstName = person.getFirstName();
		String lastName = person.getLastName();
		String address = person.getAddress();
		String phone = person.getPhone();
		
		return new PersonFromStationNumberDTO(firstName, lastName, address, phone);
	}
	
	public PersonsDataFromStationDTO toPersonsDataFromStationNumberDto(String adults, String children, List<PersonFromStationNumberDTO> persons) {
		return new PersonsDataFromStationDTO(adults, children, persons);
	}
}
