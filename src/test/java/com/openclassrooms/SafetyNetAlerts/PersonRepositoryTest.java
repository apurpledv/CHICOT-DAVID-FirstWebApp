package com.openclassrooms.SafetyNetAlerts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.repository.PersonRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonRepositoryTest {
	@Autowired
	private PersonRepository PersonRepo;
	
	@BeforeEach
	void initTests() {
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
	void testAddPersonAsRepo() {
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
		
		PersonRepo.addPerson(NewPerson);
		
		assertFalse(PersonRepo.getPerson("Daddy", "Daddy") == null);
	}
	
	@Test
	void testModifyPersonAsRepo() {
		Person PersonToModify = new Person();
		
		PersonToModify.setFirstName("John");
		PersonToModify.setLastName("Boyd");
		PersonToModify.setAddress("1 Rue de Tatooine");
		
		PersonRepo.modifyPerson(PersonToModify);
		
		assertEquals("1 Rue de Tatooine", PersonRepo.getPerson("John", "Boyd").getAddress());
	}

	@Test
	void testDeletePersonAsRepo() {
		PersonRepo.deletePerson("John", "Boyd");
		
		assertEquals(null, PersonRepo.getPerson("John", "Boyd"));
		assertFalse(PersonRepo.getPerson("Jacob", "Boyd") == null);
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
