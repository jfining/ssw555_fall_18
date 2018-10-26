package com.rasodu.gedcom.Validation;

import com.rasodu.gedcom.Fixtures;
import com.rasodu.gedcom.Utils.GedLogger;
import com.rasodu.gedcom.core.Family;
import com.rasodu.gedcom.core.Individual;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;


public class MarriageBeforeDeathTest {

    private GedLogger logger;
    private FamilyValidator vali;

    @Before
    public void setup() {
        Fixtures.SetupTestFixtures();
        logger = mock(GedLogger.class);
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
        verifyNoMoreInteractions(logger);
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
        verifyNoMoreInteractions(logger);
    }

}
