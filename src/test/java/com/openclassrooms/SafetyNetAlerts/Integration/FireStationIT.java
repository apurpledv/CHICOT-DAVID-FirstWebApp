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

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.jayway.jsonpath.JsonPath;
import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.model.HouseholdFromStationsDTO;
import com.openclassrooms.SafetyNetAlerts.model.PersonDataFromAddressDTO;
import com.openclassrooms.SafetyNetAlerts.model.PersonsDataFromStationDTO;
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
		String ResultAdultsCount = JsonPath.parse(Response).read("$.adults");
		String ResultChildrenCount= JsonPath.parse(Response).read("$.children");
		
		PersonsDataFromStationDTO ExpectedData = FireStationService.getPersonDTOFromStationNumber("2");
		String ExpectedAdultsCount = ExpectedData.getAdults();
		String ExpectedChildrenCount = ExpectedData.getChildren();
		
		// Test: Same number of adults and children when using URL and using service directly
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
		String ResultPersonAge = JsonPath.parse(Response).read("$[0].age");
		String ResultPersonStation = JsonPath.parse(Response).read("$[0].stationNumber");
		
		List<PersonDataFromAddressDTO> ExpectedList = FireStationService.getPersonDTOFromAddress("892 Downing Ct");
		String ExpectedPersonLastName = ExpectedList.get(0).getLastName();
		String ExpectedPersonPhone = ExpectedList.get(0).getPhone();
		String ExpectedPersonAge = ExpectedList.get(0).getAge();
		String ExpectedPersonStation = ExpectedList.get(0).getStationNumber();
		
		// Test: Same data for the first person DTO gotten from URL and from Service
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
		String ResultAddress4 = JsonPath.parse(Response).read("$[3].address");
		String ResultAddress5 = JsonPath.parse(Response).read("$[4].address");
		
		List<String> StationNumbers = new ArrayList<String>();
		StationNumbers.add("1");
		StationNumbers.add("4");
		
		List<HouseholdFromStationsDTO> ExpectedList = FireStationService.getHouseholdDTOFromStations(StationNumbers);
		String ExpectedAddress1 = ExpectedList.get(0).getAddress();
		String ExpectedAddress2 = ExpectedList.get(1).getAddress();
		String ExpectedAddress3 = ExpectedList.get(2).getAddress();
		String ExpectedAddress4 = ExpectedList.get(3).getAddress();
		String ExpectedAddress5 = ExpectedList.get(4).getAddress();
		
		// Test: There are 5 households handled by stations 1 and 4; we check their addresses (URL vs Service)
		assertEquals(ExpectedAddress1, ResultAddress1);
		assertEquals(ExpectedAddress2, ResultAddress2);
		assertEquals(ExpectedAddress3, ResultAddress3);
		assertEquals(ExpectedAddress4, ResultAddress4);
		assertEquals(ExpectedAddress5, ResultAddress5);
	}
	
	@Test
	public void testFloodStationsNonValid() throws Exception {
		this.mockMvc.perform(get("/flood/stations"))
			.andExpect(status().isBadRequest());
	}
}
