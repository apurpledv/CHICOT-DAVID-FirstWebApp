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

@Controller
public class FireStationController {
	@Autowired
	FireStationService service;
	
	@PostMapping("/firestation")
	public ResponseEntity<HttpStatus> addFireStation(@RequestBody FireStation station) {
		service.addFireStation(station);
		return ResponseEntity.ok(HttpStatus.OK);
	}
	
	@PutMapping("/firestation")
	public ResponseEntity<HttpStatus> modifyFireStation(@RequestBody FireStation station) {
		service.modifyFireStation(station);
		return ResponseEntity.ok(HttpStatus.OK);
	}
	
	@DeleteMapping("/firestation")
	public ResponseEntity<HttpStatus> deleteFireStation(@RequestParam String address, @RequestParam String station) {
		service.deleteFireStation(address, station);
		return ResponseEntity.ok(HttpStatus.OK);
	}
	
	@GetMapping("/firestation")
	public ResponseEntity<PersonsDataFromStationDTO> getPersonDTOFromStationNumber(@RequestParam String stationNumber) {
		return new ResponseEntity<>(service.getPersonDTOFromStationNumber(stationNumber), HttpStatus.OK);
	}
	
	@GetMapping("/fire")
	public ResponseEntity<List<PersonDataFromAddressDTO>> getPersonDTOFromAddress(@RequestParam String address) {
		return new ResponseEntity<>(service.getPersonDTOFromAddress(address), HttpStatus.OK);
	}
	
	@GetMapping("/flood/stations")
	public ResponseEntity<List<HouseholdFromStationsDTO>> getHouseholdDTOFromStations(@RequestParam List<String> stations) {
		return new ResponseEntity<>(service.getHouseholdDTOFromStations(stations), HttpStatus.OK);
	}
}
