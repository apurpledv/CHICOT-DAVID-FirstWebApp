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
	
	public void initRepo(String fileName) {
		PersonRepo.initRepo(fileName);
	}
	
	public List<Person> getPersons() throws Exception {
		return PersonRepo.getPersons();
	}
	
	public Person getPerson(String firstName, String lastName) {
		return PersonRepo.getPerson(firstName, lastName);
	}
	
	public boolean addPerson(Person person) throws Exception {
		return PersonRepo.addPerson(person);
	}
	
	public boolean modifyPerson(Person person) {
		return PersonRepo.modifyPerson(person);
	}
	
	public boolean deletePerson(String firstName, String lastName) {
		return PersonRepo.deletePerson(firstName, lastName);
	}
	
	public List<String> getPersonEmailsFromCity(String cityName) throws Exception {
		return PersonRepo.getPersonEmailsFromCity(cityName);
	}
	
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

