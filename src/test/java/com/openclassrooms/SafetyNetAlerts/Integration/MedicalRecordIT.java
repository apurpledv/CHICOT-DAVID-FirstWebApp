package com.openclassrooms.SafetyNetAlerts.Integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.jayway.jsonpath.JsonPath;
import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.service.MedicalRecordService;
import com.openclassrooms.SafetyNetAlerts.util.SNAUtil;


@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MedicalRecordIT {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	MedicalRecordService RecordService;
	
	@BeforeEach
	void resetTests() {
		RecordService.initRepo("src/main/resources/dataTEST.json");
	}
	
	@AfterAll
	void cleanUp() {
		RecordService.initRepo("src/main/resources/dataTEST.json");
	}
	
	@Test
	public void testGetMedicalRecordsIT() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(get("/medicalRecord"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andReturn();

		String Response = mvcResult.getResponse().getContentAsString();
		String ResultRecordFirstName = JsonPath.parse(Response).read("$[0].firstName");
		String ResultRecordLastName = JsonPath.parse(Response).read("$[0].lastName");
		String ResultRecordBirthDate = JsonPath.parse(Response).read("$[0].birthdate");
		
		String ExpectedFirstName = "John";
		String ExpectedLastName = "Boyd";
		String ExpectedBirthDate = "03/06/1984";
		
		assertEquals(ExpectedFirstName, ResultRecordFirstName);
		assertEquals(ExpectedLastName, ResultRecordLastName);
		assertEquals(ExpectedBirthDate, ResultRecordBirthDate);
	}
	
	@Test
	public void testAddMedicalRecordIT() throws Exception {
		String BodyContent = "{ "
				+ "\"firstName\":\"Bob\","
				+ "\"lastName\":\"Bipp\", "
				+ "\"birthdate\":\"03/06/1901\", "
				+ "\"medications\":[\"aznol:350mg\", \"hydrapermazol:100mg\"], "
				+ "\"allergies\":[\"nillacilan\"] },";
		
		this.mockMvc.perform(post("/medicalRecord")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(BodyContent)
		).andExpect(status().isOk());
		
		// Test: the record for "Bob Bipp" should exist, the one for "Bob Bippppppp" shouldn't
		assertFalse(RecordService.getMedicalRecord("Bob", "Bipp") == null);
		assertTrue(RecordService.getMedicalRecord("Bob", "Bippppppp") == null);
	}
	
	@Test
	public void testModifyMedicalRecordIT() throws Exception {
		// The medications and allergies aren't empty
		MedicalRecord RecordToChange = RecordService.getMedicalRecord("John", "Boyd");
		assertFalse(RecordToChange.getMedications().isEmpty());
		assertFalse(RecordToChange.getAllergies().isEmpty());

		// New data: medications and allergies become empty Lists
		String BodyContent = "{ "
				+ "\"firstName\":\"John\", "
				+ "\"lastName\":\"Boyd\","
				+ "\"birthdate\":\"08/30/1910\", "
				+ "\"medications\":[], "
				+ "\"allergies\":[] },";

		this.mockMvc.perform(put("/medicalRecord")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(BodyContent)
		).andExpect(status().isOk());
		
		// Test: medications and allergies should be empty
		assertTrue(RecordToChange.getMedications().isEmpty());
		assertTrue(RecordToChange.getAllergies().isEmpty());
	}
	
	@Test
	public void testDeleteMedicalRecordIT() throws Exception {
		// The medications and allergies aren't empty
		MedicalRecord RecordToDelete = RecordService.getMedicalRecord("John", "Boyd");
		assertFalse(RecordToDelete == null);
		
		// We delete this record
		this.mockMvc.perform(delete("/medicalRecord?recordFirstName=John&recordLastName=Boyd"))
			.andExpect(status().isOk());
		
		// Test: the record should be null (doesn't exist anymore)
		MedicalRecord RecordDeleted = RecordService.getMedicalRecord("John", "Boyd");
		assertTrue(RecordDeleted == null);
	}
}
