package com.openclassrooms.SafetyNetAlerts.Integration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.jayway.jsonpath.JsonPath;
import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.service.MedicalRecordService;
import com.openclassrooms.SafetyNetAlerts.service.PersonService;
import com.openclassrooms.SafetyNetAlerts.service.FireStationService;
import com.openclassrooms.SafetyNetAlerts.util.SNAUtil;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonIT {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	PersonService PersonService;
	
	@Autowired
	FireStationService FireStationService;
	
	@Autowired
	MedicalRecordService RecordService;
	
	@BeforeEach
	void resetTests() {
		PersonService.initRepo("src/main/resources/dataTEST.json");

		FireStationService.initRepo();
		
		RecordService.initRepo("src/main/resources/dataTEST.json");
		RecordService.updatePersonMedicalRecords();
	}
	
	@AfterAll
	void cleanUp() {
		PersonService.initRepo("src/main/resources/dataTEST.json");

		FireStationService.initRepo("src/main/resources/dataTEST.json");
		
		RecordService.initRepo("src/main/resources/dataTEST.json");
		RecordService.updatePersonMedicalRecords();
	}
	
	@Test
	public void testGetPersonsIT() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(get("/person"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andReturn();
		
		String Response = mvcResult.getResponse().getContentAsString();
		String ResultPersonFirstName = JsonPath.parse(Response).read("$[0].firstName");
		String ResultPersonLastName = JsonPath.parse(Response).read("$[0].lastName");

		String ExpectedFirstName = "John";
		String ExpectedLastName = "Boyd";
		
		assertEquals(ExpectedFirstName, ResultPersonFirstName);
		assertEquals(ExpectedLastName, ResultPersonLastName);
	}
	
	@Test
	public void testAddPersonIT() throws Exception {
		String BodyContent = "{"
				+ "\"firstName\": \"Boba\","
				+ "\"lastName\": \"Fett\","
				+ "\"address\": \"1 Road of Tatooine\","
				+ "\"city\": \"Parisley\", "
				+ "\"zip\": \"+99\","
				+ "\"phone\": \"0606060606\", "
				+ "\"email\": \"bobaFett@gmail.com\"}";
		
		this.mockMvc.perform(post("/person")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(BodyContent)
		).andExpect(status().isOk());
		
		// Test: the person "Boba Fett" should exist, "Bob Betty" shouldn't
		assertFalse(PersonService.getPerson("Boba", "Fett") == null);
		assertTrue(PersonService.getPerson("Boba", "Betty") == null);
	}
	
	@Test
	public void testModifyPersonIT() throws Exception {
		// The original city for John Boyd is Culver
		Person PersonToChange = PersonService.getPerson("John", "Boyd");
		assertTrue(PersonToChange.getCity().equals("Culver"));
		
		// New data: City{Culver -> Parisley}
		String BodyContent = "{"
				+ "\"firstName\": \"John\","
				+ "\"lastName\": \"Boyd\","
				+ "\"address\": \"1 Road of Tatooine\","
				+ "\"city\": \"Parisley\", "
				+ "\"zip\": \"+99\","
				+ "\"phone\": \"0606060606\", "
				+ "\"email\": \"bobaFett@gmail.com\"}";
		
		this.mockMvc.perform(put("/person")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(BodyContent)
		).andExpect(status().isOk());

		// Test: John Boyd's city should now be 
		assertFalse(PersonToChange.getCity().equals("Culver"));
		assertTrue(PersonToChange.getCity().equals("Parisley"));
	}
	
	@Test
	public void testDeletePersonIT() throws Exception {
		// John Boyd still exists
		Person PersonToDelete = PersonService.getPerson("John", "Boyd");
		assertFalse(PersonToDelete == null);
		
		// We delete "John Boyd"
		this.mockMvc.perform(delete("/person?firstName=John&lastName=Boyd"))
			.andExpect(status().isOk());
		
		// John Boyd shouldn't exist anymore
		Person PersonDeleted = PersonService.getPerson("John", "Boyd");
		assertTrue(PersonDeleted == null);
	}
	
	@Test
	public void testChildAlertIT() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(get("/childAlert?address=1509 Culver St"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andReturn();
		
		String Response = mvcResult.getResponse().getContentAsString();
		String ResultPersonFirstName = JsonPath.parse(Response).read("$[0].firstName");
		String ResultPersonLastName = JsonPath.parse(Response).read("$[0].lastName");
		int ResultPersonAge = (int) JsonPath.parse(Response).read("$[0].age");
		
		String ExpectedFirstName = "Tenley";
		String ExpectedLastName = "Boyd";
		int ExpectedAge = 13;
		
		assertEquals(ExpectedFirstName, ResultPersonFirstName);
		assertEquals(ExpectedLastName, ResultPersonLastName);
		assertEquals(ExpectedAge, ResultPersonAge);
	}
	
	@Test
	public void testChildAlertNonValidIT() throws Exception {
		this.mockMvc.perform(get("/childAlert"))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testPhoneAlertIT() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(get("/phoneAlert?firestation=1"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andReturn();
		
		String Response = mvcResult.getResponse().getContentAsString();
		String ResultFirstPhone = JsonPath.parse(Response).read("$[0]");
		
		String ExpectedFirstPhone = "841-874-6512";
		
		assertEquals(ExpectedFirstPhone, ResultFirstPhone);
	}
	
	@Test
	public void testPhoneAlertNonValidIT() throws Exception {
		this.mockMvc.perform(get("/phoneAlert"))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testPersonInfoIT() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(get("/personInfo?lastName=Boyd"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andReturn();
		
		String Response = mvcResult.getResponse().getContentAsString();
		String ResultPersonLastName = JsonPath.parse(Response).read("$[0].lastName");
		int ResultPersonAge = (int) JsonPath.parse(Response).read("$[0].age");
		String ResultPersonEmail = JsonPath.parse(Response).read("$[0].email");
		
		String ExpectedPersonInfoLastName = "Boyd";
		int ExpectedPersonInfoAge = 41;
		String ExpectedPersonInfoEmail = "jaboyd@email.com";
		
		assertEquals(ExpectedPersonInfoLastName, ResultPersonLastName);
		assertEquals(ExpectedPersonInfoAge, ResultPersonAge);
		assertEquals(ExpectedPersonInfoEmail, ResultPersonEmail);
	}
	
	@Test
	public void testPersonInfoNonValidIT() throws Exception {
		this.mockMvc.perform(get("/personInfo"))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testCommunityEmailIT() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(get("/communityEmail?city=Culver"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andReturn();
		
		String Response = mvcResult.getResponse().getContentAsString();
		String ResultFirstEmail = JsonPath.parse(Response).read("$[0]");
		
		String ExpectedFirstEmail = "jaboyd@email.com";
		
		assertEquals(ExpectedFirstEmail, ResultFirstEmail);
	}
	
	@Test
	public void testCommunityEmailNonValidIT() throws Exception {
		this.mockMvc.perform(get("/communityEmail"))
			.andExpect(status().isBadRequest());
	}
}
