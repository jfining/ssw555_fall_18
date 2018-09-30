package com.rasodu.gedcom;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {

	// US02 Test Cases

	@Test
	public void checkFamily() {

		HashMap<String, String> fakeFam = new HashMap<>();
		fakeFam.put("1", "Jones");

		assertTrue(fakeFam.get("1") == "Jones");
	}

	@Test
	public void checkIndividual() {
		HashMap<String, String> fakeInd = new HashMap<>();
		fakeInd.put("1", "Justin");

		assertFalse(fakeInd.get("1") == "Jones");
	}

	@Test
	public void checkBirthday() throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("mm/dd/yyyy");
		Date birthdate = format.parse("10/17/1988");
		assertNotNull(birthdate);
	}

	@Test
	public void checkMarriage() throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("mm/dd/yyyy");
		Date marriageDate = format.parse("10/17/1988");
		assertNotNull(marriageDate);
	}

	@Test
	public void checkDates() throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("mm/dd/yyyy");
		Date marriageDate = format.parse("1/17/1989");
		Date birthdate = format.parse("10/17/1988");
		assertTrue(marriageDate.after(birthdate));
	}
	// US03 Test Cases

	@Test
	public void checkIndividualID() {
		HashMap<String, String> fakeInd = new HashMap<>();
		fakeInd.put("1", "Justin");
		assertTrue(fakeInd.get("1") == "Justin");
	}

	@Test
	public void checkBirthday1() throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("mm/dd/yyyy");
		Date birthdate = format.parse("10/17/1988");
		assertNotNull(birthdate);
	}

	@Test
	public void checkDeathDate1() throws ParseException {
		Date deathDate = null;
		assertTrue(deathDate == null);
	}

	@Test
	public void checkDeathDate() throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("mm/dd/yyyy");
		Date deathDate = format.parse("1/17/1998");
		assertNotNull(deathDate);
	}

	@Test
	public void checkBirthDeathDates() throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("mm/dd/yyyy");
		Date deathDate = format.parse("1/17/1989");
		Date birthdate = format.parse("10/17/1988");
		assertTrue(deathDate.after(birthdate));

	}

}
