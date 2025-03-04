package com.openclassrooms.SafetyNetAlerts.util;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import org.springframework.http.MediaType;

public class SNAUtil {
	public static final int MAJORITY_AGE = 18;
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	public static int getAge(LocalDate dateFrom, LocalDate dateTo) {
		return (Period.between(dateFrom, dateTo).getYears());
	}
	
	public static int getAge(String dateFrom) {
		return (Period.between(LocalDate.parse(dateFrom, DateTimeFormatter.ofPattern("MM/dd/yyyy")), LocalDate.now()).getYears());
	}
}
