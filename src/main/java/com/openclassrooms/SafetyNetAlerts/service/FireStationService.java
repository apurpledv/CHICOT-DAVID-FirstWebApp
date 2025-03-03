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

@Data
@Service
public class FireStationService {
	@Autowired
	private PersonRepository PersonRepo;
	
	@Autowired
	private FireStationRepository FireRepo;

	@Autowired
	Mapper DTOmapper;
	
	public List<FireStation> getFireStations() {
		return FireRepo.getFireStations();
	}
	
	public void addFireStation(FireStation station) {
		FireRepo.addFireStation(station);
	}
	
	public void modifyFireStation(FireStation station) {
		FireRepo.modifyFireStation(station);
	}
	
	public void deleteFireStation(String address, String station) {
		FireRepo.deleteFireStation(address, station);
	}
	
	public List<PersonDataFromAddressDTO> getPersonDTOFromAddress(String address) {
		List<PersonDataFromAddressDTO> ListDTO = new ArrayList<PersonDataFromAddressDTO>();
		
		List<Person> personsList = PersonRepo.getPersons();
		String stationNumber = FireRepo.getFireStationNumberFromAddress(address);
		
		for (Person person : personsList) {
		    if (person.getAddress().equals(address)) {
		    	ListDTO.add(DTOmapper.toPersonDataFromAddressDto(person, stationNumber));
		    }
		}
		
		return ListDTO;
	}
	
	public List<HouseholdFromStationsDTO> getHouseholdDTOFromStations(List<String> stations) {
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
		return ListDTO;	
	}
	
	public PersonsDataFromStationDTO getPersonDTOFromStationNumber(String stationNumber) {
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
		return DTOmapper.toPersonsDataFromStationNumberDto(String.valueOf(adults), String.valueOf(children), personsDTO);	
	}
}
