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
		String fakeBodyContent = "{}";
		
		this.mockMvc.perform(post("/firestation")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(fakeBodyContent)
		).andExpect(status().isOk());
		
		verify(service, Mockito.times(1)).addFireStation(any(FireStation.class));
	}
	
	@Test
	public void testAddFireStationThrowsException() throws Exception {
		Mockito.doThrow(new Exception()).when(service).addFireStation(any(FireStation.class));
		
		String fakeBodyContent = "{}";
		
		this.mockMvc.perform(post("/firestation")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(fakeBodyContent)
		).andExpect(status().isInternalServerError());
	}
	
	@Test
	public void testModifyFireStation() throws Exception {
		String fakeBodyContent = "{}";
		
		this.mockMvc.perform(put("/firestation")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(fakeBodyContent)
		).andExpect(status().isOk());

		verify(service, Mockito.times(1)).modifyFireStation(any(FireStation.class));
	}
	
	@Test
	public void testModifyFireStationThrowsException() throws Exception {
		Mockito.doThrow(new Exception()).when(service).modifyFireStation(any(FireStation.class));
		
		String fakeBodyContent = "{}";
		
		this.mockMvc.perform(put("/firestation")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(fakeBodyContent)
		).andExpect(status().isInternalServerError());
	}
	
	@Test
	public void testDeleteFireStation() throws Exception {
		this.mockMvc.perform(delete("/firestation?address=A&station=A"))
			.andExpect(status().isOk());
		
		verify(service, Mockito.times(1)).deleteFireStation(any(String.class), any(String.class));
	}
	
	@Test
	public void testDeleteFireStationThrowsException() throws Exception {
		Mockito.doThrow(new Exception()).when(service).deleteFireStation(any(String.class), any(String.class));
		
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
