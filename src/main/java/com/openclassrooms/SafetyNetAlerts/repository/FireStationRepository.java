package com.openclassrooms.SafetyNetAlerts.repository;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.model.Mapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FireStationRepository {
	@Autowired
	Mapper DTOmapper;
	
	private String fileName = "src/main/resources/data.json";
	private JsonNode FullJSONData;

	private ObjectMapper mapper = new ObjectMapper();
	
	private List<FireStation> FireStationList = new ArrayList<FireStation>();
	
	public FireStationRepository() {
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
			FireStationList = mapper.readValue(mapper.writeValueAsString(FullJSONData.path("firestations")), new TypeReference<List<FireStation>>(){});
		} catch (JsonProcessingException e) {
			log.error("Cannot load JSON data.");
		}
		log.info("Repository loaded successfully.");
	}
	
	public List<FireStation> getFireStations() {
		return FireStationList;
	}
	
	public void addFireStation(FireStation station) {
		FireStationList.add(station);
	}
	
	public void modifyFireStation(FireStation stationToChange) {
		for (FireStation station : FireStationList) {
		    if (stationToChange.getAddress().equals(station.getAddress())) {
		    	station.setStation(stationToChange.getStation());
		    	break;
		    }
		}
	}
	
	public void deleteFireStation(FireStation stationToDelete) {
		for (FireStation station : FireStationList) {
			if (stationToDelete.getAddress().equals(station.getAddress()) && stationToDelete.getStation().equals(station.getStation())) {
				FireStationList.remove(station);
				break;
			}
		}
	}
}
