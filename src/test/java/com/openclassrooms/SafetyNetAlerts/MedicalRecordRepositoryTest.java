package com.openclassrooms.SafetyNetAlerts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.repository.MedicalRecordRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MedicalRecordRepositoryTest {
	@Autowired
	private MedicalRecordRepository MedicalRecordRepo;
	
	@BeforeEach
	void initTests() {
		MedicalRecordRepo.initRepo();
	}
	
	@AfterAll
	void cleanUp() {
		MedicalRecordRepo.initRepo();
	}
	
	@Test
	void testGetMedicalRecordsAsRepo() {
		List<MedicalRecord> RecordsList = MedicalRecordRepo.getMedicalRecords();
		
		assertFalse(RecordsList.isEmpty());
	}
	
	@Test
	void testGetOneMedicalRecordAsRepo() {
		MedicalRecord RecordFound = MedicalRecordRepo.getMedicalRecord("John", "Boyd");
		
		assertEquals("John", RecordFound.getFirstName());
		assertEquals("Boyd", RecordFound.getLastName());
	}
	
	@Test
	void testAddMedicalRecordAsRepo() {
		MedicalRecord NewRecord = new MedicalRecord();
		NewRecord.setFirstName("Daddy");
		NewRecord.setLastName("Daddy");
		NewRecord.setBirthdate("11/11/2012");
		
		MedicalRecordRepo.addMedicalRecord(NewRecord);
		
		assertFalse(MedicalRecordRepo.getMedicalRecord("Daddy", "Daddy") == null);
	}
	
	@Test
	void testModifyMedicalRecordAsRepo() {
		MedicalRecord RecordToModify = new MedicalRecord();
		
		RecordToModify.setFirstName("John");
		RecordToModify.setLastName("Boyd");
		RecordToModify.setBirthdate("09/09/2009");
		
		MedicalRecordRepo.modifyMedicalRecord(RecordToModify);
		
		assertEquals("09/09/2009", MedicalRecordRepo.getMedicalRecord("John", "Boyd").getBirthdate());
	}

	@Test
	void testDeleteMedicalRecordAsRepo() {
		MedicalRecordRepo.deleteMedicalRecord("John", "Boyd");
		
		assertEquals(null, MedicalRecordRepo.getMedicalRecord("John", "Boyd"));
		
		// This one still exists, thus it shouldn't be null
		assertFalse(MedicalRecordRepo.getMedicalRecord("Jacob", "Boyd") == null);
	}
}
