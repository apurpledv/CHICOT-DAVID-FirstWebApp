package com.openclassrooms.SafetyNetAlerts.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.SafetyNetAlerts.repository.FireStationRepository;
import com.openclassrooms.SafetyNetAlerts.repository.PersonRepository;
import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.model.Mapper;
import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.model.PersonDataFromAddressDTO;
import com.openclassrooms.SafetyNetAlerts.model.PersonDataFromLastNameDTO;

import lombok.Data;

@Data
@Service
public class PersonService {
	@Autowired
	private PersonRepository PersonRepo;
	
	@Autowired
	private FireStationRepository FireRepo;
	
	@Autowired
	Mapper DTOmapper;
	
	public List<Person> getPersons() {
		return PersonRepo.getPersons();
	}
	
	public Person getPerson(String firstName, String lastName) {
		return PersonRepo.getPerson(firstName, lastName);
	}
	
	public void addPerson(Person person) {
		PersonRepo.addPerson(person);
	}
	
	public void modifyPerson(Person person) {
		PersonRepo.modifyPerson(person);
	}
	
	public void deletePerson(String firstName, String lastName) {
		PersonRepo.deletePerson(firstName, lastName);
	}
	
	public List<String> getPersonEmailsFromCity(String cityName) {
		return PersonRepo.getPersonEmailsFromCity(cityName);
	}
	
	public List<String> getPhonesFromStationNumber(String stationNumber) {
		List<String> PhonesList = new ArrayList<String>();
		List<FireStation> FireStationList = FireRepo.getFireStations();
		
		for (FireStation station : FireStationList) {
		    if (!station.getStation().equals(stationNumber))
		    	continue;
		    
		    List<Person> PersonsAtAddressList = PersonRepo.getPersonsFromAddress(station.getAddress());
		    for (Person person : PersonsAtAddressList)
		    	PhonesList.add(person.getPhone());
		}
		
		return PhonesList;
	}
	
	public List<PersonDataFromLastNameDTO> getPersonInfo(String lastName) {
		List<PersonDataFromLastNameDTO> ListDTO = new ArrayList<PersonDataFromLastNameDTO>();
		
		List<Person> personsList = PersonRepo.getPersons();
		
		for (Person person : personsList) {
		    if (person.getLastName().equals(lastName)) {
		    	ListDTO.add(DTOmapper.toDTO(person));
		    }
		}
		
		return ListDTO;
	}
	
}

