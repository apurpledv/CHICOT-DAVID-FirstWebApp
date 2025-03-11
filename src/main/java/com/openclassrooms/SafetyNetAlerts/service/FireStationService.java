package com.openclassrooms.SafetyNetAlerts.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.model.HouseholdFromStationsDTO;
import com.openclassrooms.SafetyNetAlerts.model.Mapper;
import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.model.PersonDataFromAddressDTO;
import com.openclassrooms.SafetyNetAlerts.model.PersonFromHouseholdDTO;
import com.openclassrooms.SafetyNetAlerts.model.PersonFromStationNumberDTO;
import com.openclassrooms.SafetyNetAlerts.model.PersonsDataFromStationDTO;
import com.openclassrooms.SafetyNetAlerts.repository.FireStationRepository;
import com.openclassrooms.SafetyNetAlerts.repository.PersonRepository;
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
	
	public List<FireStation> getFireStations() {
		return FireRepo.getFireStations();
	}
	
	public FireStation getFireStation(String address, String station) {
		return FireRepo.getFireStation(address, station);
	}
	
	public void addFireStation(FireStation station) {
		log.debug("Adding Station: " + station);
		FireRepo.addFireStation(station);
	}
	
	public void modifyFireStation(FireStation station) {
		log.debug("Modifying Station: " + station);
		FireRepo.modifyFireStation(station);
	}
	
	public void deleteFireStation(String address, String station) {
		log.debug("Deleting Station at address: " + address);
		FireRepo.deleteFireStation(address, station);
	}
	
	public List<PersonDataFromAddressDTO> getPersonDTOFromAddress(String address) {
		log.debug("Fetching Persons Data From Address: " + address);
		List<PersonDataFromAddressDTO> ListDTO = new ArrayList<PersonDataFromAddressDTO>();
		
		List<Person> personsList = PersonRepo.getPersons();
		String stationNumber = FireRepo.getFireStationNumberFromAddress(address);
		
		for (Person person : personsList) {
		    if (person.getAddress().equals(address)) {
		    	ListDTO.add(DTOmapper.toPersonDataFromAddressDto(person, stationNumber));
		    }
		}
		
		if (ListDTO.isEmpty())
			log.info("No Person found at this address: " + address);
		
		return ListDTO;
	}
	
	public PersonsDataFromStationDTO getPersonDTOFromStationNumber(String stationNumber) {
		log.debug("Fetching Persons Data From Station Number: " + stationNumber);
		
		List<PersonFromStationNumberDTO> personsDTO = new ArrayList<PersonFromStationNumberDTO>();
		List<String> Addresses = new ArrayList<String>();
		
		int adults = 0;
		int children = 0;
		
		Addresses.addAll(FireRepo.getFireStationAddressesFromStationNumber(stationNumber));
		
		for (String address: Addresses) {
			List<Person> personsPerAddress = PersonRepo.getPersonsFromAddress(address);
			for (Person personAtAddress: personsPerAddress) {
				// We count how many adults/children we have in total
				if (SNAUtil.getAge(personAtAddress.getRecord().getBirthdate()) > 18)
					adults++;
				else
					children++;
				
				personsDTO.add(DTOmapper.toPersonFromStationDto(personAtAddress));
			}
		}
		
		if (personsDTO.isEmpty())
			log.info("No Person found that is handled by Station: " + stationNumber);
		
		return DTOmapper.toPersonsDataFromStationNumberDto(String.valueOf(adults), String.valueOf(children), personsDTO);	
	}
	
	public List<HouseholdFromStationsDTO> getHouseholdDTOFromStations(List<String> stations) {
		log.debug("Fetching Data Of Households Handled By Stations: " + stations);
		
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
		
		if (ListDTO.isEmpty())
			log.info("No Households found for Stations: " + stations);
		
		return ListDTO;	
	}
}
