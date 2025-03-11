package com.openclassrooms.SafetyNetAlerts.Integration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.SafetyNetAlerts.util.SNAUtil;

@SpringBootTest
@AutoConfigureMockMvc
class PersonIT {
	@Autowired
	private MockMvc mockMvc;
	
	/*@Test
	public void testGetPersonsIT() throws Exception {
		this.mockMvc.perform(get("/person"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON)
		);
	}
	
	@Test
	public void testAddPersonIT() throws Exception {
		String bodyContent = "{\"firstName\": \"Boba\", \"lastName\": \"Fett\", \"address\": \"1 Road of Tatooine\", \"city\": \"Parisley\", \"zip\": \"+99\", \"phone\": \"0606060606\", \"email\": \"bobaFett@gmail.com\"}";
		
		this.mockMvc.perform(post("/person")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(bodyContent)
		).andExpect(status().isOk());
	}
	
	@Test
	public void testModifyPersonIT() throws Exception {
		String bodyContent = "{\"firstName\": \"John\", \"lastName\": \"Boyd\", \"address\": \"1 Road of Tatooine\", \"city\": \"Parisley\", \"zip\": \"+99\", \"phone\": \"0606060606\", \"email\": \"bobaFett@gmail.com\"}";
		
		this.mockMvc.perform(put("/person")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(bodyContent)
		).andExpect(status().isOk());
	}
	
	@Test
	public void testDeletePersonIT() throws Exception {
		this.mockMvc.perform(delete("/person?firstName=John&lastName=Boyd")).andExpect(status().isOk());
	}
	
	@Test
	public void testChildAlertIT() throws Exception {
		this.mockMvc.perform(get("/childAlert?address=1509 Culver St")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	public void testChildAlertNonValidIT() throws Exception {
		this.mockMvc.perform(get("/childAlert")).andExpect(status().isBadRequest());
	}
	
	@Test
	public void testPhoneAlertIT() throws Exception {
		this.mockMvc.perform(get("/phoneAlert?firestation=1")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	public void testPhoneAlertNonValidIT() throws Exception {
		this.mockMvc.perform(get("/phoneAlert")).andExpect(status().isBadRequest());
	}
	
	@Test
	public void testPersonInfoIT() throws Exception {
		this.mockMvc.perform(get("/personInfo?lastName=Boyd")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	public void testPersonInfoNonValidIT() throws Exception {
		this.mockMvc.perform(get("/personInfo")).andExpect(status().isBadRequest());
	}
	
	@Test
	public void testCommunityEmailIT() throws Exception {
		this.mockMvc.perform(get("/communityEmail?city=Culver")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	public void testCommunityEmailNonValidIT() throws Exception {
		this.mockMvc.perform(get("/communityEmail")).andExpect(status().isBadRequest());
	}*/
}
