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

/**
 * MedicalRecordService is an entity that handles the business work linked to Medical Records
 */
@Slf4j
@Data
@Service
public class MedicalRecordService {
	@Autowired
	private MedicalRecordRepository RecordRepo;
	
	@Autowired
	private PersonRepository PersonRepo;
	
	/**
	 * Will update the MedicalRecords List upon a modification: will loop every Person registered and, if possible, bind one MedicalRecord to that Person (corresponding first and last names)
	 */
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
	
	/**
	 * Will manually order the loading of data from the default file from its repository
	 */
	public void initRepo() {
		RecordRepo.initRepo();
	}
	
	/**
	 * Will manually order the loading of data from a custom file from its repository
	 */
	public void initRepo(String fileName) {
		RecordRepo.initRepo(fileName);
	}
	
	/**
	 * <p>Will return a List of every MedicalRecord Object registered</p>
	 * @return a List of MedicalRecord Objects
	 */
	public List<MedicalRecord> getMedicalRecords() throws Exception {
		return RecordRepo.getMedicalRecords();
	}
	
	/**
	 * <p>Will return a MedicalRecord Object registered to that first and last name</p>
	 * @param firstName of the person's MedicalRecord to find
	 * @param lastName of the person's MedicalRecord to find
	 * @return a MedicalRecord Object
	 */
	public MedicalRecord getMedicalRecord(String firstName, String lastName) {
		return RecordRepo.getMedicalRecord(firstName, lastName);
	}
	
	/**
	 * Will attempt to add a MedicalRecord Object to the Application
	 * @param record the MedicalRecord Object to add
	 * @return true if successful; false otherwise
	 * @throws Exception if a record for the same person already exists, will throw an Exception
	 */
	public boolean addMedicalRecord(MedicalRecord record) throws Exception {
		boolean Success = RecordRepo.addMedicalRecord(record);
		updatePersonMedicalRecords();
		
		return Success;
	}
	
	/**
	 * <p>Will attempt to modify an already existing MedicalRecord Object</p>
	 * @param record a MedicalRecord Object containing the new attributes
	 * @return true if successful; false no record is found with the same first and last names
	 */
	public boolean modifyMedicalRecord(MedicalRecord record) {
		boolean Success = RecordRepo.modifyMedicalRecord(record);
		updatePersonMedicalRecords();

		return Success;
	}
	
	/**
	 * <p>Will attempt to delete an already existing MedicalRecord Object</p>
	 * @param recordFirstName of the person's MedicalRecord to delete
	 * @param recordLastName of the person's MedicalRecord to delete
	 * @return true if successful; false no record is found with the same first and last names
	 */
	public boolean deleteMedicalRecord(String recordFirstName, String recordLastName) {
		boolean Success = RecordRepo.deleteMedicalRecord(recordFirstName, recordLastName);
		updatePersonMedicalRecords();
		
		return Success;
	}
}
