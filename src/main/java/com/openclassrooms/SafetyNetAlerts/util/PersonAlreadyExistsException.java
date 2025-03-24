package com.openclassrooms.SafetyNetAlerts.util;

/**
 * PersonAlreadyExistsException is thrown  if a user attempts to add a new Person with the same first and last names
 */
public class PersonAlreadyExistsException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9000487713878916848L;

	public PersonAlreadyExistsException() {
		super();
	}
	
	public PersonAlreadyExistsException(String errorMessage) {
		super(errorMessage);
	}
}
