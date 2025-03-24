package com.openclassrooms.SafetyNetAlerts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.openclassrooms.SafetyNetAlerts.model.ChildDataFromAddressDTO;
import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.model.Mapper;
import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.model.PersonDataFromLastNameDTO;
import com.openclassrooms.SafetyNetAlerts.repository.FireStationRepository;
import com.openclassrooms.SafetyNetAlerts.repository.MedicalRecordRepository;
import com.openclassrooms.SafetyNetAlerts.repository.PersonRepository;
import com.openclassrooms.SafetyNetAlerts.service.PersonService;
import com.openclassrooms.SafetyNetAlerts.util.MedicalRecordNotFoundException;
import com.openclassrooms.SafetyNetAlerts.util.PersonAlreadyExistsException;

@SpringBootTest
@AutoConfigureMockMvc
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
		FakePerson.setCity("Culver");
		
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
	void testGetPersonsAsService() throws Exception {
		List<Person> PersonsList = service.getPersons();
		
		assertFalse(PersonsList.isEmpty());
		assertEquals("Daddy", PersonsList.get(0).getFirstName());
	}
	
	@Test
	void testAddPersonAsService() throws Exception {
		when(PersonRepo.addPerson(any(Person.class))).thenReturn(true);
		assertTrue(service.addPerson(new Person()));
	}
	
	@Test
	void testAddPersonAsServiceNonValid() throws Exception {
		// TEST#1 - Returns false
		when(PersonRepo.addPerson(any(Person.class))).thenReturn(false);
		assertFalse(service.addPerson(new Person()));
		
		// TEST#2 - PersonAlreadyExistsException
		Mockito.doThrow(new PersonAlreadyExistsException()).when(PersonRepo).addPerson(any(Person.class));
		assertThrows(PersonAlreadyExistsException.class, () -> service.addPerson(new Person()));
		
		// TEST#3 - Exception
		Mockito.doThrow(new Exception()).when(PersonRepo).addPerson(any(Person.class));
		assertThrows(Exception.class, () -> service.addPerson(new Person()));
	}
	
	@Test
	void testModifyPersonAsService() {
		when(PersonRepo.modifyPerson(any(Person.class))).thenReturn(true);
		assertTrue(service.modifyPerson(new Person()));
		
		// If it fails to modify the person, returns false
		when(PersonRepo.modifyPerson(any(Person.class))).thenReturn(false);
		assertFalse(service.modifyPerson(new Person()));
	}
	
	@Test
	void testDeletePersonAsService() {
		when(PersonRepo.deletePerson(any(String.class), any(String.class))).thenReturn(true);
		assertTrue(service.deletePerson("A", "A"));
		
		// If it fails to delete the person, returns false
		when(PersonRepo.deletePerson(any(String.class), any(String.class))).thenReturn(false);
		assertFalse(service.deletePerson("A", "A"));
	}
	
	@Test
	void testGetPersonEmailsFromCityAsService() throws Exception {
		List<String> MockedEmails = new ArrayList<String>();
		MockedEmails.add("e@gmail.com");
		
		when(PersonRepo.getPersonEmailsFromCity(any(String.class))).thenReturn(MockedEmails);
		
		List<String> EmailsList = service.getPersonEmailsFromCity("Culver");
		
		assertFalse(EmailsList.isEmpty());
		assertEquals("e@gmail.com", EmailsList.get(0));
	}
	
	@Test
	void testGetPhonesFromStationNumberAsService() throws Exception {
		List<Person> FakePersonsList = new ArrayList<Person>();
		Person FakePerson = new Person();
		FakePerson.setFirstName("Daddy");
		FakePerson.setPhone("070707");
		FakePersonsList.add(FakePerson);
		
		when(PersonRepo.getPersonsFromAddress(any(String.class))).thenReturn(FakePersonsList);
		
		List<String> PhonesList = service.getPhonesFromStationNumber("1");
		
		assertFalse(PhonesList.isEmpty());
		assertEquals("070707", PhonesList.get(0));
	}

	@Test
	void testGetPersonInfoAsService() throws Exception {
		PersonDataFromLastNameDTO MockedPersonInfo = new PersonDataFromLastNameDTO("Daddy", null, 20, null, null, null);
		
		when(mapper.toPersonDataFromLastNameDto(any(Person.class))).thenReturn(MockedPersonInfo);
		
		List<PersonDataFromLastNameDTO> PersonInfoList = service.getPersonInfo("Daddy");
		
		assertFalse(PersonInfoList.isEmpty());
		assertEquals("Daddy", PersonInfoList.get(0).getLastName());
	}
	
	@Test
	void testGetChildrenFromAddressAsService() throws Exception {
		List<Person> FakeChildrenList = new ArrayList<Person>();
		Person FakeChild = new Person();
		FakeChild.setFirstName("Kevin");
		FakeChild.setLastName("Deli");
		FakeChild.setAddress("Do");
		
		MedicalRecord FakeRecord = new MedicalRecord();
		FakeRecord.setBirthdate("01/03/2024");
		FakeChild.setRecord(FakeRecord);
		
		FakeChildrenList.add(FakeChild);
		
		// Mocking the 'toChildDataFromAddressDto' to return a fake DTO
		ChildDataFromAddressDTO FakeChildDTO = new ChildDataFromAddressDTO("Kevin", "Deli", 1, new ArrayList<Person>());
		List<Person> EmptyOtherPersonsList = new ArrayList<Person>();

		when(PersonRepo.getPersons()).thenReturn(FakeChildrenList);
		when(PersonRepo.getPersonsFromAddress(any(String.class))).thenReturn(new ArrayList<Person>());
		when(mapper.toChildDataFromAddressDto(any(Person.class), eq(EmptyOtherPersonsList))).thenReturn(FakeChildDTO);
		
		// Test begins now
		List<ChildDataFromAddressDTO> ChildrenList = service.getChildrenFromAddress("Do");
		
		assertFalse(ChildrenList.isEmpty());
		
		ChildDataFromAddressDTO FirstChild = ChildrenList.get(0);
		assertEquals("Kevin", FirstChild.getFirstName());
		assertEquals("Deli", FirstChild.getLastName());
		assertEquals(1, FirstChild.getAge());
	}
	
	@Test
	void testGetChildrenFromAddressAsServiceNonValid() throws Exception {
		// Setup
		List<Person> FakeChildrenList = new ArrayList<Person>();
		Person FakeChild = new Person();
		FakeChild.setFirstName("Kevin");
		FakeChild.setLastName("Deli");
		FakeChild.setAddress("Do");
		
		MedicalRecord FakeRecord = new MedicalRecord();
		FakeRecord.setBirthdate("01/03/2024");
		FakeChild.setRecord(FakeRecord);
		
		FakeChildrenList.add(FakeChild);
		
		// Mocking the 'toChildDataFromAddressDto' to return a fake DTO
		ChildDataFromAddressDTO FakeChildDTO = new ChildDataFromAddressDTO("Kevin", "Deli", 1, new ArrayList<Person>());
		List<Person> EmptyOtherPersonsList = new ArrayList<Person>();

		when(PersonRepo.getPersons()).thenReturn(FakeChildrenList);
		when(PersonRepo.getPersonsFromAddress(any(String.class))).thenReturn(new ArrayList<Person>());
		when(mapper.toChildDataFromAddressDto(any(Person.class), eq(EmptyOtherPersonsList))).thenReturn(FakeChildDTO);
		
		// TEST#1 No Record
		FakeChild.setRecord(null);
		assertThrows(MedicalRecordNotFoundException.class, () -> service.getChildrenFromAddress("Do"));
		
		// TEST#2 Not a Child (>18 years old)
		FakeRecord.setBirthdate("01/03/2004");
		FakeChild.setRecord(FakeRecord);
		assertTrue(service.getChildrenFromAddress("Do").isEmpty());
		
		// TEST#3 Not the right address
		FakeRecord.setBirthdate("01/03/2024");
		FakeChild.setAddress("Dood");	// vs 'Do'
		assertTrue(service.getChildrenFromAddress("Do").isEmpty());
		
		// TEST#4 Duplicates in 'OtherPersons'
		FakeChild.setAddress("Do");
		List<Person> OtherPersonsList = new ArrayList<Person>();
		OtherPersonsList.add(FakeChild);
		
		when(PersonRepo.getPersonsFromAddress(any(String.class))).thenReturn(OtherPersonsList);
		
		List<ChildDataFromAddressDTO> Result = service.getChildrenFromAddress("Do");
		assertFalse(Result.isEmpty());
	}
	
}
