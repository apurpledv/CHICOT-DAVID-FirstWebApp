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
import com.openclassrooms.SafetyNetAlerts.util.MedicalRecordNotFoundException;
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

	/**
	 * <p>Will return an HTTP Response containing a List of Person Objects</p>
	 * @return an HTTP Response containing a List of Person Objects, with status 200; an empty HTTP Response with status 500 if an error occurred
	 */
	@GetMapping("/person")
	public ResponseEntity<List<Person>> getPersons() {
		ResponseEntity<List<Person>> Response = null;
		
		try {
			Response = new ResponseEntity<>(service.getPersons(), HttpStatus.OK);
			log.info("[GET] /person - " + String.valueOf(Response.getStatusCode()));
		} catch (Exception e) {
			Response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[GET] /person - " + String.valueOf(Response.getStatusCode()) + " ("+ e.toString() + ")");
		}
		
		return Response;
	}
	
	/**
	 * <p>Will attempt to add a Person into the Application, and return an HTTP Status signifying its success or failure</p>
	 * @param person the Person Object to add
	 * @return an HTTP Response with: Status 200 if everything went well; Status 400 if the person already exists within the Application or isn't valid (not every field is filled); Status 500 if another problem arose; 
	 */
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
	
	/**
	 * <p>Will attempt to modify the attributes (address, phone, etc) of a Person registered in the Application</p>
	 * @param person the Person Object to modify
	 * @return an HTTP Response with: Status 200 if everything went well; Status 400 if the person isn't valid (not every field is filled); Status 500 if another problem arose;
	 */
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
	
	/**
	 * <p>Will attempt to delete a person registered in the Application</p>
	 * @param firstName of the Person to delete
	 * @param lastName of the Person to delete
	 * @return an HTTP Response with: Status 200 if everything went well; Status 500 if an error occurred during the deletion process;
	 */
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
	
	/**
	 * <p>Will retrieve every Child at a given address</p>
	 * @param address the address to look for
	 * @return an HTTP Response containing a list of Child DTOs if successful; an empty HTTP Response with status 500 if at any point, a medical record is not found
	 */
	@GetMapping("/childAlert")
	public ResponseEntity<List<ChildDataFromAddressDTO>> getChildrenFromAddress(@RequestParam String address) {
		ResponseEntity<List<ChildDataFromAddressDTO>> Response = null;
		
		try {
			Response = new ResponseEntity<>(service.getChildrenFromAddress(address), HttpStatus.OK);
			log.info("[GET] /childAlert - " + String.valueOf(Response.getStatusCode()));
		} catch (MedicalRecordNotFoundException e) {
			Response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[GET] /childAlert - " + String.valueOf(Response.getStatusCode()) + " ("+ e.toString() + ")");
		} catch (Exception e) {
			Response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[GET] /childAlert - " + String.valueOf(Response.getStatusCode()) + " ("+ e.toString() + ")");
		}
		
		return Response;
	}
	
	/**
	 * <p>Will retrieve the phone numbers of every Person covered by a specific FireStation</p>
	 * @param firestation number of the Station to check for
	 * @return an HTTP Response containing the list of Phone numbers if successful; an empty HTTP Response with status 500 if an error occurred
	 */
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
	
	/**
	 * <p>Will retrieve information related to Person(s) with a specific last name</p>
	 * @param lastName of the Person(s) to find
	 * @return an HTTP Response containing the list Person DTOs if successful; an empty HTTP Response with status 500 if an error occurred
	 */
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
	
	/**
	 * <p>Will retrieve the email addresses of every Person living in a specific city</p>
	 * @param city the name of the City to parse from
	 * @return an HTTP Response containing a List of email addresses if successful; an empty HTTP Response with status 500 if an error occurred
	 */
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
