package com.openclassrooms.SafetyNetAlerts.repository;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.model.Person;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PersonRepository {
	private String fileName = "src/main/resources/data.json";
	private JsonNode FullJSONData;

	private ObjectMapper mapper = new ObjectMapper();
	
	private List<Person> PersonsList = new ArrayList<Person>();
	
	public PersonRepository() {
		initRepo();
	}
	
 	public void getJSONFromFile() {
		// Get access to the file
		try {
			File dataFile = new File(fileName);
			
			// Parse it, and transfer into a String variable
			String JsonFromFile = FileUtils.readFileToString(dataFile, StandardCharsets.UTF_8);
			
			// Use ObjectMapper to get the JSON as exploitable data
			FullJSONData = mapper.readTree(JsonFromFile);
		} catch (Exception e) {
			log.error("Cannot open requested file.");
		}
	}
	
	private void initRepo() {
		// Get JSON from the file as exploitable data
		getJSONFromFile();
		
		// Use it to create Java Objects
		try {
			PersonsList = mapper.readValue(mapper.writeValueAsString(FullJSONData.path("persons")), new TypeReference<List<Person>>(){});
			
			// We assign each person their respective Medical Record (if found)
			List<MedicalRecord> RecordsListTemp = mapper.readValue(mapper.writeValueAsString(FullJSONData.path("medicalrecords")), new TypeReference<List<MedicalRecord>>(){});
			for (Person person : PersonsList) {
				for (MedicalRecord record : RecordsListTemp) {
				    if (person.getFirstName().equals(record.getFirstName()) && person.getLastName().equals(record.getLastName())) {
				    	person.setRecord(record);
				    	break;
				    }
				}
			}
		} catch (JsonProcessingException e) {
			log.error("Cannot load JSON data.");
		}
		log.info("Repository loaded successfully.");
	}
	
	public Person getPerson(String firstName, String lastName) {
		Person personFound = null;
		for (Person person : PersonsList) {
		    if (person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)) {
		    	personFound = person;
		    	break;
		    }
		}
		return personFound;
	}
	
	public List<Person> getPersons() {
		return PersonsList;
	}
	
	public void addPerson(Person person) {
		PersonsList.add(person);
	}
	
	public void modifyPerson(Person personToChange) {
		for (Person person : PersonsList) {
		    if (personToChange.getFirstName().equals(person.getFirstName()) && personToChange.getLastName().equals(person.getLastName())) {
		    	person.setAddress(personToChange.getAddress());
		    	person.setCity(personToChange.getCity());
		    	person.setZip(personToChange.getZip());
		    	person.setPhone(personToChange.getPhone());
		    	person.setEmail(personToChange.getEmail());
		    	break;
		    }
		}
	}
	
	public void deletePerson(String firstName, String lastName) {
		for (Person person : PersonsList) {
		    if (person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)) {
		    	PersonsList.remove(person);
		    	break;
		    }
		}
	}
	
	public List<String> getPersonEmailsFromCity(String cityName) {
		List<String> ListOfPersonsFromCity = new ArrayList<String>();
		for (Person person : PersonsList) {
		    if (person.getCity().equals(cityName))
		    	ListOfPersonsFromCity.add(person.getEmail());
		}
		return ListOfPersonsFromCity;
	}
	
	/*
	
	public String getPersonDataFromAddress(String address) {
		List<PersonDataFromAddressDTO> ListDTO = new ArrayList<PersonDataFromAddressDTO>();
		for (Person person : PersonsList) {
		    if (person.getAddress().equals(address)) {
		    	ListDTO.add(DTOmapper.toDTO(address, person, getMedicalRecord(person.getFirstName(), person.getLastName())));
		    }
		}
		
		String ListString = Arrays.toString(ListDTO.toArray());
		return ListString;
	}*/
}
