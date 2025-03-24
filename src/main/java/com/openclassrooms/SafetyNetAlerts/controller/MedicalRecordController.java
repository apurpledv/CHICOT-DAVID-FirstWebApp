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

import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.service.MedicalRecordService;
import com.openclassrooms.SafetyNetAlerts.util.MedicalRecordAlreadyExistsException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MedicalRecordController {
	@Autowired
	MedicalRecordService service;
	
	@GetMapping("/medicalRecord")
	public ResponseEntity<List<MedicalRecord>> getMedicalRecords() {
		ResponseEntity<List<MedicalRecord>> Response = null;
		
		try {
			Response = new ResponseEntity<>(service.getMedicalRecords(), HttpStatus.OK);
			log.info("[GET] /medicalRecord - " + String.valueOf(Response.getStatusCode()));
		} catch (Exception e) {
			Response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[GET] /medicalRecord - " + String.valueOf(Response.getStatusCode()) + " ("+ e.toString() + ")");
		}
		
		return Response;
	}
	
	@PostMapping("/medicalRecord")
	public ResponseEntity<HttpStatus> addMedicalRecord(@RequestBody MedicalRecord record) {
		ResponseEntity<HttpStatus> Response = new ResponseEntity<>(HttpStatus.OK);
		
		try {
			if (!record.IsValid()) {
				Response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				log.error("[POST] /medicalRecord - " + String.valueOf(Response.getStatusCode()));
			} else {
				boolean Result = service.addMedicalRecord(record);
				if (Result == false)
					throw new Exception("Could not add person");
				
				log.info("[POST] /medicalRecord - " + String.valueOf(Response.getStatusCode()));
			}
		} catch (MedicalRecordAlreadyExistsException e) {
			Response = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			log.error("[POST] /medicalRecord - " + String.valueOf(Response.getStatusCode()) + " ("+ e.toString() + ")");
		} catch (Exception e) {
			Response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[POST] /medicalRecord - " + String.valueOf(Response.getStatusCode()) + " ("+ e.toString() + ")");
		}
		
		return Response;
	}
	
	@PutMapping("/medicalRecord")
	public ResponseEntity<HttpStatus> modifyMedicalRecord(@RequestBody MedicalRecord record) {
		ResponseEntity<HttpStatus> Response = new ResponseEntity<>(HttpStatus.OK);
		
		try {
			if (!record.IsValid()) {
				Response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				log.error("[PUT] /medicalRecord - " + String.valueOf(Response.getStatusCode()));
			} else {
				boolean Result = service.modifyMedicalRecord(record);
				if (Result == false)
					throw new Exception("Could not modify Record");
				
				log.info("[PUT] /medicalRecord - " + String.valueOf(Response.getStatusCode()));
			}
		} catch (Exception e) {
			Response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[PUT] /medicalRecord - " + String.valueOf(Response.getStatusCode()) + " ("+ e.toString() + ")");
		}
		
		return Response;
	}
	
	@DeleteMapping("/medicalRecord")
	public ResponseEntity<HttpStatus> deleteMedicalRecord(@RequestParam String recordFirstName, @RequestParam String recordLastName) {
		ResponseEntity<HttpStatus> Response = new ResponseEntity<>(HttpStatus.OK);
		
		try {
			boolean Result = service.deleteMedicalRecord(recordFirstName, recordLastName);
			if (Result == false)
				throw new Exception("Could not delete Record");
				
			log.info("[DELETE] /medicalRecord - " + String.valueOf(Response.getStatusCode()));
		} catch (Exception e) {
			Response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("[DELETE] /medicalRecord - " + String.valueOf(Response.getStatusCode()) + " ("+ e.toString() + ")");
		}
		
		return Response;
	}
}
