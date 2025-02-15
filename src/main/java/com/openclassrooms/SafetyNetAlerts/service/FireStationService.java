package com.openclassrooms.SafetyNetAlerts.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.repository.FireStationRepository;

import lombok.Data;

@Data
@Service
public class FireStationService {
	@Autowired
	private FireStationRepository repo;
	
	public List<FireStation> getFireStations() {
		return repo.getFireStations();
	}
	
	public void addFireStation(FireStation station) {
		repo.addFireStation(station);
	}
	
	public void modifyFireStation(FireStation station) {
		repo.modifyFireStation(station);
	}
	
	public void deleteFireStation(FireStation station) {
		repo.deleteFireStation(station);
	}
}
