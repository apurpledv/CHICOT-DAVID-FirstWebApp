package com.openclassrooms.SafetyNetAlerts.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.openclassrooms.SafetyNetAlerts.repository.PersonRepository;

import jakarta.annotation.PostConstruct;
import lombok.Data;

@Data
@Service
public class MedicalRecordService {
	@Autowired
	private MedicalRecordRepository RecordRepo;
	
	@Autowired
	private PersonRepository PersonRepo;
	
	@PostConstruct
	public void updatePersonMedicalRecords() {
		List<Person> Persons = PersonRepo.getPersons();
		for (Person person : Persons) {
			MedicalRecord record = RecordRepo.getMedicalRecord(person.getFirstName(), person.getLastName());
			if (record == null) {
				person.setRecord(null);
				continue;	
			}
			
			person.setRecord(record);
		}
	}
	
	public List<MedicalRecord> getMedicalRecords() {
		return RecordRepo.getMedicalRecords();
	}
	
	public void addMedicalRecord(MedicalRecord record) {
		RecordRepo.addMedicalRecord(record);
		updatePersonMedicalRecords();
	}
	
	public void modifyMedicalRecord(MedicalRecord record) {
		RecordRepo.modifyMedicalRecord(record);
		updatePersonMedicalRecords();
	}
	
	public void deleteMedicalRecord(String recordFirstName, String recordLastName) {
		RecordRepo.deleteMedicalRecord(recordFirstName, recordLastName);
		updatePersonMedicalRecords();
	}
}
