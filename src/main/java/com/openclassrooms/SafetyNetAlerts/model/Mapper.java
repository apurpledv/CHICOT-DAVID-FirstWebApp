package com.openclassrooms.SafetyNetAlerts.model;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class Mapper {
	public PersonDataFromAddressDTO toDTO(String address, Person person, MedicalRecord record, String stationNumber) {
		String lastName = person.getLastName();
		String phone = person.getPhone();
		
		// Get Age from Birthdate
		String age = String.valueOf(Period.between(LocalDate.parse(record.getBirthdate(), DateTimeFormatter.ofPattern("MM/dd/yyyy")), LocalDate.now()).getYears());
		
		List<String> medication = record.getMedications();
		List<String> allergies = record.getAllergies();
		
		return new PersonDataFromAddressDTO(lastName, phone, age, stationNumber, medication, allergies);
	}
}
