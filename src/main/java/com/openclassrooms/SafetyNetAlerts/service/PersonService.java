package com.openclassrooms.SafetyNetAlerts.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.SafetyNetAlerts.repository.FireStationRepository;
import com.openclassrooms.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.openclassrooms.SafetyNetAlerts.repository.PersonRepository;
import com.openclassrooms.SafetyNetAlerts.util.SNAUtil;
import com.openclassrooms.SafetyNetAlerts.model.ChildDataFromAddressDTO;
import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.model.Mapper;
import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.model.PersonDataFromLastNameDTO;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

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
	
	public void initRepo() {
		PersonRepo.initRepo();
	}
	
	public List<Person> getPersons() {
		return PersonRepo.getPersons();
	}
	
	public Person getPerson(String firstName, String lastName) {
		return PersonRepo.getPerson(firstName, lastName);
	}
	
	public void addPerson(Person person) {
		log.debug("Adding Person: " + person);
		PersonRepo.addPerson(person);
	}
	
	public void modifyPerson(Person person) {
		log.debug("Modifying Person: " + person);
		PersonRepo.modifyPerson(person);
	}
	
	public void deletePerson(String firstName, String lastName) {
		log.debug("Deleting " + firstName + " " + lastName);
		PersonRepo.deletePerson(firstName, lastName);
	}
	
	public List<String> getPersonEmailsFromCity(String cityName) {
		log.debug("Retrieving Emails from City: " + cityName);
		return PersonRepo.getPersonEmailsFromCity(cityName);
	}
	
	public List<String> getPhonesFromStationNumber(String stationNumber) {
		log.debug("Retrieving Phone Numbers from Station No." + stationNumber);
		List<String> PhonesList = new ArrayList<String>();
		List<FireStation> FireStationList = FireRepo.getFireStations();
		
		for (FireStation station : FireStationList) {
		    if (!station.getStation().equals(stationNumber))
		    	continue;
		    
		    List<Person> PersonsAtAddressList = PersonRepo.getPersonsFromAddress(station.getAddress());
		    for (Person person : PersonsAtAddressList)
		    	PhonesList.add(person.getPhone());
		}
		
		if (PhonesList.isEmpty())
			log.info("No Phone Number found for Station: " + stationNumber);
		
		return PhonesList;
	}
	
	public List<PersonDataFromLastNameDTO> getPersonInfo(String lastName) {
		log.debug("Retrieving Data Of People With Last Name: " + lastName);
		
		List<PersonDataFromLastNameDTO> ListDTO = new ArrayList<PersonDataFromLastNameDTO>();
		
		List<Person> personsList = PersonRepo.getPersons();
		
		for (Person person : personsList) {
		    if (person.getLastName().equals(lastName)) {
		    	ListDTO.add(DTOmapper.toPersonDataFromLastNameDto(person));
		    }
		}
		
		if (ListDTO.isEmpty())
			log.info("No Person found with last name: " + lastName);
		
		return ListDTO;
	}
	
	public List<ChildDataFromAddressDTO> getChildrenFromAddress(String address) {
		log.debug("Retrieving List Of Children Living At Address: " + address);
		
		List<ChildDataFromAddressDTO> ListDTO = new ArrayList<ChildDataFromAddressDTO>();
		
		List<Person> personsList = PersonRepo.getPersons();
		for (Person person : personsList) {
			// If there is no record, we cannot know if the person is a child or not, so we ignore
			if (person.getRecord() == null)
				continue;
			
			if (SNAUtil.getAge(person.getRecord().getBirthdate()) > SNAUtil.MAJORITY_AGE)
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
		
		if (ListDTO.isEmpty())
			log.info("No Children found at address: " + address);
		
		return ListDTO;
	}
}

