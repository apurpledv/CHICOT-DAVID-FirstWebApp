package com.openclassrooms.SafetyNetAlerts.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.model.HouseholdFromStationsDTO;
import com.openclassrooms.SafetyNetAlerts.model.PersonDataFromAddressDTO;
import com.openclassrooms.SafetyNetAlerts.model.PersonsDataFromStationDTO;
import com.openclassrooms.SafetyNetAlerts.service.FireStationService;
import com.openclassrooms.SafetyNetAlerts.util.FireStationAlreadyExistsException;

import lombok.extern.slf4j.Slf4j;

/**
 * FireStationController is an entity that handles every URL/Endpoint linked to Fire Stations (adding, modifying, deleting, ...)
 */
@Slf4j
@Controller
public class FireStationController {
	@Autowired
	FireStationService service;
	
	/**
	 * <p>Will attempt to add a FireStation into the Application, and return an HTTP Status signifying its success or failure</p>
	 * @param station the FireStation Object to add
	 * @return an HTTP Response with: Status 200 if everything went well; Status 400 if the station already exists within the Application or isn't valid (not every field is filled); Status 500 if another problem arose; 
	 */
	@PostMapping("/firestation")
	public ResponseEntity<HttpStatus> addFireStation(@RequestBody FireStation station) {
		ResponseEntity<HttpStatus> Response = new ResponseEntity<>(HttpStatus.OK);
		
		try {
			if (!station.IsValid()) {
				Response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				log.error("[POST] /firestation - " + String.valueOf(Response.getStatusCode()));
			} else {
				boolean Result = service.addFireStation(station);
				if (Result == false)
					throw new Exception("Could not add fire station");
				
				log.info("[POST] /firestation - " + String.valueOf(Response.getStatusCode()));
			}
		} catch (FireStationAlreadyExistsException e) {
			Response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			log.error("[POST] /firestation - " + String.valueOf(Response.getStatusCode()) + " ("+ e.toString() + ")");
		} catch (Exception e) {
			Response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[POST] /firestation - " + String.valueOf(Response.getStatusCode()) + " ("+ e.toString() + ")");
		}
		
		return Response;
	}
	
	/**
	 * <p>Will attempt to modify the attribute (station) of a FireStation registered in the Application</p>
	 * @param station the FireStation Object to modify
	 * @return an HTTP Response with: Status 200 if everything went well; Status 400 if the station isn't valid (not every field is filled); Status 500 if another problem arose;
	 */
	@PutMapping("/firestation")
	public ResponseEntity<HttpStatus> modifyFireStation(@RequestBody FireStation station) {
		ResponseEntity<HttpStatus> Response = new ResponseEntity<>(HttpStatus.OK);

		try {
			if (!station.IsValid()) {
				Response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				log.error("[PUT] /firestation - " + String.valueOf(Response.getStatusCode()));
			} else {
				boolean Result = service.modifyFireStation(station);
				if (Result == false)
					throw new Exception("Could not modify fire station");
				
				log.info("[PUT] /firestation - " + String.valueOf(Response.getStatusCode()));
			}
		} catch (Exception e) {
			Response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[PUT] /firestation - " + String.valueOf(Response.getStatusCode()) + " ("+ e.toString() + ")");
		}
		
		return Response;
	}
	
	/**
	 * <p>Will attempt to delete a station registered in the Application</p>
	 * @param address of the FireStation to delete
	 * @param station id number of the FireStation to delete
	 * @return an HTTP Response with: Status 200 if everything went well; Status 500 if an error occurred during the deletion process;
	 */
	@DeleteMapping("/firestation")
	public ResponseEntity<HttpStatus> deleteFireStation(@RequestParam String address, @RequestParam String station) {
		ResponseEntity<HttpStatus> Response = new ResponseEntity<>(HttpStatus.OK);
		
		try {
			boolean Result = service.deleteFireStation(address, station);
			if (Result == false)
				throw new Exception("Could not modify fire station");
				
			log.info("[DELETE] /firestation - " + String.valueOf(Response.getStatusCode()));
		} catch (Exception e) {
			Response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[DELETE] /firestation - " + String.valueOf(Response.getStatusCode()) + " ("+ e.toString() + ")");
		}
		
		return Response;
	}
	
	/**
	 * <p>Will retrieve information about the people covered by a specific station (id'd by its station number)</p>
	 * @param stationNumber of the station to parse from
	 * @return an HTTP Response containing a DTO about the people covered by the station (how many adults, children, and who they are exactly) if successful; an empty HTTP Response with status 500 if at any point, a medical record is not found
	 */
	@GetMapping("/firestation")
	public ResponseEntity<PersonsDataFromStationDTO> getPersonDTOFromStationNumber(@RequestParam String stationNumber) {
		ResponseEntity<PersonsDataFromStationDTO> Response = null;
		
		try {
			Response = new ResponseEntity<>(service.getPersonDTOFromStationNumber(stationNumber), HttpStatus.OK);
			log.info("[GET] /firestation - " + String.valueOf(Response.getStatusCode()));
		} catch (Exception e) {
			Response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[GET] /firestation - " + String.valueOf(Response.getStatusCode()) + " ("+ e.toString() + ")");
		}
		
		return Response;
	}
	
	/**
	 * <p>Will retrieve information about the people living at a certain address (ie: which station covers them, their medical records, their age...)</p>
	 * @param address to parse from
	 * @return an HTTP Response containing a List of DTOs, each containing [lastName, phone, age, stationNumber, medication, allergies] with status 200 if successful; an empty HTTP Response with status 500 if an error occurred (ie: a Person doesn't have a bound MedicalRecord)
	 */
	@GetMapping("/fire")
	public ResponseEntity<List<PersonDataFromAddressDTO>> getPersonDTOFromAddress(@RequestParam String address) {
		ResponseEntity<List<PersonDataFromAddressDTO>> Response = null;
		
		try {
			Response = new ResponseEntity<>(service.getPersonDTOFromAddress(address), HttpStatus.OK);
			log.info("[GET] /fire - " + String.valueOf(Response.getStatusCode()));
		} catch (Exception e) {
			Response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[GET] /fire - " + String.valueOf(Response.getStatusCode()) + " ("+ e.toString() + ")");
		}
		
		return Response;
	}
	
	/**
	 * <p>Will retrieve information about a list of household (ie: people living at one address), who are covered by different stations</p>
	 * @param stations a List of station numbers to parse from
	 * @return an HTTP Response containing a List of DTOs, each containing [address, occupants] with status 200 if successful; an empty HTTP Response with status 500 if an error occurred
	 */
	@GetMapping("/flood/stations")
	public ResponseEntity<List<HouseholdFromStationsDTO>> getHouseholdDTOFromStations(@RequestParam List<String> stations) {
		ResponseEntity<List<HouseholdFromStationsDTO>> Response = null;
		
		try {
			Response = new ResponseEntity<>(service.getHouseholdDTOFromStations(stations), HttpStatus.OK);
			log.info("[GET] /flood/stations - " + String.valueOf(Response.getStatusCode()));
		} catch (Exception e) {
			Response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[GET] /flood/stations - " + String.valueOf(Response.getStatusCode()) + " ("+ e.toString() + ")");
		}
		
		return Response;
	}
}
