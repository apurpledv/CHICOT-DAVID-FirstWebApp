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

import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.service.FireStationService;

@Controller
public class FireStationController {
	@Autowired
	FireStationService service;
	
	@GetMapping("/firestation")
	public ResponseEntity<List<FireStation>> getFireStations() {
		return new ResponseEntity<>(service.getFireStations(), HttpStatus.OK);
	}
	
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
	public ResponseEntity<HttpStatus> deleteFireStation(@RequestBody FireStation station) {
		service.deleteFireStation(station);
		return ResponseEntity.ok(HttpStatus.OK);
	}
}
