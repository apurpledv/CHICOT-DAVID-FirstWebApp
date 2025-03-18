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
import com.openclassrooms.SafetyNetAlerts.model.FireStation;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FireStationRepository {
	private String FileName = "src/main/resources/data.json";
	private JsonNode FullJSONData;

	private ObjectMapper mapper = new ObjectMapper();
	
	private List<FireStation> FireStationList = new ArrayList<FireStation>();
	
	public FireStationRepository() {
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
			FireStationList = mapper.readValue(mapper.writeValueAsString(FullJSONData.path("firestations")), new TypeReference<List<FireStation>>(){});
		} catch (JsonProcessingException e) {
			log.error(e.toString());
		}
	}
	
	public List<FireStation> getFireStations() {
		return FireStationList;
	}
	
	public FireStation getFireStation(String address, String stationNumber) {
		FireStation StationFound = null;
		for (FireStation station : FireStationList) {
		    if (station.getAddress().equals(address) && station.getStation().equals(stationNumber)) {
		    	StationFound = station;
		    	break;
		    }
		}
		
		return StationFound;
	}
	
	public void addFireStation(FireStation station) {
		FireStationList.add(station);
		
		log.debug("Station added successfully.");
	}
	
	public void modifyFireStation(FireStation stationToChange) {
		for (FireStation station : FireStationList) {
		    if (stationToChange.getAddress().equals(station.getAddress())) {
		    	station.setStation(stationToChange.getStation());
		    	break;
		    }
		}
		
		log.debug("Station modified successfully.");
	}
	
	public void deleteFireStation(String address, String stationNumber) {
		for (FireStation station : FireStationList) {
			if (station.getAddress().equals(address) && station.getStation().equals(stationNumber)) {
				FireStationList.remove(station);
				break;
			}
		}
		
		log.debug("Station deleted successfully.");
	}
	
	public String getFireStationNumberFromAddress(String address) {
		String stationNumber = "-1";
		for (FireStation station : FireStationList) {
			if (station.getAddress().equals(address)) {
				stationNumber = station.getStation();
			}
		}
		return stationNumber; 
	}
	
	public List<String> getFireStationAddressesFromStationNumber(String stationNumber) {
		List<String> ListOfStationAddresses = new ArrayList<String>();
		for (FireStation station : FireStationList) {
			if (station.getStation().equals(stationNumber)) {
				ListOfStationAddresses.add(station.getAddress());
			}
		}
		return ListOfStationAddresses; 
	}
}
