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

/**
 * FireStationService is an entity that handles the business work linked to Fire Stations
 */
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
	
	/**
	 * Will manually order the loading of data from the default file from its repository
	 */
	public void initRepo() {
		FireRepo.initRepo();
	}
	
	/**
	 * Will manually order the loading of data from a custom file from its repository
	 */
	public void initRepo(String fileName) {
		FireRepo.initRepo(fileName);
	}
	
	/**
	 * <p>Will return a List of every FireStation Object registered</p>
	 * @return a List of FireStation Objects
	 */
	public List<FireStation> getFireStations() throws Exception {
		return FireRepo.getFireStations();
	}
	
	/**
	 * <p>Will return a FireStation Object registered with that address and station number</p>
	 * @param address of the FireStation to find
	 * @param station number of the FireStation to find
	 * @return a FireStation Object
	 */
	public FireStation getFireStation(String address, String station) {
		return FireRepo.getFireStation(address, station);
	}
	
	/**
	 * Will attempt to add a FireStation Object to the Application
	 * @param station the FireStation Object to add
	 * @return true if successful; false otherwise
	 * @throws Exception if a station with the same address and station number already exists, will throw an Exception
	 */
	public boolean addFireStation(FireStation station) throws Exception {
		return FireRepo.addFireStation(station);
	}
	
	/**
	 * <p>Will attempt to modify an already existing FireStation Object</p>
	 * @param station a FireStation Object containing the new attributes
	 * @return true if successful; false no station is found
	 */
	public boolean modifyFireStation(FireStation station) {
		return FireRepo.modifyFireStation(station);
	}
	
	/**
	 * <p>Will attempt to delete an already existing FireStation Object</p>
	 * @param address of the FireStation to delete
	 * @param station number of the FireStation to delete
	 * @return true if successful; false no station is found
	 */
	public boolean deleteFireStation(String address, String station) {
		return FireRepo.deleteFireStation(address, station);
	}
	
	/**
	 * <p>Will return a List of DTOs giving information about the people living at a certain address (ie: which station covers them, their medical records, their age...)</p>
	 * @param address to parse from
	 * @return a List of DTOs containing [lastName, phone, age, stationNumber, medication, allergies]
	 * @throws Exception if at any point, a Person doesn't have a MedicalRecord bound to them (==null), will throw an Exception
	 */
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
	
	/**
	 * <p>Will return a DTO giving information about the people covered by a specific station</p>
	 * @param stationNumber of the station to parse from
	 * @return a DTO containing [adults, children, persons], with persons being a DTO containing [firstName, lastName, address, phone]
	 * @throws Exception if at any point, a Person doesn't have a MedicalRecord bound to them (==null), will throw an Exception
	 */
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
	
	/**
	 * <p>Will return a List of DTOs giving information about different households (ie: people living at one address), who are covered by different stations</p>
	 * @param stations a List of stations (their stationNumber) to parse from
	 * @return a List of DTOs containing [address, occupants], with occupants being DTOs containing [lastName, phone, age, medication, allergies]
	 * @throws Exception if at any point, a Person doesn't have a MedicalRecord bound to them (==null), will throw an Exception
	 */
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
