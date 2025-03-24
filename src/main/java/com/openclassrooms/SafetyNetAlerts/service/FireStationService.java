package com.openclassrooms.SafetyNetAlerts.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.openclassrooms.SafetyNetAlerts.util.MedicalRecordNotFoundException;
import com.openclassrooms.SafetyNetAlerts.util.SNAUtil;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Service
public class FireStationService {
	@Autowired
	private PersonRepository PersonRepo;
	
	@Autowired
	private FireStationRepository FireRepo;

	@Autowired
	Mapper DTOmapper;
	
	public void initRepo() {
		FireRepo.initRepo();
	}
	
	public void initRepo(String fileName) {
		FireRepo.initRepo(fileName);
	}
	
	public List<FireStation> getFireStations() throws Exception {
		return FireRepo.getFireStations();
	}
	
	public FireStation getFireStation(String address, String station) {
		return FireRepo.getFireStation(address, station);
	}
	
	public boolean addFireStation(FireStation station) throws Exception {
		return FireRepo.addFireStation(station);
	}
	
	public boolean modifyFireStation(FireStation station) {
		return FireRepo.modifyFireStation(station);
	}
	
	public boolean deleteFireStation(String address, String station) {
		return FireRepo.deleteFireStation(address, station);
	}
	
	public List<PersonDataFromAddressDTO> getPersonDTOFromAddress(String address) throws Exception {
		List<PersonDataFromAddressDTO> ListDTO = new ArrayList<PersonDataFromAddressDTO>();
		
		List<Person> personsList = PersonRepo.getPersons();
		String stationNumber = FireRepo.getFireStationNumberFromAddress(address);
		
		for (Person person : personsList) {
		    if (person.getAddress().equals(address)) {
		    	ListDTO.add(DTOmapper.toPersonDataFromAddressDto(person, stationNumber));
		    }
		}

		log.debug("Number of results: " + ListDTO.size());
		
		return ListDTO;
	}
	
	public PersonsDataFromStationDTO getPersonDTOFromStationNumber(String stationNumber) throws Exception {
		List<PersonFromStationNumberDTO> personsDTO = new ArrayList<PersonFromStationNumberDTO>();
		List<String> Addresses = new ArrayList<String>();
		
		int adults = 0;
		int children = 0;
		
		Addresses.addAll(FireRepo.getFireStationAddressesFromStationNumber(stationNumber));
		
		for (String address: Addresses) {
			List<Person> personsPerAddress = PersonRepo.getPersonsFromAddress(address);
			for (Person personAtAddress: personsPerAddress) {
				MedicalRecord Record = personAtAddress.getRecord();
				if (Record == null)
					throw new MedicalRecordNotFoundException();
				
				// We count how many adults/children we have in total
				if (SNAUtil.getAge(Record.getBirthdate()) > SNAUtil.MAJORITY_AGE)
					adults++;
				else
					children++;
				
				personsDTO.add(DTOmapper.toPersonFromStationDto(personAtAddress));
			}
		}

		log.debug("Number of results: " + personsDTO.size());
		
		return DTOmapper.toPersonsDataFromStationNumberDto(adults, children, personsDTO);	
	}
	
	public List<HouseholdFromStationsDTO> getHouseholdDTOFromStations(List<String> stations) throws Exception {
		List<HouseholdFromStationsDTO> ListDTO = new ArrayList<HouseholdFromStationsDTO>();

		List<String> HouseHoldAddresses = new ArrayList<String>();
		
		for (String stationNumber : stations) {
			HouseHoldAddresses.addAll(FireRepo.getFireStationAddressesFromStationNumber(stationNumber));
		}
		
		for (String address: HouseHoldAddresses) {
			List<Person> personsPerAddress = PersonRepo.getPersonsFromAddress(address);
			List<PersonFromHouseholdDTO> personsDTO = new ArrayList<PersonFromHouseholdDTO>();
			for (Person personAtAddress: personsPerAddress) {
				personsDTO.add(DTOmapper.toPersonFromHouseholdDto(personAtAddress));
			}
			
			ListDTO.add(DTOmapper.toHouseholdFromStationsDto(address, personsDTO));
		}

		log.debug("Number of results: " + ListDTO.size());
		
		return ListDTO;	
	}
}
