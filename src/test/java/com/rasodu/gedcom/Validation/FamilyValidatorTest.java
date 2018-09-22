package com.rasodu.gedcom.Validation;

import com.rasodu.gedcom.Utils.GedLogger;
import com.rasodu.gedcom.core.Family;
import com.rasodu.gedcom.core.Individual;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class FamilyValidatorTest {
    @Test
    public void noDivorceAfterDeathTestShouldProduceError() throws ParseException {
        //arrange
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Individual ind = new Individual();
        ind.Id = "US01_IID1";
        ind.Death = sdf.parse("02/14/2014");
        ind.SpouseInFamily = new ArrayList<String>();
        ind.SpouseInFamily.add("US01_FID1");
        ind.SpouseInFamily.add("US01_FID2");
        List<Individual> indList = new ArrayList<Individual>();
        indList.add(ind);
        Family fam1 = new Family();
        fam1.Id = "US01_FID1";
        fam1.Divorced = sdf.parse("02/14/2004");
        Family fam2 = new Family();
        fam2.Id = "US01_FID2";
        fam2.Divorced = sdf.parse("02/14/2016");
        List<Family> famList = new ArrayList<Family>();
        famList.add(fam1);
        famList.add(fam2);
        GedLogger logger = mock(GedLogger.class);
        FamilyValidator validator = new FamilyValidator(famList, indList, logger);
        //act
        boolean result = validator.noDivorceAfterDeath();
        //assert
        verify(logger, Mockito.times(1)).error("US06", ind, fam2, "Individual divorced after death.");
        verifyNoMoreInteractions(logger);
        Assert.assertFalse(result);
    }
    @Test
    public void noDivorceAfterDeathTestShouldBeSuccess() throws ParseException {
        //arrange
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Individual ind = new Individual();
        ind.Id = "US01_IID1";
        ind.Death = sdf.parse("02/14/2014");
        ind.SpouseInFamily = new ArrayList<String>();
        ind.SpouseInFamily.add("US01_FID1");
        ind.SpouseInFamily.add("US01_FID2");
        List<Individual> indList = new ArrayList<Individual>();
        indList.add(ind);
        Family fam1 = new Family();
        fam1.Id = "US01_FID1";
        fam1.Divorced = sdf.parse("02/14/2004");
        Family fam2 = new Family();
        fam2.Id = "US01_FID2";
        fam2.Divorced = sdf.parse("02/14/2012");
        List<Family> famList = new ArrayList<Family>();
        famList.add(fam1);
        famList.add(fam2);
        GedLogger logger = mock(GedLogger.class);
        FamilyValidator validator = new FamilyValidator(famList, indList, logger);
        //act
        boolean result = validator.noDivorceAfterDeath();
        //assert
        verifyNoMoreInteractions(logger);
        Assert.assertTrue(result);
    }

}
