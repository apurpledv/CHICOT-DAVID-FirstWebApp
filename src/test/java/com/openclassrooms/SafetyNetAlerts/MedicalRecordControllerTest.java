package com.openclassrooms.SafetyNetAlerts;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.service.MedicalRecordService;
import com.openclassrooms.SafetyNetAlerts.util.MedicalRecordAlreadyExistsException;
import com.openclassrooms.SafetyNetAlerts.util.SNAUtil;

@SpringBootTest
@AutoConfigureMockMvc
public class MedicalRecordControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	MedicalRecordService service;
	
	@BeforeEach 
    void resetTests() throws Exception {
		when(service.getMedicalRecords()).thenReturn(new ArrayList<MedicalRecord>());
    }
	
	@Test
	void testGetMedicalRecords() throws Exception  {
		this.mockMvc.perform(get("/medicalRecord"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON));
		
		verify(service, Mockito.times(1)).getMedicalRecords();
	}
	
	@Test
	void testGetMedicalRecordsThrowsException() throws Exception  {
		Mockito.doThrow(new Exception()).when(service).getMedicalRecords();
		
		this.mockMvc.perform(get("/medicalRecord"))
			.andExpect(status().isInternalServerError());
	}
	
	@Test
	public void testAddMedicalRecord() throws Exception {
		when(service.addMedicalRecord(any(MedicalRecord.class))).thenReturn(true);
		
		String BodyContent = "{\"firstName\": \"Moby\", \"lastName\": \"Dick\", \"birthdate\": \"01/01/1999\"}";
		
		this.mockMvc.perform(post("/medicalRecord")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(BodyContent)
		).andExpect(status().isOk());
	}
	
	@Test
	public void testAddRecordNonValid() throws Exception {
		String GoodBodyContent = "{\"firstName\": \"Moby\", \"lastName\": \"Dick\", \"birthdate\": \"01/01/1999\"}";
		String BadBodyContent = "{\"firstName\": \"Boba\"}";
		
		// TEST#1 - Problem with service (returns false when adding)
		when(service.addMedicalRecord(any(MedicalRecord.class))).thenReturn(false);
		
		this.mockMvc.perform(post("/medicalRecord")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(GoodBodyContent)
		).andExpect(status().isInternalServerError());
		
		// TEST#2 - Bad body
		when(service.addMedicalRecord(any(MedicalRecord.class))).thenReturn(true);
		
		this.mockMvc.perform(post("/medicalRecord")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(BadBodyContent)
		).andExpect(status().isBadRequest());
		
		// TEST#3 - MedicalRecordAlreadyExistsException occurs when adding
		Mockito.doThrow(new MedicalRecordAlreadyExistsException()).when(service).addMedicalRecord(any(MedicalRecord.class));
		
		this.mockMvc.perform(post("/medicalRecord")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(GoodBodyContent)
		).andExpect(status().isBadRequest());
		
		// TEST#4 - Exception occurs when adding
		Mockito.doThrow(new Exception()).when(service).addMedicalRecord(any(MedicalRecord.class));

		this.mockMvc.perform(post("/medicalRecord")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(GoodBodyContent)
		).andExpect(status().isInternalServerError());
	}
	
	@Test
	public void testModifyRecord() throws Exception {
		when(service.modifyMedicalRecord(any(MedicalRecord.class))).thenReturn(true);
		
		String BodyContent = "{\"firstName\": \"Moby\", \"lastName\": \"Dick\", \"birthdate\": \"01/01/1999\"}";
		
		this.mockMvc.perform(put("/medicalRecord")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(BodyContent)
		).andExpect(status().isOk());
	}
	
	@Test
	public void testModifyRecordNonValid() throws Exception {
		// TEST#1 - Missing one required attribute in the body's MedicalRecord (birthdate)
		String BadBodyContent = "{\"firstName\": \"Moby\", \"lastName\": \"Dick\"}";
		
		this.mockMvc.perform(put("/medicalRecord")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(BadBodyContent)
		).andExpect(status().isBadRequest());
		
		// TEST#2 - Error in service (some problem with the service or repository, returns false)
		String GoodBodyContent = "{\"firstName\": \"Moby\", \"lastName\": \"Dick\", \"birthdate\": \"01/01/1999\"}";
		
		when(service.modifyMedicalRecord(any(MedicalRecord.class))).thenReturn(false);
		
		this.mockMvc.perform(put("/medicalRecord")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(GoodBodyContent)
		).andExpect(status().isInternalServerError());
	}
	
	@Test
	public void testDeleteRecord() throws Exception {
		when(service.deleteMedicalRecord(any(String.class), any(String.class))).thenReturn(true);
		
		this.mockMvc.perform(delete("/medicalRecord?recordFirstName=John&recordLastName=Boyd"))
			.andExpect(status().isOk());
		
		verify(service, Mockito.times(1)).deleteMedicalRecord(any(String.class), any(String.class));
	}
	
	@Test
	public void testDeleteRecordNonValid() throws Exception {
		// TEST#1 - Bad arguments
		this.mockMvc.perform(delete("/medicalRecord?recordLastName=Boyd"))
			.andExpect(status().isBadRequest());
		
		this.mockMvc.perform(delete("/medicalRecord?recordFirstName=John"))
			.andExpect(status().isBadRequest());
		
		// TEST#2 - Error in service (some problem with the service or repository, returns false)
		when(service.deleteMedicalRecord(any(String.class), any(String.class))).thenReturn(false);
		
		this.mockMvc.perform(delete("/medicalRecord?recordFirstName=John&recordLastName=Boyd"))
			.andExpect(status().isInternalServerError());	
	}
}
