package com.openclassrooms.SafetyNetAlerts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.openclassrooms.SafetyNetAlerts.service.MedicalRecordService;

@SpringBootTest
@AutoConfigureMockMvc
public class MedicalRecordServiceTest {
	@Autowired
	MedicalRecordService service;
	
	@MockitoBean
	private MedicalRecordRepository MedicalRepo;
	
	@BeforeEach 
    void initEachTest() {
		// PersonsRepo Mock
		List<MedicalRecord> FakeRecordsList = new ArrayList<MedicalRecord>();
		MedicalRecord FakeRecord = new MedicalRecord();
		FakeRecord.setFirstName("Daddy");
		FakeRecord.setLastName("Daddy");
		FakeRecord.setBirthdate("11/11/2012");
		
		FakeRecordsList.add(FakeRecord);

		// 'When->ThenReturn'
		when(MedicalRepo.getMedicalRecords()).thenReturn(FakeRecordsList);
    }
	
	@Test
	void testGetMedicalRecordsAsService() throws Exception {
		List<MedicalRecord> RecordsList = service.getMedicalRecords();
		
		assertFalse(RecordsList.isEmpty());
		assertEquals("Daddy", RecordsList.get(0).getFirstName());
	}
	
	@Test
	void testAddPersonAsService() throws Exception {
		service.addMedicalRecord(new MedicalRecord());
		verify(MedicalRepo, Mockito.times(1)).addMedicalRecord(any(MedicalRecord.class));
	}
	
	@Test
	void testModifyPersonAsService() {
		service.modifyMedicalRecord(new MedicalRecord());
		verify(MedicalRepo, Mockito.times(1)).modifyMedicalRecord(any(MedicalRecord.class));
	}
	
	@Test
	void testDeletePersonAsService() {
		service.deleteMedicalRecord("Daddy", "Daddy");
		verify(MedicalRepo, Mockito.times(1)).deleteMedicalRecord(any(String.class), any(String.class));
	}
}
