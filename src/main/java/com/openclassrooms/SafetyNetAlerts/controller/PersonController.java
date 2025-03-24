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
import com.openclassrooms.SafetyNetAlerts.util.PersonAlreadyExistsException;

import lombok.extern.slf4j.Slf4j;

/**
 * PersonController is an entity that handles every URL/Endpoint linked to Persons (adding, modifying, deleting, ...)
 */
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
		ResponseEntity<List<Person>> Response = null;
		
		try {
			Response = new ResponseEntity<>(service.getPersons(), HttpStatus.OK);
			log.info("[POST] /person - " + String.valueOf(Response.getStatusCode()));
		} catch (Exception e) {
			Response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[GET] /person - " + String.valueOf(Response.getStatusCode()) + " ("+ e.toString() + ")");
		}
		
		return Response;
	}
	
	@PostMapping("/person")
	public ResponseEntity<HttpStatus> addPerson(@RequestBody Person person) {
		ResponseEntity<HttpStatus> Response = new ResponseEntity<>(HttpStatus.OK);
		
		try {
			if (!person.IsValid()) {
				Response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				log.error("[POST] /person - " + String.valueOf(Response.getStatusCode()));
			} else {
				boolean Result = service.addPerson(person);
				if (Result == false)
					throw new Exception("Could not add person");
				
				log.info("[POST] /person - " + String.valueOf(Response.getStatusCode()));
			}
		} catch (PersonAlreadyExistsException e) {
			Response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			log.error("[POST] /person - " + String.valueOf(Response.getStatusCode()) + " ("+ e.toString() + ")");
		} catch (Exception e) {
			Response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[POST] /person - " + String.valueOf(Response.getStatusCode()) + " ("+ e.toString() + ")");
		}
		
		return Response;
	}
	
	@PutMapping("/person")
	public ResponseEntity<HttpStatus> modifyPerson(@RequestBody Person person) {
		ResponseEntity<HttpStatus> Response = new ResponseEntity<>(HttpStatus.OK);
		
		try {
			if (!person.IsValid()) {
				Response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				log.error("[PUT] /person - " + String.valueOf(Response.getStatusCode()));
			} else {
				boolean Result = service.modifyPerson(person);
				if (Result == false)
					throw new Exception("Could not modify person");
				
				log.info("[PUT] /person - " + String.valueOf(Response.getStatusCode()));
			}
		} catch (Exception e) {
			Response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[PUT] /person - " + String.valueOf(Response.getStatusCode()) + " ("+ e.toString() + ")");
		}
		
		return Response;
	}
	
	@DeleteMapping("/person")
	public ResponseEntity<HttpStatus> deletePerson(@RequestParam String firstName, @RequestParam String lastName) {
		ResponseEntity<HttpStatus> Response = new ResponseEntity<>(HttpStatus.OK);
		
		try {
			boolean Result = service.deletePerson(firstName, lastName);
			if (Result == false)
				throw new Exception("Could not delete person");
			
			log.info("[DELETE] /person - " + String.valueOf(Response.getStatusCode()));
		} catch (Exception e) {
			Response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[DELETE] /person - " + String.valueOf(Response.getStatusCode()) + " ("+ e.toString() + ")");
		}
		
		return Response;
	}
	
	@GetMapping("/childAlert")
	public ResponseEntity<List<ChildDataFromAddressDTO>> getChildrenFromAddress(@RequestParam String address) {
		ResponseEntity<List<ChildDataFromAddressDTO>> Response = null;
		
		try {
			Response = new ResponseEntity<>(service.getChildrenFromAddress(address), HttpStatus.OK);
			log.info("[GET] /childAlert - " + String.valueOf(Response.getStatusCode()));
		} catch (Exception e) {
			Response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[GET] /childAlert - " + String.valueOf(Response.getStatusCode()) + " ("+ e.toString() + ")");
		}
		
		return Response;
	}
	
	@GetMapping("/phoneAlert")
	public ResponseEntity<List<String>> getPhonesFromStationNumber(@RequestParam String firestation) {
		ResponseEntity<List<String>> Response = null;
		
		try {
			Response = new ResponseEntity<>(service.getPhonesFromStationNumber(firestation), HttpStatus.OK);
			log.info("[GET] /phoneAlert - " + String.valueOf(Response.getStatusCode()));
		} catch (Exception e) {
			Response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[GET] /phoneAlert - " + String.valueOf(Response.getStatusCode()) + " ("+ e.toString() + ")");
		}
		
		return Response;
	}
	
	@GetMapping("/personInfo")
	public ResponseEntity<List<PersonDataFromLastNameDTO>> getPersonInfo(@RequestParam String lastName) {
		ResponseEntity<List<PersonDataFromLastNameDTO>> Response = null;
		
		try {
			Response = new ResponseEntity<>(service.getPersonInfo(lastName), HttpStatus.OK);
			log.info("[GET] /personInfo - " + String.valueOf(Response.getStatusCode()));
		} catch (Exception e) {
			Response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[GET] /personInfo - " + String.valueOf(Response.getStatusCode()) + " ("+ e.toString() + ")");
		}
		
		return Response;
	}
	
	@GetMapping("/communityEmail")
	public ResponseEntity<List<String>> getCommunityEmail(@RequestParam String city) {
		ResponseEntity<List<String>> Response = null;
		
		try {
			Response = new ResponseEntity<>(service.getPersonEmailsFromCity(city), HttpStatus.OK);
			log.info("[GET] /communityEmail - " + String.valueOf(Response.getStatusCode()));
		} catch (Exception e) {
			Response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[GET] /communityEmail - " + String.valueOf(Response.getStatusCode()) + " ("+ e.toString() + ")");
		}
		
		return Response;
	}
}
