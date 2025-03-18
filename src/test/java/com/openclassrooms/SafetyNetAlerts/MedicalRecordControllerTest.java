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
	public void testAddPerson() throws Exception {
		String fakeBodyContent = "{\"firstName\": \"Boba\", \"lastName\": \"Fett\", \"address\": \"1 Road of Tatooine\", \"city\": \"Parisley\", \"zip\": \"+99\", \"phone\": \"0606060606\", \"email\": \"bobaFett@gmail.com\"}";
		
		this.mockMvc.perform(post("/medicalRecord")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(fakeBodyContent)
		).andExpect(status().isOk());
		
		verify(service, Mockito.times(1)).addMedicalRecord(any(MedicalRecord.class));
	}
	
	@Test
	public void testAddPersonThrowsException() throws Exception {
		Mockito.doThrow(new Exception()).when(service).addMedicalRecord(any(MedicalRecord.class));
		
		String fakeBodyContent = "{\"firstName\": \"Boba\", \"lastName\": \"Fett\", \"address\": \"1 Road of Tatooine\", \"city\": \"Parisley\", \"zip\": \"+99\", \"phone\": \"0606060606\", \"email\": \"bobaFett@gmail.com\"}";
		
		this.mockMvc.perform(post("/medicalRecord")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(fakeBodyContent)
		).andExpect(status().isInternalServerError());
	}
	
	@Test
	public void testModifyPerson() throws Exception {
		String bodyContent = "{\"firstName\": \"John\", \"lastName\": \"Boyd\", \"address\": \"1 Road of Tatooine\", \"city\": \"Parisley\", \"zip\": \"+99\", \"phone\": \"0606060606\", \"email\": \"bobaFett@gmail.com\"}";
		
		this.mockMvc.perform(put("/medicalRecord")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(bodyContent)
		).andExpect(status().isOk());

		verify(service, Mockito.times(1)).modifyMedicalRecord(any(MedicalRecord.class));
	}
	
	@Test
	public void testModifyPersonThrowsException() throws Exception {
		Mockito.doThrow(new Exception()).when(service).modifyMedicalRecord(any(MedicalRecord.class));
		
		String bodyContent = "{\"firstName\": \"John\", \"lastName\": \"Boyd\", \"address\": \"1 Road of Tatooine\", \"city\": \"Parisley\", \"zip\": \"+99\", \"phone\": \"0606060606\", \"email\": \"bobaFett@gmail.com\"}";
		
		this.mockMvc.perform(put("/medicalRecord")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(bodyContent)
		).andExpect(status().isInternalServerError());
	}
	
	@Test
	public void testDeletePerson() throws Exception {
		this.mockMvc.perform(delete("/medicalRecord?recordFirstName=John&recordLastName=Boyd"))
			.andExpect(status().isOk());
		
		verify(service, Mockito.times(1)).deleteMedicalRecord(any(String.class), any(String.class));
	}
	
	@Test
	public void testDeletePersonThrowsException() throws Exception {
		Mockito.doThrow(new Exception()).when(service).deleteMedicalRecord(any(String.class), any(String.class));
		
		this.mockMvc.perform(delete("/medicalRecord?recordFirstName=John&recordLastName=Boyd"))
			.andExpect(status().isInternalServerError());
	}
}
