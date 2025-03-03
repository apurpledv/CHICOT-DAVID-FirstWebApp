package com.openclassrooms.SafetyNetAlerts.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class SNAUtil {
	public static final int MAJORITY_AGE = 18;
	
	public static int getAge(LocalDate dateFrom, LocalDate dateTo) {
		return (Period.between(dateFrom, dateTo).getYears());
	}
	
	public static int getAge(String dateFrom) {
		return (Period.between(LocalDate.parse(dateFrom, DateTimeFormatter.ofPattern("MM/dd/yyyy")), LocalDate.now()).getYears());
	}
}
