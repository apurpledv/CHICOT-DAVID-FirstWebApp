package com.openclassrooms.SafetyNetAlerts;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.model.Mapper;
import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.repository.FireStationRepository;
import com.openclassrooms.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.openclassrooms.SafetyNetAlerts.repository.PersonRepository;
import com.openclassrooms.SafetyNetAlerts.service.PersonService;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonServiceTest {
	@Autowired
	PersonService service;

	@MockitoBean
	private PersonRepository PersonRepo;
	
	@MockitoBean
	private FireStationRepository FireRepo;
	
	@MockitoBean
	private MedicalRecordRepository MedicalRepo;
	
	@MockitoBean
	private Mapper mapper;
	
	@BeforeEach 
    void initEachTest() {
		// PersonsRepo Mock
		List<Person> FakePersonsList = new ArrayList<Person>();
		Person FakePerson = new Person();
		
		FakePerson.setFirstName("Daddy");
		FakePerson.setLastName("Daddy");
		FakePerson.setAddress("Do");
		FakePerson.setPhone("0101010101");
		
		MedicalRecord FakeRecord = new MedicalRecord();
		FakeRecord.setFirstName("Daddy");
		FakeRecord.setLastName("Daddy");
		FakeRecord.setBirthdate("11/11/2012");
		FakePerson.setRecord(FakeRecord);
		
		FakePersonsList.add(FakePerson);
		
		// FireStationRepo Mock
		List<FireStation> FakeStationsList = new ArrayList<FireStation>();
		FireStation FakeStation = new FireStation();
		FakeStation.setAddress("Do");
		FakeStation.setStation("1");
		FakeStationsList.add(FakeStation);

		// 'When->ThenReturn'
		when(PersonRepo.getPersons()).thenReturn(FakePersonsList);
		when(FireRepo.getFireStations()).thenReturn(FakeStationsList);
    }
	
	@Test
	void testGetPersonsAsService() {
		service.getPersons();
		verify(PersonRepo, Mockito.times(1)).getPersons();
	}
	
	@Test
	void testAddPersonAsService() {
		service.addPerson(new Person());
		verify(PersonRepo, Mockito.times(1)).addPerson(any(Person.class));
	}
	
	@Test
	void testModifyPersonAsService() {
		service.modifyPerson(new Person());
		verify(PersonRepo, Mockito.times(1)).modifyPerson(any(Person.class));
	}
	
	@Test
	void testDeletePersonAsService() {
		service.deletePerson("A", "A");
		verify(PersonRepo, Mockito.times(1)).deletePerson(any(String.class), any(String.class));
	}
	
	@Test
	void testGetPersonEmailsFromCityAsService() {
		service.getPersonEmailsFromCity("Culver");
		verify(PersonRepo, Mockito.times(1)).getPersonEmailsFromCity(any(String.class));
	}
	
	@Test
	void testGetPhonesFromStationNumberAsService() {
		service.getPhonesFromStationNumber("1");

		verify(FireRepo, Mockito.times(1)).getFireStations();
		verify(PersonRepo, Mockito.times(1)).getPersonsFromAddress(any(String.class));
	}

	@Test
	void testGetPersonInfoAsService() {
		service.getPersonInfo("Daddy");

		verify(PersonRepo, Mockito.times(1)).getPersons();
		verify(mapper, Mockito.times(1)).toPersonDataFromLastNameDto(any(Person.class));
	}
	
	@Test
	void testGetChildrenFromAddressAsService() {
		service.getChildrenFromAddress("Do");
		

		verify(PersonRepo, Mockito.times(2)).getPersons();
		verify(PersonRepo, Mockito.times(1)).getPersonsFromAddress(any(String.class));
		
		ArrayList<Person> MockOtherMembers = new ArrayList<Person>();
		verify(mapper, Mockito.times(1)).toChildDataFromAddressDto(any(Person.class), eq(MockOtherMembers));
		
	}
	
}
