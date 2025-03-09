package com.openclassrooms.SafetyNetAlerts.Integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.SafetyNetAlerts.util.SNAUtil;

@SpringBootTest
@AutoConfigureMockMvc
public class MedicalRecordIT {
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void testGetMedicalRecord() throws Exception {
		this.mockMvc.perform(get("/medicalRecord")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	public void testAddMedicalRecord() throws Exception {
		String bodyContent = "{ \"firstName\":\"Bob\", \"lastName\":\"Bipp\", \"birthdate\":\"03/06/1901\", \"medications\":[\"aznol:350mg\", \"hydrapermazol:100mg\"], \"allergies\":[\"nillacilan\"] },";
		
		this.mockMvc.perform(post("/medicalRecord")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(bodyContent)
		).andExpect(status().isOk());
	}
	
	@Test
	public void testModifyMedicalRecord() throws Exception {
		String bodyContent = "{ \"firstName\":\"Reginold\", \"lastName\":\"Walker\", \"birthdate\":\"08/30/1910\", \"medications\":[], \"allergies\":[] },";
		
		this.mockMvc.perform(put("/medicalRecord")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(bodyContent)
		).andExpect(status().isOk());
	}
	
	@Test
	public void testDeleteMedicalRecord() throws Exception {
		this.mockMvc.perform(delete("/medicalRecord?recordFirstName=John&recordLastName=Boyd")).andExpect(status().isOk());
	}
}
