package com.rasodu.gedcom.Validation;

import com.rasodu.gedcom.Fixtures;
import com.rasodu.gedcom.Utils.GedLogger;
import com.rasodu.gedcom.core.Family;
import com.rasodu.gedcom.core.Individual;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class FamilyInfoTest {
	
	private GedLogger logger;
    private FamilyValidator vali;

    @Before
    public void setup() {
        logger = mock(GedLogger.class);
        Fixtures.SetupTestFixtures();
        vali = new FamilyValidator(null, null, logger);
    }

    @Test
    public void TestUS34CreepyMarriageDetection() {
        List<Family> fams = new ArrayList<Family>();
        List<Individual> indis = new ArrayList<Individual>();
        fams.add(Fixtures.famList.get(0));
        fams.add(Fixtures.famList.get(13));
        fams.add(Fixtures.famList.get(14));
        indis.add(Fixtures.indiList.get(0));
        indis.add(Fixtures.indiList.get(1));
        indis.add(Fixtures.indiList.get(14));
        indis.add(Fixtures.indiList.get(15));
        indis.add(Fixtures.indiList.get(16));
        indis.add(Fixtures.indiList.get(17));
        vali.setIndividualList(indis);
        vali.setFamilyList(fams);
        vali.listCreepyMarriages();
        verify(logger, Mockito.times(1)).info("US34", null, fams.get(1), "Husband indi14 (OldHusband /Creepy/) married indi15 (Lolita /Creepy/) who was half his age or younger");
        verify(logger, Mockito.times(1)).info("US34", null, fams.get(2), "Wife indi17 (OldLady /Complex/) married indi16 (Oedipus /Complex/) who was half her age or younger");
        verifyNoMoreInteractions(logger);
    }
    
    @Test
    public void TestUS32TwinsDetection() {
        List<Family> fams = new ArrayList<Family>();
        List<Individual> indis = new ArrayList<Individual>();
        fams.add(Fixtures.famList.get(0));
        fams.add(Fixtures.famList.get(9));
        indis.add(Fixtures.indiList.get(0));
        indis.add(Fixtures.indiList.get(1));
        indis.add(Fixtures.indiList.get(4));
        indis.add(Fixtures.indiList.get(5));
        indis.add(Fixtures.indiList.get(7));
        vali.setIndividualList(indis);
        vali.setFamilyList(fams);
        vali.listMultipleBirths();
        verify(logger, Mockito.times(1)).info("US32", null, fams.get(1), "Family has multiple simultaneous births: indi04 (ChildOne /Max/), indi05 (Twin /Max/)");
        verifyNoMoreInteractions(logger);
    }
}
