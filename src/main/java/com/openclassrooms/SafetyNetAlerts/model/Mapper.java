package com.openclassrooms.SafetyNetAlerts.model;

import java.util.List;

import org.springframework.stereotype.Component;

import com.openclassrooms.SafetyNetAlerts.util.MedicalRecordNotFoundException;
import com.openclassrooms.SafetyNetAlerts.util.SNAUtil;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Mapper is an entity that creates Data Transfer Objects (DTOs) that facilitate the way data is displayed to the user
 */
@Slf4j
@Component
@Data
public class Mapper {
	/**
	 * <p>Will return a DTO of a Person from a given address</p>
	 * @param person the Person Object from the address
	 * @param stationNumber the number of the station affiliated with this Person
	 * @return a DTO containing [lastName, phone, age, stationNumber, medication, allergies]
	 * @throws Exception if the Person's MedicalRecord doesn't exist (is null), will throw an Exception
	 */
	public PersonDataFromAddressDTO toPersonDataFromAddressDto(Person person, String stationNumber) throws Exception {
		MedicalRecord record = person.getRecord();
		if (record == null)
			throw new MedicalRecordNotFoundException();
		
		String lastName = person.getLastName();
		String phone = person.getPhone();
		int age = SNAUtil.getAge(record.getBirthdate());
		List<String> medication = record.getMedications();
		List<String> allergies = record.getAllergies();
		
		return new PersonDataFromAddressDTO(lastName, phone, age, stationNumber, medication, allergies); 
	}
	
	/**
	 * <p>Will return a DTO of a Person parsed using their last name</p>
	 * @param person the Person Object from the address
	 * @return a DTO containing [lastName, address, age, email, medication, allergies]
	 * @throws Exception if the Person's MedicalRecord doesn't exist (is null), will throw an Exception
	 */
	public PersonDataFromLastNameDTO toPersonDataFromLastNameDto(Person person) throws Exception {
		MedicalRecord record = person.getRecord();
		if (record == null)
			throw new MedicalRecordNotFoundException();
		
		String lastName = person.getLastName();
		String address = person.getAddress();
		String email = person.getEmail();
		int age = SNAUtil.getAge(record.getBirthdate());
		List<String> medication = record.getMedications();
		List<String> allergies = record.getAllergies();
		
		return new PersonDataFromLastNameDTO(lastName, address, age, email, medication, allergies);
	}
	
	/**
	 * <p>Will return a DTO of a Child and their relatives from a given address</p>
	 * @param person the Person Object of the child from the address
	 * @param otherMembers a List of Person Objects; the relatives
	 * @return a DTO containing [firstName, lastName, age, otherMembers]
	 * @throws Exception if the Person's MedicalRecord doesn't exist (is null), will throw an Exception
	 */
	public ChildDataFromAddressDTO toChildDataFromAddressDto(Person person, List<Person> otherMembers) throws Exception {
		MedicalRecord record = person.getRecord();
		if (record == null)
			throw new MedicalRecordNotFoundException();
		
		String firstName = person.getFirstName();
		String lastName = person.getLastName();
		int age = SNAUtil.getAge(record.getBirthdate());
		
		return new ChildDataFromAddressDTO(firstName, lastName, age, otherMembers);
	}
	
	/**
	 * <p>Will return a DTO of a Person living at a given address</p>
	 * @param person the Person Object from the address
	 * @return a DTO containing [lastName, phone, age, medication, allergies]
	 * @throws Exception if the Person's MedicalRecord doesn't exist (is null), will throw an Exception
	 */
	public PersonFromHouseholdDTO toPersonFromHouseholdDto(Person person) throws Exception {
		MedicalRecord record = person.getRecord();
		if (record == null)
			throw new MedicalRecordNotFoundException();
		
		String lastName = person.getLastName();
		String phone = person.getPhone();
		int age = SNAUtil.getAge(record.getBirthdate());
		List<String> medication = record.getMedications();
		List<String> allergies = record.getAllergies();
		
		return new PersonFromHouseholdDTO(lastName, phone, age, medication, allergies);
	}
	
	/**
	 * <p>Will return a DTO of a Household (an address, and the people living there)</p>
	 * @param address the address of the Household
	 * @param occupants a List of PersonFromHouseholdDTO Objects-the people living at this address 
	 * @return a DTO containing [address, occupants]
	 */
	public HouseholdFromStationsDTO toHouseholdFromStationsDto(String address, List<PersonFromHouseholdDTO> occupants) {
		return new HouseholdFromStationsDTO(address, occupants);
	}
	
	/**
	 * <p>Will return a DTO of a Person affiliated with a specific station</p>
	 * @param person the Person Object affiliated with the station 
	 * @return a DTO containing [firstName, lastName, address, phone]
	 */
	public PersonFromStationNumberDTO toPersonFromStationDto(Person person) {
		String firstName = person.getFirstName();
		String lastName = person.getLastName();
		String address = person.getAddress();
		String phone = person.getPhone();
		
		return new PersonFromStationNumberDTO(firstName, lastName, address, phone);
	}
	
	/**
	 * <p>Will return a DTO giving information about the people covered by a specific station (how many adults and children notably)</p>
	 * @param adults the number of adults covered by the station
	 * @param children the number of children covered by the station
	 * @return a DTO containing [adults, children, persons]
	 */
	public PersonsDataFromStationDTO toPersonsDataFromStationNumberDto(int adults, int children, List<PersonFromStationNumberDTO> persons) {
		return new PersonsDataFromStationDTO(adults, children, persons);
	}
}
