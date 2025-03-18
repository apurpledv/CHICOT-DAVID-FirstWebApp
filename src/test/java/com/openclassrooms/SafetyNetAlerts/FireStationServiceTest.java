package com.openclassrooms.SafetyNetAlerts;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
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

import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.model.Mapper;
import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.model.PersonFromHouseholdDTO;
import com.openclassrooms.SafetyNetAlerts.model.PersonFromStationNumberDTO;
import com.openclassrooms.SafetyNetAlerts.repository.FireStationRepository;
import com.openclassrooms.SafetyNetAlerts.repository.PersonRepository;
import com.openclassrooms.SafetyNetAlerts.service.FireStationService;

@SpringBootTest
@AutoConfigureMockMvc
public class FireStationServiceTest {
	@Autowired
	FireStationService service;

	@MockitoBean
	private FireStationRepository FireRepo;
	
	@MockitoBean
	private PersonRepository PersonRepo;
	
	@MockitoBean
	private Mapper mapper;
	
	private Person FakePerson;
	
	@BeforeEach 
    void initEachTest() {
		// PersonsRepo Mock
		List<Person> FakePersonsList = new ArrayList<Person>();
		FakePerson = new Person();
		
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
		
		FireStation FakeStation2 = new FireStation();
		FakeStation2.setAddress("Doda");
		FakeStation2.setStation("3");
		FakeStationsList.add(FakeStation2);

		// 'When->ThenReturn'
		when(PersonRepo.getPersons()).thenReturn(FakePersonsList);
		when(FireRepo.getFireStations()).thenReturn(FakeStationsList);
	}
	
	@Test
	void testGetStationsAsService() throws Exception {
		service.getFireStations();
		verify(FireRepo, Mockito.times(1)).getFireStations();
	}
	
	@Test
	void testAddStationAsService() throws Exception {
		service.addFireStation(new FireStation());
		verify(FireRepo, Mockito.times(1)).addFireStation(any(FireStation.class));
	}
	
	@Test
	void testModifyStationAsService() throws Exception {
		service.modifyFireStation(new FireStation());
		verify(FireRepo, Mockito.times(1)).modifyFireStation(any(FireStation.class));
	}
	
	@Test
	void testDeleteStationAsService() throws Exception {
		service.deleteFireStation("Do", "1");
		verify(FireRepo, Mockito.times(1)).deleteFireStation(any(String.class), any(String.class));
	}
	
	@Test
	void testGetPersonDTOFromAddressAsService() throws Exception {
		when(FireRepo.getFireStationNumberFromAddress(any(String.class))).thenReturn("1");
		
		service.getPersonDTOFromAddress("Do");
		
		verify(PersonRepo, Mockito.times(1)).getPersons();
		verify(FireRepo, Mockito.times(1)).getFireStationNumberFromAddress(any(String.class));
		verify(mapper, Mockito.times(1)).toPersonDataFromAddressDto(any(Person.class), any(String.class));
	}
	
	@Test
	void testGetPersonDTOFromAddressAsServiceNonValid() throws Exception {
		when(FireRepo.getFireStationNumberFromAddress(any(String.class))).thenReturn("1");
		
		service.getPersonDTOFromAddress("Dooooooooooooooo");
		
		verify(PersonRepo, Mockito.times(1)).getPersons();
		verify(FireRepo, Mockito.times(1)).getFireStationNumberFromAddress(any(String.class));
		
		// No person should be found at this address, thus this method should be called 0 times
		verify(mapper, Mockito.times(0)).toPersonDataFromAddressDto(any(Person.class), any(String.class));
	}
	
	@Test
	void testGetHouseholdDTOFromStationsAsService() throws Exception {
		// Mocking the station address return values depending on the station numbers provided
		List<String> FakeAddressesS1 = new ArrayList<String>();
		FakeAddressesS1.add("Do");
		
		List<String> FakeAddressesS2 = new ArrayList<String>();
		FakeAddressesS2.add("Doda");
		
		// Mocking the 'getPersonsFromAddress' to return our 'FakePerson'
		List<Person> FakePersonFromAddressList = new ArrayList<Person>();
		FakePersonFromAddressList.add(FakePerson);
		
		// Mocking the 'toPersonFromHouseholdDTO' to return a fake person DTO
		List<PersonFromHouseholdDTO> FakePersonsDTOList = new ArrayList<PersonFromHouseholdDTO>();
		PersonFromHouseholdDTO FakePersonDTO = new PersonFromHouseholdDTO(
			"Daddy", "0101010101", 14, new ArrayList<String>(), new ArrayList<String>()
		);
		FakePersonsDTOList.add(FakePersonDTO);
		
		// 'Whens'
		when(FireRepo.getFireStationAddressesFromStationNumber(eq("1"))).thenReturn(FakeAddressesS1);
		when(FireRepo.getFireStationAddressesFromStationNumber(eq("3"))).thenReturn(FakeAddressesS2);
		when(PersonRepo.getPersonsFromAddress(eq("Do"))).thenReturn(FakePersonFromAddressList);
		when(mapper.toPersonFromHouseholdDto(any(Person.class))).thenReturn(FakePersonDTO);
		
		
		// Start of the test
		List<String> StationNumberList = new ArrayList<String>();
		StationNumberList.add("1");
		StationNumberList.add("3");
		
		service.getHouseholdDTOFromStations(StationNumberList);
		
		verify(FireRepo, Mockito.times(2)).getFireStationAddressesFromStationNumber(any(String.class));
		verify(PersonRepo, Mockito.times(2)).getPersonsFromAddress(any(String.class));
		verify(mapper, Mockito.times(1)).toPersonFromHouseholdDto(any(Person.class));
		verify(mapper, Mockito.times(1)).toHouseholdFromStationsDto(any(String.class), eq(FakePersonsDTOList));
	}
	
	@Test
	void testGetHouseholdDTOFromStationsAsServiceNonValid() throws Exception {
		// No mocks since the station numbers aren't valid
		
		// 'Whens'
		when(FireRepo.getFireStationAddressesFromStationNumber(eq("99"))).thenReturn(new ArrayList<String>());
		when(mapper.toPersonFromHouseholdDto(any(Person.class))).thenReturn(null);
		
		
		// Start of the test
		List<String> BadStationNumberList = new ArrayList<String>();
		BadStationNumberList.add("999");
		
		service.getHouseholdDTOFromStations(BadStationNumberList);
		
		verify(FireRepo, Mockito.times(1)).getFireStationAddressesFromStationNumber(any(String.class));
		verify(PersonRepo, Mockito.times(0)).getPersonsFromAddress(any(String.class));
		verify(mapper, Mockito.times(0)).toPersonFromHouseholdDto(any(Person.class));
	}
	
	@Test
	void testGetPersonDTOFromStationNumberAsService() throws Exception {
		// Mocking the station address return values depending on the station numbers provided
		List<String> FakeAddresses = new ArrayList<String>();
		FakeAddresses.add("Do");
		
		// Mocking the 'getPersonsFromAddress' to return our 'FakePerson'
		List<Person> FakePersonFromAddressList = new ArrayList<Person>();
		FakePersonFromAddressList.add(FakePerson);
				
		// Mocking the 'toPersonFromStationDto' to return a list of fake person DTOs
		List<PersonFromStationNumberDTO> FakePersonsDTOList = new ArrayList<PersonFromStationNumberDTO>();
		PersonFromStationNumberDTO FakePersonDTO = new PersonFromStationNumberDTO(
			"Daddy", "Daddy", "Do", "0101010101"
		);
		FakePersonsDTOList.add(FakePersonDTO);
		
		// 'Whens'
		when(FireRepo.getFireStationAddressesFromStationNumber(eq("1"))).thenReturn(FakeAddresses);
		when(PersonRepo.getPersonsFromAddress(eq("Do"))).thenReturn(FakePersonFromAddressList);
		when(mapper.toPersonFromStationDto(any(Person.class))).thenReturn(FakePersonDTO);
		
		
		// Start of the test		
		service.getPersonDTOFromStationNumber("1");
		
		verify(FireRepo, Mockito.times(1)).getFireStationAddressesFromStationNumber(any(String.class));
		verify(PersonRepo, Mockito.times(1)).getPersonsFromAddress(any(String.class));
		verify(mapper, Mockito.times(1)).toPersonFromStationDto(any(Person.class));
		verify(mapper, Mockito.times(1)).toPersonsDataFromStationNumberDto(any(int.class), any(int.class), eq(FakePersonsDTOList));
	}
	
	@Test
	void testGetPersonDTOFromStationNumberAsServiceNonValid() throws Exception {
		// No mocks since the station numbers aren't valid
		
		// 'Whens'
		when(FireRepo.getFireStationAddressesFromStationNumber(eq("100"))).thenReturn(new ArrayList<String>());
		when(mapper.toPersonFromStationDto(any(Person.class))).thenReturn(null);
		
		
		// Start of the test		
		service.getPersonDTOFromStationNumber("100");
		
		verify(FireRepo, Mockito.times(1)).getFireStationAddressesFromStationNumber(any(String.class));
		verify(PersonRepo, Mockito.times(0)).getPersonsFromAddress(any(String.class));
		verify(mapper, Mockito.times(0)).toPersonFromStationDto(any(Person.class));
	}
}
