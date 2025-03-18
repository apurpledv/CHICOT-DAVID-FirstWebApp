package com.openclassrooms.SafetyNetAlerts.model;

import java.util.List;

import org.springframework.stereotype.Component;

import com.openclassrooms.SafetyNetAlerts.util.SNAUtil;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Data
public class Mapper {
	public PersonDataFromAddressDTO toPersonDataFromAddressDto(Person person, String stationNumber) throws Exception {
		String lastName = person.getLastName();
		String phone = person.getPhone();

		MedicalRecord record = person.getRecord();
		int age = SNAUtil.getAge(record.getBirthdate());
		List<String> medication = record.getMedications();
		List<String> allergies = record.getAllergies();
		
		return new PersonDataFromAddressDTO(lastName, phone, age, stationNumber, medication, allergies); 
	}
	
	public PersonDataFromLastNameDTO toPersonDataFromLastNameDto(Person person) throws Exception  {
		String lastName = person.getLastName();
		String address = person.getAddress();
		String email = person.getEmail();

		MedicalRecord record = person.getRecord();
		int age = SNAUtil.getAge(record.getBirthdate());
		List<String> medication = record.getMedications();
		List<String> allergies = record.getAllergies();
		
		return new PersonDataFromLastNameDTO(lastName, address, age, email, medication, allergies);
	}
	
	public ChildDataFromAddressDTO toChildDataFromAddressDto(Person person, List<Person> otherMembers) throws Exception {
		String firstName = person.getFirstName();
		String lastName = person.getLastName();
		
		MedicalRecord record = person.getRecord();
		int age = SNAUtil.getAge(record.getBirthdate());
		
		return new ChildDataFromAddressDTO(firstName, lastName, age, otherMembers);
	}
	
	public PersonFromHouseholdDTO toPersonFromHouseholdDto(Person person) throws Exception {
		String lastName = person.getLastName();
		String phone = person.getPhone();
		
		MedicalRecord record = person.getRecord();
		int age = SNAUtil.getAge(record.getBirthdate());
		List<String> medication = record.getMedications();
		List<String> allergies = record.getAllergies();
		
		return new PersonFromHouseholdDTO(lastName, phone, age, medication, allergies);
	}
	
	public HouseholdFromStationsDTO toHouseholdFromStationsDto(String address, List<PersonFromHouseholdDTO> occupants) throws Exception {
		return new HouseholdFromStationsDTO(address, occupants);
	}
	
	public PersonFromStationNumberDTO toPersonFromStationDto(Person person) throws Exception {
		String firstName = person.getFirstName();
		String lastName = person.getLastName();
		String address = person.getAddress();
		String phone = person.getPhone();
		
		return new PersonFromStationNumberDTO(firstName, lastName, address, phone);
	}
	
	public PersonsDataFromStationDTO toPersonsDataFromStationNumberDto(int adults, int children, List<PersonFromStationNumberDTO> persons) throws Exception {
		return new PersonsDataFromStationDTO(adults, children, persons);
	}
}
