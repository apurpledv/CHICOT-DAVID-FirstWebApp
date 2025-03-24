package com.openclassrooms.SafetyNetAlerts;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.service.FireStationService;
import com.openclassrooms.SafetyNetAlerts.util.FireStationAlreadyExistsException;
import com.openclassrooms.SafetyNetAlerts.util.SNAUtil;

@SpringBootTest
@AutoConfigureMockMvc
public class FireStationControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	FireStationService service;
	
	@BeforeEach 
    void resetTests() throws Exception {
		when(service.getFireStations()).thenReturn(new ArrayList<FireStation>());
    }
	
	@Test
	public void testAddFireStation() throws Exception {
		when(service.addFireStation(any(FireStation.class))).thenReturn(true);
		
		String BodyContent = "{\"address\": \"1 Rue De la Fett\", \"station\": \"99999\"}";
		
		this.mockMvc.perform(post("/firestation")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(BodyContent)
		).andExpect(status().isOk());
	}
	
	@Test
	public void testAddFireStationNonValid() throws Exception {
		// TEST#1 - Bad Body (the FireStation object is invalid)
		String BadBodyContent = "{}";
				
		this.mockMvc.perform(post("/firestation")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(BadBodyContent)
		).andExpect(status().isBadRequest());
		
		// TEST#2 - Error in service (some problem with the service or repository, returns false)
		when(service.addFireStation(any(FireStation.class))).thenReturn(false);
		
		String GoodBodyContent = "{\"address\": \"1 Road De Tatooine\", \"station\": \"13\"}";
		
		this.mockMvc.perform(post("/firestation")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(GoodBodyContent)
		).andExpect(status().isInternalServerError());
		
		// TEST#3 - FireStationAlreadyExistsException thrown from service
		Mockito.doThrow(new FireStationAlreadyExistsException()).when(service).addFireStation(any(FireStation.class));
		
		this.mockMvc.perform(post("/firestation")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(GoodBodyContent)
		).andExpect(status().isBadRequest());
		
		// TEST#4 - Exception thrown from service
		Mockito.doThrow(new Exception()).when(service).addFireStation(any(FireStation.class));
		
		this.mockMvc.perform(post("/firestation")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(GoodBodyContent)
		).andExpect(status().isInternalServerError());
	}
	
	@Test
	public void testModifyFireStation() throws Exception {
		when(service.modifyFireStation(any(FireStation.class))).thenReturn(true);
		
		String GoodBodyContent = "{\"address\": \"1 Road De Tatooine\", \"station\": \"13\"}";
		
		this.mockMvc.perform(put("/firestation")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(GoodBodyContent)
		).andExpect(status().isOk());
	}
	
	@Test
	public void testModifyFireStationNonValid() throws Exception {
		// TEST#1 - Bad Body (the FireStation object is invalid)
		String BadBodyContent = "{\"address\": \"1 Road De Tatooine\"}";
		
		this.mockMvc.perform(put("/firestation")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(BadBodyContent)
		).andExpect(status().isBadRequest());
		
		// TEST#2 - Error in service (some problem with the service or repository, returns false)
		when(service.modifyFireStation(any(FireStation.class))).thenReturn(false);
		
		String GoodBodyContent = "{\"address\": \"1 Road De Tatooine\", \"station\": \"13\"}";
		
		this.mockMvc.perform(put("/firestation")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(GoodBodyContent)
		).andExpect(status().isInternalServerError());
	}
	
	@Test
	public void testDeleteFireStation() throws Exception {
		when(service.deleteFireStation(any(String.class), any(String.class))).thenReturn(true);
		
		this.mockMvc.perform(delete("/firestation?address=A&station=A"))
			.andExpect(status().isOk());
		
		verify(service, Mockito.times(1)).deleteFireStation(any(String.class), any(String.class));
	}
	
	@Test
	public void testDeleteFireStationNonValid() throws Exception {
		// TEST#1 - Missing arguments
		this.mockMvc.perform(delete("/person?address=A"))
			.andExpect(status().isBadRequest());
		
		this.mockMvc.perform(delete("/person?station=A"))
			.andExpect(status().isBadRequest());
		
		// TEST#2 - Error in service (some problem with the service or repository, returns false)
		when(service.deleteFireStation(any(String.class), any(String.class))).thenReturn(false);
		
		this.mockMvc.perform(delete("/firestation?address=A&station=A"))
			.andExpect(status().isInternalServerError());
	}
	
	@Test
	public void testGetPersonDTOFromStationNumber() throws Exception {
		this.mockMvc.perform(get("/firestation?stationNumber=1"))
			.andExpect(status().isOk());
		
		verify(service, Mockito.times(1)).getPersonDTOFromStationNumber(any(String.class));
	}
	
	@Test
	public void testGetPersonDTOFromStationNumberThrowsException() throws Exception {
		Mockito.doThrow(new Exception()).when(service).getPersonDTOFromStationNumber(any(String.class));
		
		this.mockMvc.perform(get("/firestation?stationNumber=1"))
			.andExpect(status().isInternalServerError());
	}
	
	@Test
	public void testGetPersonDTOFromStationNumberNonValid() throws Exception {
		this.mockMvc.perform(get("/firestation"))
			.andExpect(status().isBadRequest());
		
		verify(service, Mockito.times(0)).getPersonDTOFromStationNumber(any(String.class));
	}
	
	@Test
	public void testGetPersonDTOFromAddress() throws Exception {
		this.mockMvc.perform(get("/fire?address=1 Random Address In Random City"))
			.andExpect(status().isOk());
		
		verify(service, Mockito.times(1)).getPersonDTOFromAddress(any(String.class));
	}
	
	@Test
	public void testGetPersonDTOFromAddressThrowsException() throws Exception {
		Mockito.doThrow(new Exception()).when(service).getPersonDTOFromAddress(any(String.class));
		
		this.mockMvc.perform(get("/fire?address=1 Random Address In Random City"))
			.andExpect(status().isInternalServerError());
	}
	
	@Test
	public void testGetPersonDTOFromAddressNonValid() throws Exception {
		this.mockMvc.perform(get("/fire"))
			.andExpect(status().isBadRequest());
		
		verify(service, Mockito.times(0)).getPersonDTOFromAddress(any(String.class));
	}
	
	@Test
	public void testGetHouseholdDTOFromStations() throws Exception {
		// Equivalent List of stations of this request's RequestParam 'stations'
		List<String> stationsList = new ArrayList<String>();
		stationsList.add("1");
		stationsList.add("2");
		
		this.mockMvc.perform(get("/flood/stations?stations=1,2"))
			.andExpect(status().isOk());
		
		verify(service, Mockito.times(1)).getHouseholdDTOFromStations(eq(stationsList));
	}
	
	@Test
	public void testGetHouseholdDTOFromStationsThrowsException() throws Exception {
		// Equivalent List of stations of this request's RequestParam 'stations'
		List<String> stationsList = new ArrayList<String>();
		stationsList.add("1");
		stationsList.add("2");
				
		Mockito.doThrow(new Exception()).when(service).getHouseholdDTOFromStations(eq(stationsList));
		
		this.mockMvc.perform(get("/flood/stations?stations=1,2"))
			.andExpect(status().isInternalServerError());
	}
	
	@Test
	public void testGetHouseholdDTOFromStationsNonValid() throws Exception {
		// Equivalent List of stations of this request's RequestParam 'stations'
		List<String> stationsList = new ArrayList<String>();
		stationsList.add("1");
		stationsList.add("2");
		
		this.mockMvc.perform(get("/flood/stations"))
			.andExpect(status().isBadRequest());
		
		verify(service, Mockito.times(0)).getHouseholdDTOFromStations(eq(stationsList));
	}
}
