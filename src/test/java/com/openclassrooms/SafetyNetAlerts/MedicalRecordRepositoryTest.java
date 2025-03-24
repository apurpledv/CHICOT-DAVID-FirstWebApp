package com.openclassrooms.SafetyNetAlerts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import com.openclassrooms.SafetyNetAlerts.util.MedicalRecordAlreadyExistsException;

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
	void testAddMedicalRecordAsRepo() throws Exception {
		MedicalRecord NewRecord = new MedicalRecord();
		NewRecord.setFirstName("Daddy");
		NewRecord.setLastName("Daddy");
		NewRecord.setBirthdate("11/11/2012");
		
		MedicalRecordRepo.addMedicalRecord(NewRecord);
		
		assertFalse(MedicalRecordRepo.getMedicalRecord("Daddy", "Daddy") == null);
	}
	
	@Test
	void testAddMedicalRecordAsRepoNonValid() throws Exception {
		MedicalRecord A = new MedicalRecord();
		A.setFirstName("Daddy");
		A.setLastName("Daddy");
		A.setBirthdate("11/11/2012");
		
		MedicalRecord B = new MedicalRecord();
		B.setFirstName("Daddy");
		B.setLastName("Daddy");
		B.setBirthdate("11/11/2012");
		
		MedicalRecordRepo.addMedicalRecord(A);
		
		assertThrows(MedicalRecordAlreadyExistsException.class, () -> MedicalRecordRepo.addMedicalRecord(B));
	}
	
	@Test
	void testModifyMedicalRecordAsRepo() {
		MedicalRecord RecordToModify = new MedicalRecord();
		
		RecordToModify.setFirstName("John");
		RecordToModify.setLastName("Boyd");
		RecordToModify.setBirthdate("09/09/2009");
		
		boolean Result = MedicalRecordRepo.modifyMedicalRecord(RecordToModify);
		assertTrue(Result);
		
		assertEquals("09/09/2009", MedicalRecordRepo.getMedicalRecord("John", "Boyd").getBirthdate());
	}
	
	@Test
	void testModifyMedicalRecordAsRepoNonValid() {
		MedicalRecord UnknownRecord = new MedicalRecord();
		UnknownRecord.setFirstName("");
		UnknownRecord.setLastName("");
		
		boolean Result = MedicalRecordRepo.modifyMedicalRecord(UnknownRecord);
		assertFalse(Result);
	}

	@Test
	void testDeleteMedicalRecordAsRepo() {
		boolean Result = MedicalRecordRepo.deleteMedicalRecord("John", "Boyd");
		assertTrue(Result);
		
		assertEquals(null, MedicalRecordRepo.getMedicalRecord("John", "Boyd"));
		
		// This one still exists, thus it shouldn't be null
		assertFalse(MedicalRecordRepo.getMedicalRecord("Jacob", "Boyd") == null);
	}
	
	@Test
	void testDeleteMedicalRecordAsRepoNonValid() {
		boolean Result = MedicalRecordRepo.deleteMedicalRecord("", "");
		assertFalse(Result);
	}
}
