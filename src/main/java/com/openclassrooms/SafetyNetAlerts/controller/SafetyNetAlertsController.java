package com.openclassrooms.SafetyNetAlerts.controller;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.service.SafetyNetAlertsService;

@Controller
public class SafetyNetAlertsController {
	@Autowired
	SafetyNetAlertsService service;
	
	@GetMapping("/")
    public void home() {
		// code here
    }
	
	// MAPPING OF: '/person'
	
	@GetMapping("/person")
	public ResponseEntity<String> getPersons() {
		return new ResponseEntity<>(service.getPersonsAsJSONString(), HttpStatus.OK);
	}
	
	@PostMapping("/person")
	public ResponseEntity<String> addPerson(@RequestBody Person person) {
		System.out.println("Adding '" + person.getFirstName() + " " + person.getLastName() + "' into the list.");

		service.addPerson(person);
		
		return new ResponseEntity<>("'" + person.getFirstName() + " " + person.getLastName() + "' was successfully added.", HttpStatus.OK);
	}
	
	@PutMapping("/person")
	public ResponseEntity<String> modifyPerson(@RequestBody Person person) {
		service.modifyPerson(person);
		
		String ResponseText = "'" + person.getFirstName() + " " + person.getLastName() + "' was successfully modified.";
		return new ResponseEntity<>(ResponseText, HttpStatus.OK);
	}
	
	@DeleteMapping("/person")
	public ResponseEntity<HttpStatus> deletePerson(@RequestBody Person person) {
		System.out.println("Deleting '" + person.getFirstName() + " " + person.getLastName() + "'.");

		service.deletePerson(person.getFirstName(), person.getLastName());
		
		return ResponseEntity.ok(HttpStatus.OK);
	}
	
	// MAPPING OF: '/firestation'
	
	@GetMapping("/firestation")
	public ResponseEntity<String> getFireStations() {
		return new ResponseEntity<>(service.getFireStationsAsJSONString(), HttpStatus.OK);
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
