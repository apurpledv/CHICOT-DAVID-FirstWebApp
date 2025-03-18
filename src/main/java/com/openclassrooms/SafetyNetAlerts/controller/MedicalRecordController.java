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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MedicalRecordController {
	@Autowired
	MedicalRecordService service;
	
	@GetMapping("/medicalRecord")
	public ResponseEntity<List<MedicalRecord>> getMedicalRecords() {
		try {
			log.info("'GET/medicalRecord' endpoint requested.");
			return new ResponseEntity<>(service.getMedicalRecords(), HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.toString());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/medicalRecord")
	public ResponseEntity<HttpStatus> addMedicalRecord(@RequestBody MedicalRecord record) {
		try {
			log.info("'POST/medicalRecord' endpoint requested.");
			service.addMedicalRecord(record);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.toString());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/medicalRecord")
	public ResponseEntity<HttpStatus> modifyMedicalRecord(@RequestBody MedicalRecord record) {
		try {
			log.info("'PUT/medicalRecord' endpoint requested.");
			service.modifyMedicalRecord(record);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.toString());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/medicalRecord")
	public ResponseEntity<HttpStatus> deleteMedicalRecord(@RequestParam String recordFirstName, @RequestParam String recordLastName) {
		try {
			log.info("'DELETE/medicalRecord' endpoint requested.");
			service.deleteMedicalRecord(recordFirstName, recordLastName);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.toString());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
