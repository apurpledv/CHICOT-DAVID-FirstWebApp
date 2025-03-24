package com.openclassrooms.SafetyNetAlerts.util;

/**
 * MedicalRecordAlreadyExistsException is thrown if a user attempts to add a new MedicalRecord for a Person already covered by another MedicalRecord
 */
public class MedicalRecordAlreadyExistsException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4774853092429375307L;

	public MedicalRecordAlreadyExistsException() {
		super();
	}
	
	public MedicalRecordAlreadyExistsException(String errorMessage) {
		super(errorMessage);
	}
}
