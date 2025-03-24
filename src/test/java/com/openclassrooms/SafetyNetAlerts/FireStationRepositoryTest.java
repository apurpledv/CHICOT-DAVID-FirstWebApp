package com.openclassrooms.SafetyNetAlerts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.repository.FireStationRepository;
import com.openclassrooms.SafetyNetAlerts.util.FireStationAlreadyExistsException;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FireStationRepositoryTest {
	@Autowired
	private FireStationRepository FireStationRepo;
	
	@BeforeEach
	void initTests() {
		FireStationRepo.initRepo();
	}
	
	@AfterAll
	void cleanUp() {
		FireStationRepo.initRepo();
	}
	
	@Test
	void testGetStationsAsRepo() {
		List<FireStation> StationList = FireStationRepo.getFireStations();
		
		assertFalse(StationList.isEmpty());
	}
	
	@Test
	void testGetOneStationAsRepo() {
		FireStation StationFound = FireStationRepo.getFireStation("489 Manchester St", "4");
		
		assertFalse(StationFound == null);
	}
	
	@Test
	void testGetOneStationAsRepoNonValid() {
		FireStation StationFound = FireStationRepo.getFireStation("489 Manchester St", "75745");
		
		assertTrue(StationFound == null);
	}
	
	@Test
	void testAddStationAsRepo() throws Exception {
		FireStation NewStation = new FireStation();
		NewStation.setAddress("Do");
		NewStation.setStation("1");
		FireStationRepo.addFireStation(NewStation);
		
		assertFalse(FireStationRepo.getFireStation("Do", "1") == null);
		// We make sure any other station (same address, different station number) doesn't exist, just the one we added
		assertTrue(FireStationRepo.getFireStation("Do", "172575") == null);
	}
	
	@Test
	void testAddStationAsRepoNonValid() throws Exception {
		FireStation A = new FireStation();
		A.setAddress("A");
		A.setStation("A");
		
		FireStation B = new FireStation();
		B.setAddress("A");
		B.setStation("A");
		
		FireStationRepo.addFireStation(A);

		assertThrows(FireStationAlreadyExistsException.class, () -> FireStationRepo.addFireStation(B));
	}
	
	@Test
	void testModifyStationAsRepo() {
		FireStation StationToModify = new FireStation();
		StationToModify.setAddress("489 Manchester St");
		StationToModify.setStation("1");
		
		boolean Result = FireStationRepo.modifyFireStation(StationToModify);
		assertTrue(Result);
		
		// The original one, "489 Manchester St, Station=4"
		assertTrue(FireStationRepo.getFireStation("489 Manchester St", "4") == null);
		// The new one, "489 Manchester St, Station=1"
		assertFalse(FireStationRepo.getFireStation("489 Manchester St", "1") == null);
	}
	
	@Test
	void testModifyStationAsRepoNonValid() {
		FireStation StationToModify = new FireStation();
		StationToModify.setAddress("");
		
		boolean Result = FireStationRepo.modifyFireStation(StationToModify);
		assertFalse(Result);
	}
	
	@Test
	void testDeleteStationAsRepo() {
		boolean Result = FireStationRepo.deleteFireStation("489 Manchester St", "4");
		assertTrue(Result);

		// This station shouldn't exist anymore, and thus should return 'null'
		assertTrue(FireStationRepo.getFireStation("489 Manchester St", "4") == null);
	}
	
	@Test
	void testDeleteStationAsRepoNonValid() {
		boolean Result = FireStationRepo.deleteFireStation("", "");
		assertFalse(Result);
	}
	
	@Test
	void testGetFireStationNumberFromAddressAsRepo() {
		String StationNumber = FireStationRepo.getFireStationNumberFromAddress("489 Manchester St");

		assertEquals("4", StationNumber);
	}
	
	@Test
	void testGetFireStationNumberFromAddressAsRepoNonValid() {
		String BadStationNumber = FireStationRepo.getFireStationNumberFromAddress("489 Manchester Stititititi");

		assertEquals("-1", BadStationNumber);
	}
	
	@Test
	void testGetFireStationAddressesFromStationNumberAsRepo() {
		List<String> ListOfStations = FireStationRepo.getFireStationAddressesFromStationNumber("1");

		assertFalse(ListOfStations.isEmpty());
	}
	
	@Test
	void testGetFireStationAddressesFromStationNumberAsRepoNonValid() {
		List<String> ListOfBadStations = FireStationRepo.getFireStationAddressesFromStationNumber("120");

		assertTrue(ListOfBadStations.isEmpty());
	}
}
