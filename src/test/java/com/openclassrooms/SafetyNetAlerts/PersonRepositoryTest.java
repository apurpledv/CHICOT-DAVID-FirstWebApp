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

import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.repository.PersonRepository;
import com.openclassrooms.SafetyNetAlerts.util.PersonAlreadyExistsException;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonRepositoryTest {
	@Autowired
	private PersonRepository PersonRepo;
	
	@BeforeEach
	void initTests() {
		PersonRepo.initRepo();
	}
	
	@AfterAll
	void cleanUp() {
		PersonRepo.initRepo();
	}
	
	@Test
	void testGetPersonsAsRepo() {
		List<Person> PersonsList = PersonRepo.getPersons();
		
		assertFalse(PersonsList.isEmpty());
	}
	
	@Test
	void testGetOnePersonAsRepo() {
		Person PersonFound = PersonRepo.getPerson("John", "Boyd");
		
		assertEquals("John", PersonFound.getFirstName());
		assertEquals("Boyd", PersonFound.getLastName());
	}
	
	@Test
	void testAddPersonAsRepo() throws Exception {
		Person NewPerson = new Person();
		
		NewPerson.setFirstName("Daddy");
		NewPerson.setLastName("Daddy");
		NewPerson.setAddress("Do");
		NewPerson.setPhone("0101010101");
		
		MedicalRecord FakeRecord = new MedicalRecord();
		FakeRecord.setFirstName("Daddy");
		FakeRecord.setLastName("Daddy");
		FakeRecord.setBirthdate("11/11/2012");
		NewPerson.setRecord(FakeRecord);
		
		boolean Result = PersonRepo.addPerson(NewPerson);
		assertTrue(Result);
		
		assertFalse(PersonRepo.getPerson("Daddy", "Daddy") == null);
	}
	
	@Test
	void testAddPersonAsRepoNonValid() throws Exception {
		Person A = new Person();
		A.setFirstName("A");
		A.setLastName("A");
		
		Person B = new Person();
		B.setFirstName("A");
		B.setLastName("A");
		
		PersonRepo.addPerson(A);
		
		// Test: Should throw an exception (attempting to add a person that already exists; same first and last name)
		assertThrows(PersonAlreadyExistsException.class, () -> PersonRepo.addPerson(B));
	}
	
	@Test
	void testModifyPersonAsRepo() throws Exception {
		Person PersonToModify = new Person();
		
		PersonToModify.setFirstName("John");
		PersonToModify.setLastName("Boyd");
		PersonToModify.setAddress("1 Rue de Tatooine");
		
		boolean Result = PersonRepo.modifyPerson(PersonToModify);
		assertTrue(Result);
		
		assertEquals("1 Rue de Tatooine", PersonRepo.getPerson("John", "Boyd").getAddress());
	}
	
	@Test
	void testModifyPersonAsRepoNonValid() throws Exception {
		// Person not in list
		Person PersonToModify = new Person();
		PersonToModify.setFirstName("Kela");
		PersonToModify.setLastName("De Thaym");
		PersonToModify.setAddress("1 Rue de Tatooine");
		
		boolean Result = PersonRepo.modifyPerson(PersonToModify);
		assertFalse(Result);
	}

	@Test
	void testDeletePersonAsRepo() throws Exception {
		boolean Result = PersonRepo.deletePerson("John", "Boyd");
		assertTrue(Result);
		
		assertEquals(null, PersonRepo.getPerson("John", "Boyd"));
		
		// This one still exists, thus it shouldn't be null
		assertFalse(PersonRepo.getPerson("Jacob", "Boyd") == null);
	}
	
	@Test
	void testDeletePersonAsRepoNonValid() throws Exception {
		boolean Result = PersonRepo.deletePerson("", "");
		assertFalse(Result);
	}
	
	@Test
	void testGetPersonEmailsFromCityAsRepo() {
		List<String> EmailsList = PersonRepo.getPersonEmailsFromCity("Culver");
		
		assertFalse(EmailsList.isEmpty());
	}
	
	@Test
	void testGetPersonEmailsFromCityAsRepoNonValid() {
		List<String> BadEmailsList = PersonRepo.getPersonEmailsFromCity("CulverRitano");
		
		assertTrue(BadEmailsList.isEmpty());
	}
	
	@Test
	void testGetPersonsFromAddressAsRepo() {
		List<Person> PersonsFromAddressList = PersonRepo.getPersonsFromAddress("1509 Culver St");
		
		assertFalse(PersonsFromAddressList.isEmpty());
	}
	
	@Test
	void testGetPersonsFromAddressAsRepoNonValid() {
		List<Person> BadPersonsFromAddressList = PersonRepo.getPersonsFromAddress("Tututututuutututututuu");
		
		assertTrue(BadPersonsFromAddressList.isEmpty());
	}
}
