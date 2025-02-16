package com.openclassrooms.SafetyNetAlerts.model;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Component;

import com.openclassrooms.SafetyNetAlerts.util.SNAUtil;

import lombok.Data;

@Component
@Data
public class Mapper {
	public PersonDataFromAddressDTO toDTO(Person person, String stationNumber) {
		String lastName = person.getLastName();
		String phone = person.getPhone();
		MedicalRecord record = person.getRecord();
		
		// Get Age from Birthdate
		String age = String.valueOf(SNAUtil.getAge(record.getBirthdate()));
		
		List<String> medication = record.getMedications();
		List<String> allergies = record.getAllergies();
		
		return new PersonDataFromAddressDTO(lastName, phone, age, stationNumber, medication, allergies);
	}
	
	public PersonDataFromLastNameDTO toDTO(Person person) {
		String lastName = person.getLastName();
		String address = person.getAddress();
		MedicalRecord record = person.getRecord();
		
		// Get Age from Birthdate
		String age = String.valueOf(SNAUtil.getAge(record.getBirthdate()));
		
		String email = person.getEmail();
		List<String> medication = record.getMedications();
		List<String> allergies = record.getAllergies();
		
		return new PersonDataFromLastNameDTO(lastName, address, age, email, medication, allergies);
	}
	
	public ChildDataFromAddressDTO toDTO(Person person, List<Person> otherMembers) {
		String firstName = person.getFirstName();
		String lastName = person.getLastName();
		MedicalRecord record = person.getRecord();
		
		// Get Age from Birthdate
		String age = String.valueOf(SNAUtil.getAge(record.getBirthdate()));
		
		return new ChildDataFromAddressDTO(firstName, lastName, age, otherMembers);
	}
}
