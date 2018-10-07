package com.rasodu.gedcom;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import com.rasodu.gedcom.core.*;
import com.rasodu.gedcom.Utils.GedLogger;
import com.rasodu.gedcom.Validation.*;


public class MarriageBeforeDeathTest{

	private GedLogger logger = new GedLogger();
	private FamilyValidator vali;
	
	@Before
	public void setup() {
		Fixtures.SetupTestFixtures();
		vali = new FamilyValidator(null, null, logger);
	}
	
	@Test
	public void testCase00() {
		Fixtures.SetupTestFixtures();
		List<Family> fams = new ArrayList<Family>();
		List<Individual> indis = new ArrayList<Individual>();
		fams.add(Fixtures.famList.get(0));
		indis.add(Fixtures.indiList.get(0));
		indis.add(Fixtures.indiList.get(1));
		vali.setIndividualList(indis);
		vali.setFamilyList(fams);
		assertTrue(vali.validateMarriageDate());
	}
	
	@Test
	public void testCase01() {
		List<Family> fams = new ArrayList<Family>();
		List<Individual> indis = new ArrayList<Individual>();
		fams.add(Fixtures.famList.get(1));
		indis.add(Fixtures.indiList.get(0));
		indis.add(Fixtures.indiList.get(2));
		vali.setIndividualList(indis);
		vali.setFamilyList(fams);
		assertFalse(vali.validateMarriageDate());
	}
	
	@Test
	public void testCase02() {
		List<Family> fams = new ArrayList<Family>();
		List<Individual> indis = new ArrayList<Individual>();
		fams.add(Fixtures.famList.get(2));
		indis.add(Fixtures.indiList.get(1));
		indis.add(Fixtures.indiList.get(3));
		vali.setIndividualList(indis);
		vali.setFamilyList(fams);
		assertFalse(vali.validateMarriageDate());
	}
	
	@Test
	public void testCase03() {
		List<Family> fams = new ArrayList<Family>();
		List<Individual> indis = new ArrayList<Individual>();
		fams.add(Fixtures.famList.get(3));
		indis.add(Fixtures.indiList.get(2));
		indis.add(Fixtures.indiList.get(3));
		vali.setIndividualList(indis);
		vali.setFamilyList(fams);
		assertFalse(vali.validateMarriageDate());
	}
	
	@Test
	public void testCase04() {
		List<Family> fams = new ArrayList<Family>();
		List<Individual> indis = new ArrayList<Individual>();
		fams.add(Fixtures.famList.get(4));
		indis.add(Fixtures.indiList.get(0));
		indis.add(Fixtures.indiList.get(2));
		vali.setIndividualList(indis);
		vali.setFamilyList(fams);
		assertTrue(vali.validateMarriageDate());
	}

}
