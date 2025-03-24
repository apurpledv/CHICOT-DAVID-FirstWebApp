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
import com.openclassrooms.SafetyNetAlerts.util.FireStationAlreadyExistsException;

import lombok.extern.slf4j.Slf4j;

/**
 * FireStationRepository is the entity that will manipulate all the data related to Fire Stations
 */
@Slf4j
@Component
public class FireStationRepository {
	private String FileName = "src/main/resources/data.json";
	private JsonNode FullJSONData;

	private ObjectMapper mapper = new ObjectMapper();
	
	private List<FireStation> FireStationList = new ArrayList<FireStation>();
	
	/**
	 * Will initiate the data parsing upon being constructed
	 */
	public FireStationRepository() {
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
 	 * <p>Will parse a given file to find every Fire Station (in the 'firestations' array of the file)
 	 * and map it to a List of FireStation Objects</p>
 	 * @param fileName the path to the JSON file
 	 */
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
	
	/**
 	 * <p>Will parse a given file (by default: FileName = "src/main/resources/data.json") to find every Fire Station
 	 * and map it to a List of FireStation Objects</p>
 	 */
	public void initRepo() {
 		initRepo(this.FileName);
 	}
	
	/**
 	 * <p>Will loop the List of FireStations to find a specific one</p>
 	 * @param address of the station
 	 * @param stationNumber number of the station
 	 * @return a FireStation Object if found; null if none is found
 	 */
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
	
	/**
	 * <p>Will return the List of FireStation Objects</p>
	 * @return a List of FireStation Objects
	 */
	public List<FireStation> getFireStations() {
		return FireStationList;
	}
	
	/**
	 * <p>Will add a FireStation Object to the FireStationList List</p>
	 * @param stationToAdd a FireStation Object to be added
	 * @return a boolean of the method List.add()
	 * @throws Exception if a station with the same address and station number (check FireStation.equals(FireStation) method), 
	 * will throw an Exception
	 */
	public boolean addFireStation(FireStation stationToAdd) throws Exception {
		for (FireStation station : FireStationList) {
		    if (stationToAdd.equals(station)) 
		    	throw new FireStationAlreadyExistsException();
		}
		return FireStationList.add(stationToAdd);
	}
	
	/**
	 * <p>Will loop through FireStationList, identify the FireStation using its address, and modify its attribute (station)</p>
	 * @param stationToChange a FireStation Object containing the new attribute
	 * @return true if the FireStation is found and successfully modified; false otherwise
	 */
	public boolean modifyFireStation(FireStation stationToChange) {
		for (FireStation station : FireStationList) {
		    if (stationToChange.equals(station)) {
		    	station.setStation(stationToChange.getStation());
				return true;
		    }
		}
		return false;
	}
	
	/**
	 * <p>Will delete a given FireStation from FireStationList</p>
	 * @param address of the FireStation to delete
	 * @param stationNumber of the FireStation to delete
	 * @return true if the FireStation is found and successfully deleted; false otherwise
	 */
	public boolean deleteFireStation(String address, String stationNumber) {
		for (FireStation station : FireStationList) {
			if (station.getAddress().equals(address) && station.getStation().equals(stationNumber)) {
				FireStationList.remove(station);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * <p>Will return the number of the station found at a specific address</p>
	 * @param address of the station
	 * @return the station's number if found; '-1' otherwise
	 */
	public String getFireStationNumberFromAddress(String address) {
		String stationNumber = "-1";
		for (FireStation station : FireStationList) {
			if (station.getAddress().equals(address)) {
				stationNumber = station.getStation();
			}
		}
		return stationNumber; 
	}
	
	/**
	 * <p>Will return a List of addresses of every station identified by their station number</p>
	 * @param stationNumber of the stations
	 * @return a List of their addresses
	 */
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
