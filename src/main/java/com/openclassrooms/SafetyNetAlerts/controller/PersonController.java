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

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
		try {
			log.info("'GET/person' endpoint requested.");
			return new ResponseEntity<>(service.getPersons(), HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.toString());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/person")
	public ResponseEntity<HttpStatus> addPerson(@RequestBody Person person) {
		try {
			log.info("'POST/person' endpoint requested.");
			service.addPerson(person);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.toString());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/person")
	public ResponseEntity<HttpStatus> modifyPerson(@RequestBody Person person) {
		try {
			log.info("'PUT/person' endpoint requested.");
			service.modifyPerson(person);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.toString());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/person")
	public ResponseEntity<HttpStatus> deletePerson(@RequestParam String firstName, @RequestParam String lastName) {
		try {
			log.info("'DELETE/person' endpoint requested.");
			service.deletePerson(firstName, lastName);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.toString());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/childAlert")
	public ResponseEntity<List<ChildDataFromAddressDTO>> getChildrenFromAddress(@RequestParam String address) {
		try {
			log.info("'/childAlert' endpoint requested.");
			return new ResponseEntity<>(service.getChildrenFromAddress(address), HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.toString());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/phoneAlert")
	public ResponseEntity<List<String>> getPhonesFromStationNumber(@RequestParam String firestation) {
		try {
			log.info("'/phoneAlert' endpoint requested.");
			return new ResponseEntity<>(service.getPhonesFromStationNumber(firestation), HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.toString());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/personInfo")
	public ResponseEntity<List<PersonDataFromLastNameDTO>> getPersonInfo(@RequestParam String lastName) {
		try {
			log.info("'/personInfo' endpoint requested.");
			return new ResponseEntity<>(service.getPersonInfo(lastName), HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.toString());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@GetMapping("/communityEmail")
	public ResponseEntity<List<String>> getCommunityEmail(@RequestParam String city) {
		try {
			log.info("'/communityEmail' endpoint requested.");
			return new ResponseEntity<>(service.getPersonEmailsFromCity(city), HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.toString());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
