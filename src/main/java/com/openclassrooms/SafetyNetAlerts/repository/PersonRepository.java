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
import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.util.PersonAlreadyExistsException;

import lombok.extern.slf4j.Slf4j;

/**
 * PersonRepository is the entity that will manipulate all the data related to Persons
 */

@Slf4j
@Component
public class PersonRepository {
	private String FileName = "src/main/resources/data.json";
	private JsonNode FullJSONData;

	private ObjectMapper mapper = new ObjectMapper();
	
	private List<Person> PersonsList = new ArrayList<Person>();
	
	/**
	 * Will initiate the data parsing upon being constructed
	 */
	public PersonRepository() {
		initRepo();
	}
	
	/**
	 * <p>Will read an entire file and turn it into a String</p>
	 * @param fileName the path to the file
	 */
 	public void getJSONFromFile(String fileName) {
		// Get access to the file
		try {
			File dataFile = new File(fileName);
			
			// Parse it, and transfer into a String variable
			String JsonFromFile = FileUtils.readFileToString(dataFile, StandardCharsets.UTF_8);
			
			// Use ObjectMapper to get the JSON as exploitable data
			FullJSONData = mapper.readTree(JsonFromFile);
		} catch (Exception e) {
			log.error(e.toString());
		}
	}
	
 	/**
 	 * <p>Will parse a given file to find every Person (in the 'persons' array of the file)
 	 * and map it to a List of Persons</p>
 	 * @param fileName the path to the JSON file
 	 */
	public void initRepo(String fileName) {
		// Get JSON from the file as exploitable data
		getJSONFromFile(fileName);
		
		// Use it to create Java Objects
		try {
			PersonsList = mapper.readValue(mapper.writeValueAsString(FullJSONData.path("persons")), new TypeReference<List<Person>>(){});
		} catch (JsonProcessingException e) {
			log.error(e.toString());
		}
	}
	
	/**
 	 * <p>Will parse a given file (by default: FileName = "src/main/resources/data.json") to find every Person
 	 * and map it to a List of Person Object Objects</p>
 	 */
 	public void initRepo() {
 		initRepo(this.FileName);
 	}

 	/**
 	 * <p>Will loop the List of Persons to find a specific one</p>
 	 * @param firstName of the person
 	 * @param lastName of the person
 	 * @return a Person Object if found; null if none is found
 	 */
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
	
	/**
	 * <p>Will return the List of Person Objects</p>
	 * @return a List of Person Objects
	 */
	public List<Person> getPersons() {
		return PersonsList;
	}
	
	/**
	 * <p>Will add a Person Object to the PersonsList List</p>
	 * @param personToAdd a Person Object to be added
	 * @return a boolean of the method List.add()
	 * @throws Exception if a person with the same first and last names (check Person.equals(Person) method), 
	 * will throw an Exception
	 */
	public boolean addPerson(Person personToAdd) throws Exception {
		for (Person person : PersonsList) {
		    if (personToAdd.equals(person))
		    	throw new PersonAlreadyExistsException("This person already exists.");
		}
		
		return PersonsList.add(personToAdd);
	}
	
	/**
	 * <p>Will loop through PersonsList, identify the Person using firstName and lastName, and modify its attributes</p>
	 * @param personToChange a Person Object containing the new attributes
	 * @return true if the Person is found and successfully modified; false otherwise
	 */
	public boolean modifyPerson(Person personToChange) {
		for (Person person : PersonsList) {
		    if (personToChange.equals(person)) {
	    		person.setAddress(personToChange.getAddress());
	    		person.setCity(personToChange.getCity());
	    		person.setZip(personToChange.getZip());
	    		person.setPhone(personToChange.getPhone());
	    		person.setEmail(personToChange.getEmail());
	    		return true;
		    }
		}
		return false;
	}
	
	/**
	 * <p>Will delete a given Person from PersonsList</p>
	 * @param firstName of the Person to delete
	 * @param lastName of the Person to delete
	 * @return true if the Person is found and successfully deleted; false otherwise
	 */
	public boolean deletePerson(String firstName, String lastName) {
		for (Person person : PersonsList) {
		    if (person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)) {
		    	PersonsList.remove(person);
		    	
		    	return true;
		    }
		}
		return false;
	}
	
	/**
	 * <p>Will retrieve the email addresses of every Person associated with a given City</p>
	 * @param cityName name of the City to parse from
	 * @return a List of the email addresses found
	 */
	public List<String> getPersonEmailsFromCity(String cityName) {
		List<String> ListOfPersonsFromCity = new ArrayList<String>();
		for (Person person : PersonsList) {
		    if (person.getCity().equals(cityName))
		    	ListOfPersonsFromCity.add(person.getEmail());
		}
		return ListOfPersonsFromCity;
	}
	
	/**
	 * <p>Will retrieve the Persons living at a specific Address</p>
	 * @param address to parse from
	 * @return a List of the Persons found at this address
	 */
	public List<Person> getPersonsFromAddress(String address) {
		List<Person> ListOfPersonsFromAddress = new ArrayList<Person>();
		for (Person person : PersonsList) {
		    if (person.getAddress().equals(address))
		    	ListOfPersonsFromAddress.add(person);
		}
		return ListOfPersonsFromAddress;
	}
}
