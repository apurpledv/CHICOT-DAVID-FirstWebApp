package com.openclassrooms.SafetyNetAlerts.repository;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.model.Person;

@Component
public class SafetyNetAlertsRepository {
	private String fileName = "src/main/resources/data.json";
	private JsonNode FullJSONData;

	private ObjectMapper mapper = new ObjectMapper();
	
	private Boolean init = false;
	
	private List<Person> PersonsList = null;
	private List<FireStation> FireStationList = null;
	private List<MedicalRecord> RecordList = null;
	
	public SafetyNetAlertsRepository() {
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
			System.out.println("Error: Cannot open requested file.");
		}
	}
	
	public void initRepo() {
		if (init) return;
		
		// Get JSON from the file as exploitable data
		getJSONFromFile();
		
		// Use it to create Java Objects
		try {
			PersonsList = mapper.readValue(mapper.writeValueAsString(FullJSONData.path("persons")), new TypeReference<List<Person>>(){});
			FireStationList = mapper.readValue(mapper.writeValueAsString(FullJSONData.path("firestations")), new TypeReference<List<FireStation>>(){});
			RecordList = mapper.readValue(mapper.writeValueAsString(FullJSONData.path("medicalrecords")), new TypeReference<List<MedicalRecord>>(){});
		} catch (JsonProcessingException e) {
			System.out.println("Error: Cannot load JSON data.");
		}
		
		System.out.println("File loaded successfully.");
		
		init = true;
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
	
	public List<Person> getPersonsList() {
		return PersonsList;
	}
	
	public String getPersonsAsString() {
		String PersonsListString = "";
		for (Person person : PersonsList) {
			PersonsListString = PersonsListString
					.concat(person.getFirstName())
					.concat(" ")
					.concat(person.getLastName())
					.concat(" - ")
					.concat(person.getAddress())
					.concat("\n");
		}
		return PersonsListString;
	}
	
	public String getPersonsAsJSONString() {
		String ListString = Arrays.toString(PersonsList.toArray());
		return ListString;
	}
	
	public void addPersonIntoList(Person person) {
		PersonsList.add(person);
	}
	
	public void modifyPersonInList(Person personToChange) {
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
	
	public void deletePersonFromList(String firstName, String lastName) {
		for (Person person : PersonsList) {
		    if (person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)) {
		    	PersonsList.remove(person);
		    	break;
		    }
		}
	}
	
	public String getFireStationsAsJSONString() {
		String ListString = Arrays.toString(FireStationList.toArray());
		return ListString;
	}
	
	public void addFireStationIntoList(FireStation station) {
		FireStationList.add(station);
	}
	
	public void modifyFireStationInList(FireStation stationToChange) {
		for (FireStation station : FireStationList) {
		    if (stationToChange.getAddress().equals(station.getAddress())) {
		    	station.setStation(stationToChange.getStation());
		    	break;
		    }
		}
	}
	
	public void deleteFireStationFromList(FireStation stationToDelete) {
		for (FireStation station : FireStationList) {
			if (stationToDelete.getAddress().equals(station.getAddress()) && stationToDelete.getStation().equals(station.getStation())) {
				FireStationList.remove(station);
				break;
			}
		}
	}
	
	public String getMedicalRecordsAsJSONString() {
		String ListString = Arrays.toString(RecordList.toArray());
		return ListString;
	}
	
	public void addMedicalRecordIntoList(MedicalRecord record) {
		RecordList.add(record);
	}
	
	public void modifyMedicalRecordInList(MedicalRecord recordToChange) {
		for (MedicalRecord record : RecordList) {
		    if (recordToChange.getFirstName().equals(record.getFirstName()) && recordToChange.getLastName().equals(record.getLastName())) {
		    	record.setBirthdate(recordToChange.getBirthdate());
		    	record.setMedications(recordToChange.getMedications());
		    	record.setAllergies(recordToChange.getAllergies());
		    	break;
		    }
		}
	}
	
	public void deleteMedicalRecordFromList(MedicalRecord recordToDelete) {
		for (MedicalRecord record : RecordList) {
			if (recordToDelete.getFirstName().equals(record.getFirstName()) && recordToDelete.getLastName().equals(record.getLastName())) {
				RecordList.remove(record);
				break;
			}
		}
	}
}
