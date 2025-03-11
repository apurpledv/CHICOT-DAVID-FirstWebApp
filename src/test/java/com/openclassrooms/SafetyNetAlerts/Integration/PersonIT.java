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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.jayway.jsonpath.JsonPath;
import com.openclassrooms.SafetyNetAlerts.model.ChildDataFromAddressDTO;
import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.model.PersonDataFromLastNameDTO;
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
		PersonService.initRepo();

		FireStationService.initRepo();
		
		RecordService.initRepo();
		RecordService.updatePersonMedicalRecords();
	}
	
	@AfterAll
	void cleanUp() {
		PersonService.initRepo();

		FireStationService.initRepo();
		
		RecordService.initRepo();
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

		List<Person> PersonsList = PersonService.getPersons();
		String ExpectedFirstName = PersonsList.get(0).getFirstName();
		String ExpectedLastName = PersonsList.get(0).getLastName();
		
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
		
		// Test: the person "Boba Fett" should exist, "Bob Bett" shouldn't
		assertFalse(PersonService.getPerson("Boba", "Fett") == null);
		assertTrue(PersonService.getPerson("Boba", "Bett") == null);
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
		String ResultPersonAge = JsonPath.parse(Response).read("$[0].age");
		
		List<ChildDataFromAddressDTO> PersonsList = PersonService.getChildrenFromAddress("1509 Culver St");
		String ExpectedFirstName = PersonsList.get(0).getFirstName();
		String ExpectedLastName = PersonsList.get(0).getLastName();
		String ExpectedAge = PersonsList.get(0).getAge();
		
		// Test: The first child found at this address is the same as the one found through our service 
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
		
		List<String> ExpectedPhones = PersonService.getPhonesFromStationNumber("1");
		String ExpectedFirstPhone = ExpectedPhones.get(0);
		
		// Test: The first phone found is the same as the first one found using our service
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
		String ResultPersonAge = JsonPath.parse(Response).read("$[0].age");
		String ResultPersonEmail = JsonPath.parse(Response).read("$[0].email");
		
		List<PersonDataFromLastNameDTO> ExpectedPersonInfoList = PersonService.getPersonInfo("Boyd");
		String ExpectedPersonInfoLastName = ExpectedPersonInfoList.get(0).getLastName();
		String ExpectedPersonInfoAge = ExpectedPersonInfoList.get(0).getAge();
		String ExpectedPersonInfoEmail = ExpectedPersonInfoList.get(0).getEmail();
		
		// Test: The first data fetched corresponds to the first one retrieved with our service
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
		
		List<String> ExpectedEmails = PersonService.getPersonEmailsFromCity("Culver");
		String ExpectedFirstEmail = ExpectedEmails.get(0);
		
		// Test: The first email found is the same as the first one found using our service
		assertEquals(ExpectedFirstEmail, ResultFirstEmail);
	}
	
	@Test
	public void testCommunityEmailNonValidIT() throws Exception {
		this.mockMvc.perform(get("/communityEmail"))
			.andExpect(status().isBadRequest());
	}
}
