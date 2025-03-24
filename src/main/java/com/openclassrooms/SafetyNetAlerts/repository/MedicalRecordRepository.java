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
import com.openclassrooms.SafetyNetAlerts.util.MedicalRecordAlreadyExistsException;

import lombok.extern.slf4j.Slf4j;

/**
 * MedicalRecordRepository is the entity that will manipulate all the data related to Medical Records
 */
@Slf4j
@Component
public class MedicalRecordRepository {
	private String FileName = "src/main/resources/data.json";
	private JsonNode FullJSONData;

	private ObjectMapper mapper = new ObjectMapper();
	
	private List<MedicalRecord> RecordsList = new ArrayList<MedicalRecord>();
	
	/**
	 * Will initiate the data parsing upon being constructed
	 */
	public MedicalRecordRepository() {
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
 	 * <p>Will parse a given file to find every Medical Record (in the 'medicalrecords' array of the file)
 	 * and map it to a List of MedicalRecord Objects</p>
 	 * @param fileName the path to the JSON file
 	 */
	public void initRepo(String fileName) {
		// Get JSON from the file as exploitable data
		getJSONFromFile(fileName);
		
		// Use it to create Java Objects
		try {
			RecordsList = mapper.readValue(mapper.writeValueAsString(FullJSONData.path("medicalrecords")), new TypeReference<List<MedicalRecord>>(){});
		} catch (JsonProcessingException e) {
			log.error(e.toString());
		}
	}
	
	/**
 	 * <p>Will parse a given file (by default: FileName = "src/main/resources/data.json") to find every Medical Record
 	 * and map it to a List of MedicalRecord Objects</p>
 	 */
	public void initRepo() {
 		initRepo(this.FileName);
 	}
	
	/**
 	 * <p>Will loop the List of MedicalRecords to find a specific one</p>
 	 * @param firstName of the person associated with this record
 	 * @param lastName of the person associated with this record
 	 * @return a MedicalRecord Object if found; null if none is found
 	 */
	public MedicalRecord getMedicalRecord(String firstName, String lastName) {
		MedicalRecord recordFound = null;
		for (MedicalRecord record : RecordsList) {
		    if (record.getFirstName().equals(firstName) && record.getLastName().equals(lastName)) {
		    	recordFound = record;
		    	break;
		    }
		}
		return recordFound;
	}
	
	/**
	 * <p>Will return the List of MedicalRecord Objects</p>
	 * @return a List of MedicalRecord Objects
	 */
	public List<MedicalRecord> getMedicalRecords() {
		return RecordsList;
	}
	
	/**
	 * <p>Will add a MedicalRecord Object to the RecordsList List</p>
	 * @param recordToAdd a MedicalRecord Object to be added
	 * @return a boolean of the method List.add()
	 * @throws Exception if a record with the same first and last names (check MedicalRecord.equals(MedicalRecord) method), 
	 * will throw an Exception
	 */
	public boolean addMedicalRecord(MedicalRecord recordToAdd) throws Exception {
		for (MedicalRecord record : RecordsList) {
		    if (recordToAdd.equals(record))
		    	throw new MedicalRecordAlreadyExistsException();
		}
		return RecordsList.add(recordToAdd);
	}
	
	/**
	 * <p>Will loop through RecordsList, identify the MedicalRecord using firstName and lastName, and modify its attributes</p>
	 * @param recordToChange a MedicalRecord Object containing the new attributes
	 * @return true if the MedicalRecord is found and successfully modified; false otherwise
	 */
	public boolean modifyMedicalRecord(MedicalRecord recordToChange) {
		for (MedicalRecord record : RecordsList) {
		    if (recordToChange.equals(record)) {
		    	record.setBirthdate(recordToChange.getBirthdate());
		    	record.setMedications(recordToChange.getMedications());
		    	record.setAllergies(recordToChange.getAllergies());
		    	return true;
		    }
		}
		return false;
	}
	
	/**
	 * <p>Will delete a given MedicalRecord from RecordsList</p>
	 * @param recordFirstName of the person's MedicalRecord to delete
	 * @param recordLastName of the person's MedicalRecord to delete
	 * @return true if the MedicalRecord is found and successfully deleted; false otherwise
	 */
	public boolean deleteMedicalRecord(String recordFirstName, String recordLastName) {
		for (MedicalRecord record : RecordsList) {
			if (record.getFirstName().equals(recordFirstName) && record.getLastName().equals(recordLastName))
				return RecordsList.remove(record);
		}
		return false;
	}
	
}
