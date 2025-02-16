package com.openclassrooms.SafetyNetAlerts.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.model.Mapper;
import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.model.PersonDataFromAddressDTO;
import com.openclassrooms.SafetyNetAlerts.repository.FireStationRepository;
import com.openclassrooms.SafetyNetAlerts.repository.PersonRepository;

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
	
	public void deleteFireStation(FireStation station) {
		FireRepo.deleteFireStation(station);
	}
	
	public List<PersonDataFromAddressDTO> getPersonDTOFromAddress(String address) {
		List<PersonDataFromAddressDTO> ListDTO = new ArrayList<PersonDataFromAddressDTO>();
		
		List<Person> personsList = PersonRepo.getPersons();
		String stationNumber = FireRepo.getFireStationNumberFromAddress(address);
		
		for (Person person : personsList) {
		    if (person.getAddress().equals(address)) {
		    	ListDTO.add(DTOmapper.toDTO(address, person, person.getRecord(), stationNumber));
		    }
		}
		
		return ListDTO;
		
	}
}
