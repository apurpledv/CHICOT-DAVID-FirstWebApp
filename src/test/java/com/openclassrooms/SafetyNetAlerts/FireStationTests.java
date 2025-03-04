package com.openclassrooms.SafetyNetAlerts;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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

import com.openclassrooms.SafetyNetAlerts.util.SNAUtil;

@SpringBootTest
@AutoConfigureMockMvc
public class FireStationTests {
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void contextLoads() {
		
	}
	
	@Test
	public void testAddFireStation() throws Exception {
		String bodyContent = "{\"address\": \"1 Rue De la Fett\", \"station\": \"99999\"}";
		
		this.mockMvc.perform(post("/firestation")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(bodyContent)
		).andExpect(status().isOk());
	}
	
	@Test
	public void testModifyFireStation() throws Exception {
		String bodyContent = "{\"address\": \"1 Rue De la Fett\", \"station\": \"25\"}";
		
		this.mockMvc.perform(put("/firestation")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(bodyContent)
		).andExpect(status().isOk());
	}
	
	@Test
	public void testDeleteFireStation() throws Exception {
		this.mockMvc.perform(delete("/firestation?address=489 Manchester St&station=4")).andExpect(status().isOk());
	}
	
	@Test
	public void testFireStation() throws Exception {
		this.mockMvc.perform(get("/firestation?stationNumber=2")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	public void testFireStationNonValid() throws Exception {
		this.mockMvc.perform(get("/firestation")).andExpect(status().isBadRequest());
	}
	
	@Test
	public void testFire() throws Exception {
		this.mockMvc.perform(get("/fire?address=892 Downing Ct")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	public void testFireNonValid() throws Exception {
		this.mockMvc.perform(get("/fire")).andExpect(status().isBadRequest());
	}
	
	@Test
	public void testFloodStations() throws Exception {
		this.mockMvc.perform(get("/flood/stations?stations=1,4")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	public void testFloodStationsNonValid() throws Exception {
		this.mockMvc.perform(get("/flood/stations")).andExpect(status().isBadRequest());
	}
}
