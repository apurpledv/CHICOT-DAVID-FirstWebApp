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

import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.service.MedicalRecordService;

@Controller
public class MedicalRecordController {
	@Autowired
	MedicalRecordService service;
	
	@GetMapping("/medicalRecord")
	public ResponseEntity<List<MedicalRecord>> getMedicalRecords() {
		return new ResponseEntity<>(service.getMedicalRecords(), HttpStatus.OK);
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
}
