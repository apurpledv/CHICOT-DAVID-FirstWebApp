package com.openclassrooms.SafetyNetAlerts.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.SafetyNetAlerts.repository.FireStationRepository;
import com.openclassrooms.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.openclassrooms.SafetyNetAlerts.repository.PersonRepository;
import com.openclassrooms.SafetyNetAlerts.util.MedicalRecordNotFoundException;
import com.openclassrooms.SafetyNetAlerts.util.SNAUtil;
import com.openclassrooms.SafetyNetAlerts.model.ChildDataFromAddressDTO;
import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.model.Mapper;
import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.model.PersonDataFromLastNameDTO;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * MedicalRecordService is an entity that handles the business work linked to Persons
 */
@Slf4j
@Data
@Service
public class PersonService {
	@Autowired
	private PersonRepository PersonRepo;
	
	@Autowired
	private FireStationRepository FireRepo;
	
	@Autowired
	private MedicalRecordRepository MedicalRepo;
	
	@Autowired
	Mapper DTOmapper;
	
	/**
	 * Will manually order the loading of data from the default file from its repository
	 */
	public void initRepo() {
		PersonRepo.initRepo();
	}
	
	/**
	 * Will manually order the loading of data from a custom file from its repository
	 */
	public void initRepo(String fileName) {
		PersonRepo.initRepo(fileName);
	}
	
	/**
	 * <p>Will return a List of every Person Object registered</p>
	 * @return a List of Person Objects
	 */
	public List<Person> getPersons() throws Exception {
		return PersonRepo.getPersons();
	}
	
	/**
	 * <p>Will return a Person Object registered with that first and last name</p>
	 * @param firstName of the person's MedicalRecord to find
	 * @param lastName of the person's MedicalRecord to find
	 * @return a Person Object
	 */
	public Person getPerson(String firstName, String lastName) {
		return PersonRepo.getPerson(firstName, lastName);
	}
	
	/**
	 * Will attempt to add a Person Object to the Application
	 * @param person the Person Object to add
	 * @return true if successful; false otherwise
	 * @throws Exception if a person with the same first and last names already exists, will throw an Exception
	 */
	public boolean addPerson(Person person) throws Exception {
		return PersonRepo.addPerson(person);
	}
	
	/**
	 * <p>Will attempt to modify an already existing Person Object</p>
	 * @param person a Person Object containing the new attributes
	 * @return true if successful; false no person is found
	 */
	public boolean modifyPerson(Person person) {
		return PersonRepo.modifyPerson(person);
	}
	
	/**
	 * <p>Will attempt to delete an already existing Person Object</p>
	 * @param firstName of the Person to delete
	 * @param lastName of the Person to delete
	 * @return true if successful; false no person is found
	 */
	public boolean deletePerson(String firstName, String lastName) {
		return PersonRepo.deletePerson(firstName, lastName);
	}
	
	/**
	 * <p>Will return a List of email addresses of Persons from a given City</p>
	 * @param cityName city to parse from
	 * @return a List of email addresses
	 */
	public List<String> getPersonEmailsFromCity(String cityName) throws Exception {
		return PersonRepo.getPersonEmailsFromCity(cityName);
	}
	
	/**
	 * <p>Will return a List of the phone numbers of people covered by a given station (id'd by its station number)</p>
	 * @param stationNumber number of the station
	 * @return a List of phone numbers
	 */
	public List<String> getPhonesFromStationNumber(String stationNumber) throws Exception {
		List<String> PhonesList = new ArrayList<String>();
		List<FireStation> FireStationList = FireRepo.getFireStations();
		
		for (FireStation station : FireStationList) {
		    if (!station.getStation().equals(stationNumber))
		    	continue;
		    
		    List<Person> PersonsAtAddressList = PersonRepo.getPersonsFromAddress(station.getAddress());
		    for (Person person : PersonsAtAddressList)
		    	PhonesList.add(person.getPhone());
		}

		log.debug("Number of results: " + PhonesList.size());
		
		return PhonesList;
	}
	
	/**
	 * <p>Will return a List of Person DTOs with information related to Person(s) with a specific last name</p>
	 * @param lastName of the Person(s) to look for
	 * @return a List of DTOs, each containing [lastName, address, age, email, medication, allergies]
	 * @throws Exception if at any point, a Person doesn't have a MedicalRecord bound to them (==null), will throw an Exception
	 */
	public List<PersonDataFromLastNameDTO> getPersonInfo(String lastName) throws Exception {
		List<PersonDataFromLastNameDTO> ListDTO = new ArrayList<PersonDataFromLastNameDTO>();
		
		List<Person> personsList = PersonRepo.getPersons();
		
		for (Person person : personsList) {
		    if (person.getLastName().equals(lastName)) {
		    	ListDTO.add(DTOmapper.toPersonDataFromLastNameDto(person));
		    }
		}
		
		log.debug("Number of results: " + ListDTO.size());
		
		return ListDTO;
	}
	
	/**
	 * <p>Will return a List of Person DTOs of every Child at a given address
	 * @param address to parse persons from
	 * @return a List of DTOs, each containing @return a DTO containing [firstName, lastName, age, otherMembers]
	 * @throws Exception if at any point, a Person doesn't have a MedicalRecord bound to them (==null), will throw an Exception
	 */
	public List<ChildDataFromAddressDTO> getChildrenFromAddress(String address) throws Exception {
		List<ChildDataFromAddressDTO> ListDTO = new ArrayList<ChildDataFromAddressDTO>();
		
		List<Person> personsList = PersonRepo.getPersons();
		for (Person person : personsList) {
			MedicalRecord Record = person.getRecord();
			if (Record == null)
				throw new MedicalRecordNotFoundException();
			
			if (SNAUtil.getAge(Record.getBirthdate()) > SNAUtil.MAJORITY_AGE)
				continue;
			
		    if (!person.getAddress().equals(address))
		    	continue;
		    
		    List<Person> otherMembers = PersonRepo.getPersonsFromAddress(address);
		    
		    // We make sure we don't have that person in the "Other Members" list
		    for (Person otherMember : otherMembers) {
		    	if (person.equals(otherMember)) {
		    		otherMembers.remove(otherMember);
		    		break;
		    	}
		    }
		    
		    ListDTO.add(DTOmapper.toChildDataFromAddressDto(person, otherMembers));
		}

		log.debug("Number of results: " + ListDTO.size());
		
		return ListDTO;
	}
}

