package com.openclassrooms.SafetyNetAlerts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.SafetyNetAlerts.model.ChildDataFromAddressDTO;
import com.openclassrooms.SafetyNetAlerts.model.HouseholdFromStationsDTO;
import com.openclassrooms.SafetyNetAlerts.model.Mapper;
import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.model.PersonDataFromAddressDTO;
import com.openclassrooms.SafetyNetAlerts.model.PersonDataFromLastNameDTO;
import com.openclassrooms.SafetyNetAlerts.model.PersonFromHouseholdDTO;
import com.openclassrooms.SafetyNetAlerts.model.PersonFromStationNumberDTO;
import com.openclassrooms.SafetyNetAlerts.model.PersonsDataFromStationDTO;
import com.openclassrooms.SafetyNetAlerts.util.MedicalRecordNotFoundException;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MapperTest {
	@Autowired
	Mapper mapper;
	
	private Person MockPerson;
	
	@BeforeEach
	public void initTests() {
		// Mock a person, and their medical record
		MockPerson = new Person();
		MockPerson.setFirstName("Bobby");
		MockPerson.setLastName("Bobby");
		MockPerson.setAddress("1 Road of Bobby");
		MockPerson.setEmail("bobby@bobby.com");
		MockPerson.setPhone("0101");
		
		MedicalRecord MockRecord = new MedicalRecord();
		MockRecord.setBirthdate("01/01/2009");	// Age is 16
		MockRecord.setMedications(new ArrayList<String>());	// Nothing
		MockRecord.setAllergies(new ArrayList<String>());	// Nothing
		
		MockPerson.setRecord(MockRecord);	// Attach this record to the mocked person
	}
	
	@Test
	public void testToPersonDataFromAddressDTO() throws Exception {
		String StationNumber = "1";
		
		// Test begins
		PersonDataFromAddressDTO Result = mapper.toPersonDataFromAddressDto(MockPerson, StationNumber);
		
		assertEquals("Bobby", Result.getLastName());
		assertEquals("0101", Result.getPhone());
		assertEquals(16, Result.getAge());
		assertEquals("1", Result.getStationNumber());
		assertTrue(Result.getMedication().isEmpty());
		assertTrue(Result.getAllergies().isEmpty());
	}
	
	@Test
	public void testToPersonDataFromAddressDTONonValid() throws Exception {
		String StationNumber = "1";
		
		// Delete the Record (will throw an exception)
		MockPerson.setRecord(null);
		
		// Test begins
		assertThrows(MedicalRecordNotFoundException.class, () -> mapper.toPersonDataFromAddressDto(MockPerson, StationNumber));
	}
	
	@Test
	public void testToPersonDataFromLastNameDTO() throws Exception {
		// Test begins
		PersonDataFromLastNameDTO Result = mapper.toPersonDataFromLastNameDto(MockPerson);
		
		assertEquals("Bobby", Result.getLastName());
		assertEquals("1 Road of Bobby", Result.getAddress());
		assertEquals(16, Result.getAge());
		assertEquals("bobby@bobby.com", Result.getEmail());
		assertTrue(Result.getMedication().isEmpty());
		assertTrue(Result.getAllergies().isEmpty());
	}
	
	@Test
	public void testToPersonDataFromLastNameDTONonValid() throws Exception {
		// Delete the Record (will throw an exception)
		MockPerson.setRecord(null);
		
		// Test begins
		assertThrows(MedicalRecordNotFoundException.class, () -> mapper.toPersonDataFromLastNameDto(MockPerson));
	}
	
	@Test
	public void testToChildDataFromAddressDTO() throws Exception {
		List<Person> OtherPersons = new ArrayList<Person>();
		Person OtherPerson = new Person();
		OtherPerson.setLastName("Kelly");
		
		OtherPersons.add(OtherPerson);
		
		// Test begins
		ChildDataFromAddressDTO Result = mapper.toChildDataFromAddressDto(MockPerson, OtherPersons);
		
		assertEquals("Bobby", Result.getFirstName());
		assertEquals("Bobby", Result.getLastName());
		assertEquals(16, Result.getAge());
		
		// 'OtherMembers' now has one person, with the last name 'Kelly'
		assertFalse(Result.getOtherMembers().isEmpty());
		assertEquals("Kelly", Result.getOtherMembers().get(0).getLastName());
	}
	
	@Test
	public void testToChildDataFromAddressDTONonValid() throws Exception {
		// Delete the Record (will throw an exception)
		MockPerson.setRecord(null);
		
		// Test begins
		assertThrows(MedicalRecordNotFoundException.class, () -> mapper.toChildDataFromAddressDto(MockPerson, new ArrayList<Person>()));
	}
	
	@Test
	public void testToPersonFromHouseholdDTO() throws Exception {
		// Test begins
		PersonFromHouseholdDTO Result = mapper.toPersonFromHouseholdDto(MockPerson);
		
		assertEquals("Bobby", Result.getLastName());
		assertEquals("0101", Result.getPhone());
		assertEquals(16, Result.getAge());
		assertTrue(Result.getMedication().isEmpty());
		assertTrue(Result.getAllergies().isEmpty());
	}
	
	@Test
	public void testToPersonFromHouseholdDTONonValid() throws Exception {
		// Delete the Record (will throw an exception)
		MockPerson.setRecord(null);
		
		// Test begins
		assertThrows(MedicalRecordNotFoundException.class, () -> mapper.toPersonFromHouseholdDto(MockPerson));
	}
	
	@Test
	public void testToHouseholdFromStationsDTO() throws Exception {
		String Address = "1 Road of Bobby";
		
		List<PersonFromHouseholdDTO> PersonsList = new ArrayList<PersonFromHouseholdDTO>();
		PersonFromHouseholdDTO PersonFromHH = mapper.toPersonFromHouseholdDto(MockPerson);
		PersonsList.add(PersonFromHH);
		
		// Test begins
		HouseholdFromStationsDTO Result = mapper.toHouseholdFromStationsDto(Address, PersonsList);
		
		assertEquals("1 Road of Bobby", Result.getAddress());
		assertFalse(Result.getOccupants().isEmpty());
		
		PersonFromHouseholdDTO FirstOccupant = Result.getOccupants().get(0);
		assertEquals("Bobby", FirstOccupant.getLastName());
		assertEquals("0101", FirstOccupant.getPhone());
		assertEquals(16, FirstOccupant.getAge());
		assertTrue(FirstOccupant.getMedication().isEmpty());
		assertTrue(FirstOccupant.getAllergies().isEmpty());
	}
	
	@Test
	public void testToPersonFromStationDTO() throws Exception {
		// Test begins
		PersonFromStationNumberDTO Result = mapper.toPersonFromStationDto(MockPerson);

		assertEquals("Bobby", Result.getFirstName());
		assertEquals("Bobby", Result.getLastName());
		assertEquals("1 Road of Bobby", Result.getAddress());
		assertEquals("0101", Result.getPhone());
	}
	
	@Test
	public void testToPersonsDataFromStationNumberDTO() throws Exception {
		int Adults = 0;
		int Children = 1;
		
		List<PersonFromStationNumberDTO> PersonsList = new ArrayList<PersonFromStationNumberDTO>();
		PersonFromStationNumberDTO PersonFromStation = mapper.toPersonFromStationDto(MockPerson);
		PersonsList.add(PersonFromStation);
		
		// Test begins
		PersonsDataFromStationDTO Result = mapper.toPersonsDataFromStationNumberDto(Adults, Children, PersonsList);

		assertEquals(0, Result.getAdults());
		assertEquals(1, Result.getChildren());
		assertFalse(Result.getPersons().isEmpty());
		
		PersonFromStationNumberDTO FirstPerson = Result.getPersons().get(0);
		assertEquals("Bobby", FirstPerson.getFirstName());
		assertEquals("Bobby", FirstPerson.getLastName());
		assertEquals("1 Road of Bobby", FirstPerson.getAddress());
		assertEquals("0101", FirstPerson.getPhone());
	}
}
