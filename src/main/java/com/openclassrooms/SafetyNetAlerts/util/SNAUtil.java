package com.openclassrooms.SafetyNetAlerts.util;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import org.springframework.http.MediaType;

/**
 * SNAUtil is an entity that holds constants (ie Majority Age) and is used to calculate the age of Persons
 */
public class SNAUtil {
	public static final int MAJORITY_AGE = 18;
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	/**
	 * <p>Will calculate the age in years using two dates</p>
	 * @param dateFrom the older date
	 * @param dateTo the most recent date
	 * @return the age in years
	 */
	public static int getAge(LocalDate dateFrom, LocalDate dateTo) {
		return (Period.between(dateFrom, dateTo).getYears());
	}
	
	/**
	 * <p>Will calculate the age in years from today to string of the past date</p>
	 * @param dateFrom the older date
	 * @return the age in years
	 */
	public static int getAge(String dateFrom) {
		return (Period.between(LocalDate.parse(dateFrom, DateTimeFormatter.ofPattern("MM/dd/yyyy")), LocalDate.now()).getYears());
	}
}
