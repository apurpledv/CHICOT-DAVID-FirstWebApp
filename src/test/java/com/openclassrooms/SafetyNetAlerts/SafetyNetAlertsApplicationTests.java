package com.openclassrooms.SafetyNetAlerts;

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
class PersonsTests {
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void contextLoads() {
		
	}
	
	@Test
	public void testGetPersons() throws Exception {
		this.mockMvc.perform(get("/person")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	public void testAddPerson() throws Exception {
		String bodyContent = "{\"firstName\": \"Boba\", \"lastName\": \"Fett\", \"address\": \"1 Road of Tatooine\", \"city\": \"Parisley\", \"zip\": \"+99\", \"phone\": \"0606060606\", \"email\": \"bobaFett@gmail.com\"}";
		
		this.mockMvc.perform(post("/person")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(bodyContent)
		).andExpect(status().isOk());
	}
	
	@Test
	public void testModifyPerson() throws Exception {
		String bodyContent = "{\"firstName\": \"John\", \"lastName\": \"Boyd\", \"address\": \"1 Road of Tatooine\", \"city\": \"Parisley\", \"zip\": \"+99\", \"phone\": \"0606060606\", \"email\": \"bobaFett@gmail.com\"}";
		
		this.mockMvc.perform(put("/person")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(bodyContent)
		).andExpect(status().isOk());
	}
	
	@Test
	public void testDeletePerson() throws Exception {
		this.mockMvc.perform(delete("/person?firstName=John&lastName=Boyd")).andExpect(status().isOk());
	}
	
	@Test
	public void testChildAlert() throws Exception {
		this.mockMvc.perform(get("/childAlert?address=1509 Culver St")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	public void testChildAlertNonValid() throws Exception {
		this.mockMvc.perform(get("/childAlert")).andExpect(status().isBadRequest());
	}
	
	@Test
	public void testPhoneAlert() throws Exception {
		this.mockMvc.perform(get("/phoneAlert?firestation=1")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	public void testPhoneAlertNonValid() throws Exception {
		this.mockMvc.perform(get("/phoneAlert")).andExpect(status().isBadRequest());
	}
	
	@Test
	public void testPersonInfo() throws Exception {
		this.mockMvc.perform(get("/personInfo?lastName=Boyd")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	public void testPersonInfoNonValid() throws Exception {
		this.mockMvc.perform(get("/personInfo")).andExpect(status().isBadRequest());
	}
	
	@Test
	public void testCommunityEmail() throws Exception {
		this.mockMvc.perform(get("/communityEmail?city=Culver")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	public void testCommunityEmailNonValid() throws Exception {
		this.mockMvc.perform(get("/communityEmail")).andExpect(status().isBadRequest());
	}
}
