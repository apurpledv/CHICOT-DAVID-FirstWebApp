package com.openclassrooms.SafetyNetAlerts.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.SafetyNetAlerts.repository.SafetyNetAlertsRepository;
import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.model.Person;

import lombok.Data;

@Data
@Service
public class SafetyNetAlertsService {
	@Autowired
	private SafetyNetAlertsRepository repo;
	
	public void initRepository() {
		repo.initRepo();
	}

	public List<Person> getPersons() {
		return repo.getPersonsList();
	}
	
	public Person getPerson(String firstName, String lastName) {
		return repo.getPerson(firstName, lastName);
	}
	
	public String getPersonsAsString() {
		return repo.getPersonsAsString();
	}
	
	public String getPersonsAsJSONString() {
		return repo.getPersonsAsJSONString();
	}
	
	public void addPerson(Person person) {
		repo.addPersonIntoList(person);
	}
	
	public void modifyPerson(Person person) {
		repo.modifyPersonInList(person);
	}
	
	public void deletePerson(String firstName, String lastName) {
		repo.deletePersonFromList(firstName, lastName);
	}

	public Person getFireStation(String firstName, String lastName) {
		return repo.getPerson(firstName, lastName);
	}
	
	/*public String getFireStationsAsJSONString() {
		return repo.getPersonsAsJSONString();
	}
	
	public void addFireStation(FireStation station) {
		repo.addFireStationIntoList(station);
	}
	
	public void modifyPerson(FireStation station) {
		repo.modifyFireStationInList(station);
	}
	
	public void deleteFireStation(FireStation station) {
		repo.deleteFireStationFromList(station);
	}*/
	
}

