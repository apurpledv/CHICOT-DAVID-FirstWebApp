package com.openclassrooms.SafetyNetAlerts.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.repository.MedicalRecordRepository;

import lombok.Data;

@Data
@Service
public class MedicalRecordService {
	@Autowired
	private MedicalRecordRepository repo;
	
	public List<MedicalRecord> getMedicalRecords() {
		return repo.getMedicalRecords();
	}
	
	public void addMedicalRecord(MedicalRecord record) {
		repo.addMedicalRecord(record);
	}
	
	public void modifyMedicalRecord(MedicalRecord record) {
		repo.modifyMedicalRecord(record);
	}
	
	public void deleteMedicalRecord(String recordFirstName, String recordLastName) {
		repo.deleteMedicalRecord(recordFirstName, recordLastName);
	}
}
