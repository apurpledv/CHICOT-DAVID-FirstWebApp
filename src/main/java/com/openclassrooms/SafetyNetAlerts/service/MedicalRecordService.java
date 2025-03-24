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
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	
	public void initRepo() {
		RecordRepo.initRepo();
	}
	
	public void initRepo(String fileName) {
		RecordRepo.initRepo(fileName);
	}
	
	public List<MedicalRecord> getMedicalRecords() throws Exception {
		return RecordRepo.getMedicalRecords();
	}
	
	public MedicalRecord getMedicalRecord(String firstName, String lastName) {
		return RecordRepo.getMedicalRecord(firstName, lastName);
	}
	
	public boolean addMedicalRecord(MedicalRecord record) throws Exception {
		boolean Success = RecordRepo.addMedicalRecord(record);
		updatePersonMedicalRecords();
		
		return Success;
	}
	
	public boolean modifyMedicalRecord(MedicalRecord record) {
		boolean Success = RecordRepo.modifyMedicalRecord(record);
		updatePersonMedicalRecords();

		return Success;
	}
	
	public boolean deleteMedicalRecord(String recordFirstName, String recordLastName) {
		boolean Success = RecordRepo.deleteMedicalRecord(recordFirstName, recordLastName);
		updatePersonMedicalRecords();
		
		return Success;
	}
}
