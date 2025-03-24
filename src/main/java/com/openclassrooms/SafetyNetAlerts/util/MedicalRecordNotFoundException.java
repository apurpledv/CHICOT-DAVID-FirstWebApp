package com.openclassrooms.SafetyNetAlerts.util;

/**
 * MedicalRecordNotFoundException is thrown when a Person's record is needed, but doesn't exist (is null)
 */
public class MedicalRecordNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2263490479116847501L;

	public MedicalRecordNotFoundException() {
		super();
	}
	
	public MedicalRecordNotFoundException(String errorMessage) {
		super(errorMessage);
	}
}
