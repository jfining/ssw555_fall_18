package com.rasodu.gedcom.Validation;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import com.rasodu.gedcom.core.*;
import com.rasodu.gedcom.Fixtures;
import com.rasodu.gedcom.Utils.GedLogger;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

public class SiblingBirthsTest {

	private GedLogger logger;
	private FamilyValidator vali;
	
	@Before
	public void setup() {
		logger = mock(GedLogger.class);
		Fixtures.SetupTestFixtures();
		vali = new FamilyValidator(null, null, logger);
	}
	
	@Test
	public void TestUS13NormalSiblings() {
		Fixtures.SetupTestFixtures();
		List<Family> fams = new ArrayList<Family>();
		List<Individual> indis = new ArrayList<Individual>();
		fams.add(Fixtures.famList.get(9));
		indis.add(Fixtures.indiList.get(0));
		indis.add(Fixtures.indiList.get(1));
		indis.add(Fixtures.indiList.get(4));
		indis.add(Fixtures.indiList.get(5));
		indis.add(Fixtures.indiList.get(7));
		vali.setIndividualList(indis);
		vali.setFamilyList(fams);
		assertTrue(vali.validateChildBirthdays());
		verifyNoMoreInteractions(logger);
	}
	
	@Test
	public void TestUS13AnomalousSiblings() {
		Fixtures.SetupTestFixtures();
		List<Family> fams = new ArrayList<Family>();
		List<Individual> indis = new ArrayList<Individual>();
		fams.add(Fixtures.famList.get(10));
		indis.add(Fixtures.indiList.get(0));
		indis.add(Fixtures.indiList.get(1));
		indis.add(Fixtures.indiList.get(4));
		indis.add(Fixtures.indiList.get(5));
		indis.add(Fixtures.indiList.get(6));
		indis.add(Fixtures.indiList.get(7));
		vali.setIndividualList(indis);
		vali.setFamilyList(fams);
		assertFalse(vali.validateChildBirthdays());
		verify(logger, Mockito.times(1)).error("US13", indis.get(4), fams.get(0), "Child born between 1 day and 9 months after older sibling indi04");
		verifyNoMoreInteractions(logger);
	}
	
	@Test
	public void TestUS14Quintuplets() {
		Fixtures.SetupTestFixtures();
		List<Family> fams = new ArrayList<Family>();
		List<Individual> indis = new ArrayList<Individual>();
		fams.add(Fixtures.famList.get(11));
		indis.add(Fixtures.indiList.get(0));
		indis.add(Fixtures.indiList.get(1));
		indis.add(Fixtures.indiList.get(4));
		indis.add(Fixtures.indiList.get(5));
		indis.add(Fixtures.indiList.get(8));
		indis.add(Fixtures.indiList.get(9));
		indis.add(Fixtures.indiList.get(10));
		vali.setIndividualList(indis);
		vali.setFamilyList(fams);
		assertTrue(vali.validateChildBirthdays());
		verifyNoMoreInteractions(logger);
	}
	
	@Test
	public void TestUS14Sextuplets() {
		Fixtures.SetupTestFixtures();
		List<Family> fams = new ArrayList<Family>();
		List<Individual> indis = new ArrayList<Individual>();
		fams.add(Fixtures.famList.get(12));
		indis.add(Fixtures.indiList.get(0));
		indis.add(Fixtures.indiList.get(1));
		indis.add(Fixtures.indiList.get(4));
		indis.add(Fixtures.indiList.get(5));
		indis.add(Fixtures.indiList.get(8));
		indis.add(Fixtures.indiList.get(9));
		indis.add(Fixtures.indiList.get(10));
		indis.add(Fixtures.indiList.get(11));
		vali.setIndividualList(indis);
		vali.setFamilyList(fams);
		assertFalse(vali.validateChildBirthdays());
		verify(logger, Mockito.times(1)).error("US14", indis.get(2), fams.get(0), "Child has 5 or more siblings born at the same time");
		verify(logger, Mockito.times(1)).error("US14", indis.get(3), fams.get(0), "Child has 5 or more siblings born at the same time");
		verify(logger, Mockito.times(1)).error("US14", indis.get(4), fams.get(0), "Child has 5 or more siblings born at the same time");
		verify(logger, Mockito.times(1)).error("US14", indis.get(5), fams.get(0), "Child has 5 or more siblings born at the same time");
		verify(logger, Mockito.times(1)).error("US14", indis.get(6), fams.get(0), "Child has 5 or more siblings born at the same time");
		verify(logger, Mockito.times(1)).error("US14", indis.get(7), fams.get(0), "Child has 5 or more siblings born at the same time");
		verifyNoMoreInteractions(logger);
	}
}
