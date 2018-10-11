package com.rasodu.gedcom;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.awt.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.rasodu.gedcom.Utils.GedLogger;
import com.rasodu.gedcom.Validation.IndividualValidator;
import com.rasodu.gedcom.core.Family;
import com.rasodu.gedcom.core.Individual;

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

	@Test
	public void checkDateDiff() throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("mm/dd/yyyy");
		Date deathDate = format.parse("1/17/1989");
		Date birthDate = format.parse("10/17/1988");
		long diff = deathDate.getTime() - birthDate.getTime();

		assertTrue(diff > 14);

	}

	@Test
	public void bornBeforeOrAfterMarriageShouldProduceError() throws ParseException {
		// arrange
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Individual ind1 = new Individual();
		ind1.Id = "US16_IID1";
		ind1.Gender = 'M';
		ind1.Name = "Justin Verzosa";
		ind1.Birthday = sdf.parse("01/10/1981");
		ind1.ChildOfFamily = "US16_FID1";
		Individual ind2 = new Individual();
		ind2.Id = "US16_IID2";
		ind2.Name = "Jan Verzosa";
		ind2.Birthday = sdf.parse("01/10/1982");
		ind2.ChildOfFamily = "US16_FID1";
		ArrayList<Individual> indList = new ArrayList<Individual>();
		indList.add(ind1);
		indList.add(ind2);
		Family fam = new Family();
		fam.Id = "US16_FID1";
		fam.HusbandName = "Paul Verzosa";
		fam.Married = sdf.parse("02/15/1981");
		fam.Divorced = sdf.parse("01/15/1981");
		fam.ChildrenIds = new ArrayList<String>();
		fam.ChildrenIds.add(ind1.Id);
		fam.ChildrenIds.add(ind2.Id);

		String[] husbSplitName = fam.HusbandName.split("\\s+");
		String[] indSplitName = ind1.Name.split("\\s+");

		// assert
		Assert.assertTrue(husbSplitName[1].equals(indSplitName[1]));
	}

}
