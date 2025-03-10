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

import com.openclassrooms.SafetyNetAlerts.model.ChildDataFromAddressDTO;
import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.model.PersonDataFromLastNameDTO;
import com.openclassrooms.SafetyNetAlerts.service.PersonService;

@Controller
public class PersonController {
	@Autowired
	PersonService service;
	
	@GetMapping("/")
    public void home() {
		// code here
    }

	@GetMapping("/person")
	public ResponseEntity<List<Person>> getPersons() {
		return new ResponseEntity<>(service.getPersons(), HttpStatus.OK);
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
	public ResponseEntity<HttpStatus> deletePerson(@RequestParam String firstName, @RequestParam String lastName) {
		service.deletePerson(firstName, lastName);
		return ResponseEntity.ok(HttpStatus.OK);
	}
	
	@GetMapping("/childAlert")
	public ResponseEntity<List<ChildDataFromAddressDTO>> getChildrenFromAddress(@RequestParam String address) {
		return new ResponseEntity<>(service.getChildrenFromAddress(address), HttpStatus.OK);
	}
	
	@GetMapping("/phoneAlert")
	public ResponseEntity<List<String>> getPhonesFromStationNumber(@RequestParam String firestation) {
		return new ResponseEntity<>(service.getPhonesFromStationNumber(firestation), HttpStatus.OK);
	}
	
	@GetMapping("/personInfo")
	public ResponseEntity<List<PersonDataFromLastNameDTO>> getPersonInfo(@RequestParam String lastName) {
		return new ResponseEntity<>(service.getPersonInfo(lastName), HttpStatus.OK);
	}
	
	@GetMapping("/communityEmail")
	public ResponseEntity<List<String>> getCommunityEmail(@RequestParam String city) {
		return new ResponseEntity<>(service.getPersonEmailsFromCity(city), HttpStatus.OK);
	}
}
