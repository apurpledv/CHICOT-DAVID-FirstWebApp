package com.openclassrooms.SafetyNetAlerts.Integration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.jayway.jsonpath.JsonPath;
import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.service.FireStationService;
import com.openclassrooms.SafetyNetAlerts.service.MedicalRecordService;
import com.openclassrooms.SafetyNetAlerts.service.PersonService;
import com.openclassrooms.SafetyNetAlerts.util.SNAUtil;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FireStationIT {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	FireStationService FireStationService;
	
	@Autowired
	PersonService PersonService;
	
	@Autowired
	MedicalRecordService RecordService;
	
	@BeforeEach
	void resetTests() {
		PersonService.initRepo("src/main/resources/dataTEST.json");

		FireStationService.initRepo("src/main/resources/dataTEST.json");
		
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
	public void testAddFireStation() throws Exception {
		String BodyContent = "{"
				+ "\"address\": \"1 Rue De la Fett\", "
				+ "\"station\": \"99999\"}";
		
		this.mockMvc.perform(post("/firestation")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(BodyContent)
		).andExpect(status().isOk());
		
		// Test: the station "address: 1 Rue De la Fett, station: 99999"  should exist
		assertFalse(FireStationService.getFireStation("1 Rue De la Fett", "99999") == null);
		assertTrue(FireStationService.getFireStation("1 Rue De la Fett", "-1") == null);
	}
	
	@Test
	public void testModifyFireStation() throws Exception {
		// The original station for 1509 Culver St is 3
		FireStation OldStation = FireStationService.getFireStation("1509 Culver St", "3");
		assertFalse(OldStation == null);
		
		String BodyContent = "{"
				+ "\"address\": \"1509 Culver St\", "
				+ "\"station\": \"25\"}";
		
		this.mockMvc.perform(put("/firestation")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(BodyContent)
		).andExpect(status().isOk());
		
		OldStation = FireStationService.getFireStation("1509 Culver St", "3");
		FireStation ModifiedStation = FireStationService.getFireStation("1509 Culver St", "25");

		// Test: OldStation, the one we modified, doesn't exist with the 'station = 3', so it should return 'null', whereas ModifiedStation, the one after change, should exist with station = 25
		assertTrue(OldStation == null);
		assertFalse(ModifiedStation == null);
	}
	
	@Test
	public void testDeleteFireStation() throws Exception {
		// Before deletion: isn't null
		FireStation DeletedStation = FireStationService.getFireStation("489 Manchester St", "4");
		assertFalse(DeletedStation == null);
		
		this.mockMvc.perform(delete("/firestation?address=489 Manchester St&station=4"))
			.andExpect(status().isOk());
		
		// Test: After deletion: is null
		DeletedStation = FireStationService.getFireStation("489 Manchester St", "4");
		assertTrue(DeletedStation == null);
	}
	
	@Test
	public void testFireStation() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(get("/firestation?stationNumber=2"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andReturn();
		
		String Response = mvcResult.getResponse().getContentAsString();
		int ResultAdultsCount = (int) JsonPath.parse(Response).read("$.adults");
		int ResultChildrenCount= (int) JsonPath.parse(Response).read("$.children");
		
		int ExpectedAdultsCount = 4;
		int ExpectedChildrenCount = 1;
		
		assertEquals(ExpectedAdultsCount, ResultAdultsCount);
		assertEquals(ExpectedChildrenCount, ResultChildrenCount);
	}
	
	@Test
	public void testFireStationNonValid() throws Exception {
		this.mockMvc.perform(get("/firestation"))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testFire() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(get("/fire?address=892 Downing Ct"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andReturn();
		
		String Response = mvcResult.getResponse().getContentAsString();
		String ResultPersonLastName = JsonPath.parse(Response).read("$[0].lastName");
		String ResultPersonPhone = JsonPath.parse(Response).read("$[0].phone");
		int ResultPersonAge = (int) JsonPath.parse(Response).read("$[0].age");
		String ResultPersonStation = JsonPath.parse(Response).read("$[0].stationNumber");
		
		String ExpectedPersonLastName = "Zemicks";
		String ExpectedPersonPhone = "841-874-7878";
		int ExpectedPersonAge = 37;
		String ExpectedPersonStation = "2";
		
		assertEquals(ExpectedPersonLastName, ResultPersonLastName);
		assertEquals(ExpectedPersonPhone, ResultPersonPhone);
		assertEquals(ExpectedPersonAge, ResultPersonAge);
		assertEquals(ExpectedPersonStation, ResultPersonStation);
	}
	
	@Test
	public void testFireNonValid() throws Exception {
		this.mockMvc.perform(get("/fire"))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testFloodStations() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(get("/flood/stations?stations=1,4"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andReturn();
		
		String Response = mvcResult.getResponse().getContentAsString();
		String ResultAddress1 = JsonPath.parse(Response).read("$[0].address");
		String ResultAddress2 = JsonPath.parse(Response).read("$[1].address");
		String ResultAddress3 = JsonPath.parse(Response).read("$[2].address");
		
		String ExpectedAddress1 = "644 Gershwin Cir";
		String ExpectedAddress2 = "908 73rd St";
		String ExpectedAddress3 = "947 E. Rose Dr";
		
		assertEquals(ExpectedAddress1, ResultAddress1);
		assertEquals(ExpectedAddress2, ResultAddress2);
		assertEquals(ExpectedAddress3, ResultAddress3);
	}
	
	@Test
	public void testFloodStationsNonValid() throws Exception {
		this.mockMvc.perform(get("/flood/stations"))
			.andExpect(status().isBadRequest());
	}
}
