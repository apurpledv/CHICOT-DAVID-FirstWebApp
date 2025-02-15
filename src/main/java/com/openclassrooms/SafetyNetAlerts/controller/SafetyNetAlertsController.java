package com.openclassrooms.SafetyNetAlerts.controller;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.model.PersonDataFromAddressDTO;
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
	public ResponseEntity<HttpStatus> addPerson(@RequestBody Person person) {
		service.addPerson(person);
		return ResponseEntity.ok(HttpStatus.OK);
	}
	
	@PutMapping("/person")
	public ResponseEntity<HttpStatus> modifyPerson(@RequestBody Person person) {
		service.modifyPerson(person);
		return ResponseEntity.ok(HttpStatus.OK);
	}
	
	@DeleteMapping("/person")
	public ResponseEntity<HttpStatus> deletePerson(@RequestBody Person person) {
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

	// MAPPING OF: '/medicalRecord'
	
	@GetMapping("/medicalRecord")
	public ResponseEntity<String> getMedicalRecords() {
		return new ResponseEntity<>(service.getMedicalRecordsAsJSONString(), HttpStatus.OK);
	}
	
	@PostMapping("/medicalRecord")
	public ResponseEntity<HttpStatus> addMedicalRecord(@RequestBody MedicalRecord record) {
		service.addMedicalRecord(record);
		return ResponseEntity.ok(HttpStatus.OK);
	}
	
	@PutMapping("/medicalRecord")
	public ResponseEntity<HttpStatus> modifyMedicalRecord(@RequestBody MedicalRecord record) {
		service.modifyMedicalRecord(record);
		return ResponseEntity.ok(HttpStatus.OK);
	}
	
	@DeleteMapping("/medicalRecord")
	public ResponseEntity<HttpStatus> deleteMedicalRecord(@RequestBody MedicalRecord record) {
		service.deleteMedicalRecord(record);
		return ResponseEntity.ok(HttpStatus.OK);
	}
	
	// URLS
	@GetMapping("/fire")
	public ResponseEntity<Person> getPersonDataFromAddress(@RequestParam String address) {
		//return new ResponseEntity<>(service.getPersonDataFromAddress(address), HttpStatus.OK);
		return new ResponseEntity<>(new Person(), HttpStatus.OK);
	}
	
	@GetMapping("/communityEmail")
	public ResponseEntity<String> getCommunityEmail(@RequestParam String city) {
		return new ResponseEntity<>(service.getPersonsEmailFromCity(city), HttpStatus.OK);
	}
	
}
