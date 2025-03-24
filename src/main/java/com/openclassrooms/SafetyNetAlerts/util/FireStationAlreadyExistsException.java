package com.openclassrooms.SafetyNetAlerts.util;

/**
 * FireStationAlreadyExistsException is thrown if a user attempts to add a new FireStation with the same address and station number
 */
public class FireStationAlreadyExistsException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2410967097452415323L;

	public FireStationAlreadyExistsException() {
		super();
	}
	
	public FireStationAlreadyExistsException(String errorMessage) {
		super(errorMessage);
	}
}
