package com.openclassrooms.SafetyNetAlerts.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.SafetyNetAlerts.repository.PersonRepository;
import com.openclassrooms.SafetyNetAlerts.model.Person;

import lombok.Data;

@Data
@Service
public class PersonService {
	@Autowired
	private PersonRepository repo;
	
	public List<Person> getPersons() {
		return repo.getPersons();
	}
	
	public Person getPerson(String firstName, String lastName) {
		return repo.getPerson(firstName, lastName);
	}
	
	public void addPerson(Person person) {
		repo.addPerson(person);
	}
	
	public void modifyPerson(Person person) {
		repo.modifyPerson(person);
	}
	
	public void deletePerson(String firstName, String lastName) {
		repo.deletePerson(firstName, lastName);
	}
	
	public List<String> getPersonEmailsFromCity(String cityName) {
		return repo.getPersonEmailsFromCity(cityName);
	}
	
	/*
	
	public String getPersonDataFromAddress(String address) {
		return repo.getPersonDataFromAddress(address);
	}*/
	
}

