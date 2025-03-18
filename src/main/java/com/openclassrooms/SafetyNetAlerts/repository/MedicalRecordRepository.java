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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MedicalRecordRepository {
	private String FileName = "src/main/resources/data.json";
	private JsonNode FullJSONData;

	private ObjectMapper mapper = new ObjectMapper();
	
	private List<MedicalRecord> RecordList = new ArrayList<MedicalRecord>();
	
	public MedicalRecordRepository() {
		initRepo();
	}
	
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
	
 	public void initRepo() {
 		initRepo(this.FileName);
 	}
	
	public void initRepo(String fileName) {
		// Get JSON from the file as exploitable data
		getJSONFromFile(fileName);
		
		// Use it to create Java Objects
		try {
			RecordList = mapper.readValue(mapper.writeValueAsString(FullJSONData.path("medicalrecords")), new TypeReference<List<MedicalRecord>>(){});
		} catch (JsonProcessingException e) {
			log.error(e.toString());
		}
	}
	
	public MedicalRecord getMedicalRecord(String firstName, String lastName) {
		MedicalRecord recordFound = null;
		for (MedicalRecord record : RecordList) {
		    if (record.getFirstName().equals(firstName) && record.getLastName().equals(lastName)) {
		    	recordFound = record;
		    	break;
		    }
		}
		return recordFound;
	}
	
	public List<MedicalRecord> getMedicalRecords() {
		return RecordList;
	}
	
	public void addMedicalRecord(MedicalRecord record) {
		RecordList.add(record);
	}
	
	public void modifyMedicalRecord(MedicalRecord recordToChange) {
		for (MedicalRecord record : RecordList) {
		    if (recordToChange.getFirstName().equals(record.getFirstName()) && recordToChange.getLastName().equals(record.getLastName())) {
		    	record.setBirthdate(recordToChange.getBirthdate());
		    	record.setMedications(recordToChange.getMedications());
		    	record.setAllergies(recordToChange.getAllergies());
		    	break;
		    }
		}
	}
	
	public void deleteMedicalRecord(String recordFirstName, String recordLastName) {
		for (MedicalRecord record : RecordList) {
			if (record.getFirstName().equals(recordFirstName) && record.getLastName().equals(recordLastName)) {
				RecordList.remove(record);
				break;
			}
		}
	}
	
}
