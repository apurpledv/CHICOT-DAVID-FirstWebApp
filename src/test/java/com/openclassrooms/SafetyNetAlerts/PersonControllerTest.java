package com.openclassrooms.SafetyNetAlerts;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.service.PersonService;
import com.openclassrooms.SafetyNetAlerts.util.SNAUtil;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	PersonService service;
	
	@BeforeEach 
    void resetTests() throws Exception {
		when(service.getPersons()).thenReturn(new ArrayList<Person>());
    }
	
	@Test
	public void testGetPersons() throws Exception {
		this.mockMvc.perform(get("/person"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON));

		verify(service, Mockito.times(1)).getPersons();
	}
	
	@Test
	public void testGetPersonsThrowsException() throws Exception {
		Mockito.doThrow(new Exception()).when(service).getPersons();
		
		this.mockMvc.perform(get("/person"))
			.andExpect(status().isInternalServerError());
	}
	
	@Test
	public void testAddPerson() throws Exception {
		String fakeBodyContent = "{\"firstName\": \"Boba\", \"lastName\": \"Fett\", \"address\": \"1 Road of Tatooine\", \"city\": \"Parisley\", \"zip\": \"+99\", \"phone\": \"0606060606\", \"email\": \"bobaFett@gmail.com\"}";
		
		this.mockMvc.perform(post("/person")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(fakeBodyContent)
		).andExpect(status().isOk());
		
		verify(service, Mockito.times(1)).addPerson(any(Person.class));
	}
	
	@Test
	public void testAddPersonThrowsException() throws Exception {
		Mockito.doThrow(new Exception()).when(service).addPerson(any(Person.class));
		
		String fakeBodyContent = "{\"firstName\": \"Boba\", \"lastName\": \"Fett\", \"address\": \"1 Road of Tatooine\", \"city\": \"Parisley\", \"zip\": \"+99\", \"phone\": \"0606060606\", \"email\": \"bobaFett@gmail.com\"}";
		
		this.mockMvc.perform(post("/person")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(fakeBodyContent)
		).andExpect(status().isInternalServerError());
	}
	
	@Test
	public void testModifyPerson() throws Exception {
		String bodyContent = "{\"firstName\": \"John\", \"lastName\": \"Boyd\", \"address\": \"1 Road of Tatooine\", \"city\": \"Parisley\", \"zip\": \"+99\", \"phone\": \"0606060606\", \"email\": \"bobaFett@gmail.com\"}";
		
		this.mockMvc.perform(put("/person")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(bodyContent)
		).andExpect(status().isOk());

		verify(service, Mockito.times(1)).modifyPerson(any(Person.class));
	}
	
	@Test
	public void testModifyPersonThrowsException() throws Exception {
		Mockito.doThrow(new Exception()).when(service).modifyPerson(any(Person.class));
		
		String bodyContent = "{\"firstName\": \"John\", \"lastName\": \"Boyd\", \"address\": \"1 Road of Tatooine\", \"city\": \"Parisley\", \"zip\": \"+99\", \"phone\": \"0606060606\", \"email\": \"bobaFett@gmail.com\"}";
		
		this.mockMvc.perform(put("/person")
			.contentType(SNAUtil.APPLICATION_JSON_UTF8)
			.content(bodyContent)
		).andExpect(status().isInternalServerError());
	}
	
	@Test
	public void testDeletePerson() throws Exception {
		this.mockMvc.perform(delete("/person?firstName=John&lastName=Boyd"))
			.andExpect(status().isOk());
		
		verify(service, Mockito.times(1)).deletePerson(any(String.class), any(String.class));
	}
	
	@Test
	public void testDeletePersonThrowsException() throws Exception {
		Mockito.doThrow(new Exception()).when(service).deletePerson(any(String.class), any(String.class));
		
		this.mockMvc.perform(delete("/person?firstName=John&lastName=Boyd"))
			.andExpect(status().isInternalServerError());
	}
	
	@Test
	public void testChildAlert() throws Exception {
		this.mockMvc.perform(get("/childAlert?address=1509 Culver St"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON));

		verify(service, Mockito.times(1)).getChildrenFromAddress(any(String.class));
	}
	
	@Test
	public void testChildAlertThrowsException() throws Exception {
		Mockito.doThrow(new Exception()).when(service).getChildrenFromAddress(any(String.class));
		
		this.mockMvc.perform(get("/childAlert?address=1509 Culver St"))
			.andExpect(status().isInternalServerError());

		verify(service, Mockito.times(1)).getChildrenFromAddress(any(String.class));
	}
	
	@Test
	public void testChildAlertNonValid() throws Exception {
		this.mockMvc.perform(get("/childAlert"))
			.andExpect(status().isBadRequest());
		
		verify(service, Mockito.times(0)).getChildrenFromAddress(any(String.class));
	}
	
	@Test
	public void testPhoneAlert() throws Exception {
		this.mockMvc.perform(get("/phoneAlert?firestation=1"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON));
		
		verify(service, Mockito.times(1)).getPhonesFromStationNumber(any(String.class));
	}
	
	@Test
	public void testPhoneAlertThrowsException() throws Exception {
		Mockito.doThrow(new Exception()).when(service).getPhonesFromStationNumber(any(String.class));
		
		this.mockMvc.perform(get("/phoneAlert?firestation=1"))
			.andExpect(status().isInternalServerError());
		
		verify(service, Mockito.times(1)).getPhonesFromStationNumber(any(String.class));
	}
	
	@Test
	public void testPhoneAlertNonValid() throws Exception {
		this.mockMvc.perform(get("/phoneAlert"))
			.andExpect(status().isBadRequest());

		verify(service, Mockito.times(0)).getPhonesFromStationNumber(any(String.class));
	}
	
	@Test
	public void testPersonInfo() throws Exception {
		this.mockMvc.perform(get("/personInfo?lastName=Boyd")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));

		verify(service, Mockito.times(1)).getPersonInfo(any(String.class));
	}
	
	@Test
	public void testPersonInfoThrowsException() throws Exception {
		Mockito.doThrow(new Exception()).when(service).getPersonInfo(any(String.class));
		
		this.mockMvc.perform(get("/personInfo?lastName=Boyd"))
			.andExpect(status().isInternalServerError());
	}
	
	@Test
	public void testPersonInfoNonValid() throws Exception {
		this.mockMvc.perform(get("/personInfo")).andExpect(status().isBadRequest());
		
		verify(service, Mockito.times(0)).getPersonInfo(any(String.class));
	}
	
	@Test
	public void testCommunityEmail() throws Exception {
		this.mockMvc.perform(get("/communityEmail?city=Culver")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));

		verify(service, Mockito.times(1)).getPersonEmailsFromCity(any(String.class));
	}
	
	@Test
	public void testCommunityEmailThrowsException() throws Exception {
		Mockito.doThrow(new Exception()).when(service).getPersonEmailsFromCity(any(String.class));
		
		this.mockMvc.perform(get("/communityEmail?city=Culver"))
			.andExpect(status().isInternalServerError());
	}
	
	@Test
	public void testCommunityEmailNonValid() throws Exception {
		this.mockMvc.perform(get("/communityEmail")).andExpect(status().isBadRequest());
		
		verify(service, Mockito.times(0)).getPersonEmailsFromCity(any(String.class));
	}
}
