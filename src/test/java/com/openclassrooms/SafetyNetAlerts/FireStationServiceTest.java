package com.openclassrooms.SafetyNetAlerts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import com.openclassrooms.SafetyNetAlerts.model.HouseholdFromStationsDTO;
import com.openclassrooms.SafetyNetAlerts.model.Mapper;
import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.model.PersonDataFromAddressDTO;
import com.openclassrooms.SafetyNetAlerts.model.PersonFromHouseholdDTO;
import com.openclassrooms.SafetyNetAlerts.model.PersonFromStationNumberDTO;
import com.openclassrooms.SafetyNetAlerts.model.PersonsDataFromStationDTO;
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
		List<FireStation> StationsList = service.getFireStations();
		
		assertFalse(StationsList.isEmpty());
		assertEquals(2, StationsList.size());
		assertEquals("Do", StationsList.get(0).getAddress());
		assertEquals("1", StationsList.get(0).getStation());
	}
	
	@Test
	void testAddStationAsService() throws Exception {
		service.addFireStation(new FireStation());
		verify(FireRepo, Mockito.times(1)).addFireStation(any(FireStation.class));
	}
	
	@Test
	void testModifyStationAsService(){
		service.modifyFireStation(new FireStation());
		verify(FireRepo, Mockito.times(1)).modifyFireStation(any(FireStation.class));
	}
	
	@Test
	void testDeleteStationAsService() {
		service.deleteFireStation("Do", "1");
		verify(FireRepo, Mockito.times(1)).deleteFireStation(any(String.class), any(String.class));
	}
	
	@Test
	void testGetPersonDTOFromAddressAsService() throws Exception {
		PersonDataFromAddressDTO FakeDTO = new PersonDataFromAddressDTO("Daddy", null, 20, "1", null, null);
		
		when(FireRepo.getFireStationNumberFromAddress(any(String.class))).thenReturn("1");
		when(mapper.toPersonDataFromAddressDto(any(Person.class), any(String.class))).thenReturn(FakeDTO);
		
		List<PersonDataFromAddressDTO> DTOList = service.getPersonDTOFromAddress("Do");
		
		assertFalse(DTOList.isEmpty());
		assertEquals("Daddy", DTOList.get(0).getLastName());
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
		
		// Mocking the 'toHouseholdFromStationsDto' to return a fake household DTO
		HouseholdFromStationsDTO FakeHouseholdDTO = new HouseholdFromStationsDTO("Do", FakePersonsDTOList);
		
		// 'Whens'
		when(FireRepo.getFireStationAddressesFromStationNumber(eq("1"))).thenReturn(FakeAddressesS1);
		when(FireRepo.getFireStationAddressesFromStationNumber(eq("3"))).thenReturn(FakeAddressesS2);
		when(PersonRepo.getPersonsFromAddress(eq("Do"))).thenReturn(FakePersonFromAddressList);
		when(mapper.toPersonFromHouseholdDto(any(Person.class))).thenReturn(FakePersonDTO);
		when(mapper.toHouseholdFromStationsDto(any(String.class), eq(FakePersonsDTOList))).thenReturn(FakeHouseholdDTO);
		
		
		// Start of the test
		List<String> StationNumberList = new ArrayList<String>();
		StationNumberList.add("1");
		StationNumberList.add("3");
		
		List<HouseholdFromStationsDTO> HouseholdsList = service.getHouseholdDTOFromStations(StationNumberList);
		assertFalse(HouseholdsList.isEmpty());
		
		PersonFromHouseholdDTO FirstOccupant = HouseholdsList.get(0).getOccupants().get(0);
		assertEquals("Daddy", FirstOccupant.getLastName());
		assertEquals("0101010101", FirstOccupant.getPhone());
		assertEquals(14, FirstOccupant.getAge());
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
		
		// Mocking the 'toPersonsDataFromStationNumberDto' to return a fake DTO
		PersonsDataFromStationDTO FakePersonsDataDTO = new PersonsDataFromStationDTO(1, 0, FakePersonsDTOList);
		
		// 'Whens'
		when(FireRepo.getFireStationAddressesFromStationNumber(eq("1"))).thenReturn(FakeAddresses);
		when(PersonRepo.getPersonsFromAddress(eq("Do"))).thenReturn(FakePersonFromAddressList);
		when(mapper.toPersonFromStationDto(any(Person.class))).thenReturn(FakePersonDTO);
		when(mapper.toPersonsDataFromStationNumberDto(any(int.class), any(int.class), eq(FakePersonsDTOList))).thenReturn(FakePersonsDataDTO);
		
		
		// Start of the test		
		PersonsDataFromStationDTO PersonsDataDTO = service.getPersonDTOFromStationNumber("1");

		assertFalse(PersonsDataDTO == null);
		assertEquals(1, PersonsDataDTO.getAdults());
		assertEquals(0, PersonsDataDTO.getChildren());
		
		PersonFromStationNumberDTO FirstPerson = PersonsDataDTO.getPersons().get(0);
		assertEquals("Daddy", FirstPerson.getFirstName());
		assertEquals("Daddy", FirstPerson.getLastName());
		assertEquals("Do", FirstPerson.getAddress());
		assertEquals("0101010101", FirstPerson.getPhone());
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
