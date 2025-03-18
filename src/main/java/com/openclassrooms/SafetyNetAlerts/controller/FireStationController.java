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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class FireStationController {
	@Autowired
	FireStationService service;
	
	@PostMapping("/firestation")
	public ResponseEntity<HttpStatus> addFireStation(@RequestBody FireStation station) {
		try {
			log.info("'POST/firestation' endpoint requested.");
			service.addFireStation(station);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.toString());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/firestation")
	public ResponseEntity<HttpStatus> modifyFireStation(@RequestBody FireStation station) {
		try {
			log.info("'PUT/firestation' endpoint requested.");
			service.modifyFireStation(station);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.toString());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/firestation")
	public ResponseEntity<HttpStatus> deleteFireStation(@RequestParam String address, @RequestParam String station) {
		try {
			log.info("'DELETE/firestation' endpoint requested.");
			service.deleteFireStation(address, station);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.toString());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/firestation")
	public ResponseEntity<PersonsDataFromStationDTO> getPersonDTOFromStationNumber(@RequestParam String stationNumber) {
		try {
			log.info("'/firestation' endpoint requested.");
			return new ResponseEntity<>(service.getPersonDTOFromStationNumber(stationNumber), HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.toString());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/fire")
	public ResponseEntity<List<PersonDataFromAddressDTO>> getPersonDTOFromAddress(@RequestParam String address) {
		try {
			log.info("'/fire' endpoint requested.");
			return new ResponseEntity<>(service.getPersonDTOFromAddress(address), HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.toString());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/flood/stations")
	public ResponseEntity<List<HouseholdFromStationsDTO>> getHouseholdDTOFromStations(@RequestParam List<String> stations) {
		try {
			log.info("'/flood/stations' endpoint requested.");
			return new ResponseEntity<>(service.getHouseholdDTOFromStations(stations), HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.toString());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
