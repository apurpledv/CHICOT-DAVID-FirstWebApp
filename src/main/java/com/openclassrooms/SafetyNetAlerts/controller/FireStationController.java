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

@Slf4j
@Controller
public class FireStationController {
	@Autowired
	FireStationService service;
	
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
