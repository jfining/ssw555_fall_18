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

    @Test
    public void bornBeforeOrAfterMarriageShouldProduceError() throws ParseException {
        //arrange
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Individual ind1 = new Individual();
        ind1.Id = "US08_IID1";
        ind1.Birthday = sdf.parse("01/10/1981");
        ind1.ChildOfFamily = new ArrayList<String>();
        ind1.ChildOfFamily.add("US08_FID1");
        Individual ind2 = new Individual();
        ind2.Id = "US08_IID2";
        ind2.Birthday = sdf.parse("01/10/1982");
        ind2.ChildOfFamily = new ArrayList<String>();
        ind2.ChildOfFamily.add("US08_FID1");
        List<Individual> indList = new ArrayList<Individual>();
        indList.add(ind1);
        indList.add(ind2);
        Family fam = new Family();
        fam.Id = "US08_FID1";
        fam.Married = sdf.parse("02/15/1981");
        fam.Divorced = sdf.parse("01/15/1981");
        fam.ChildrenIds = new ArrayList<String>();
        fam.ChildrenIds.add(ind1.Id);
        fam.ChildrenIds.add(ind2.Id);
        List<Family> famList = new ArrayList<Family>();
        famList.add(fam);
        GedLogger logger = mock(GedLogger.class);
        IndividualValidator validator = new IndividualValidator(famList, indList, logger);
        //act
        boolean result = validator.bornBeforeOrAfterMarriage();
        //assert
        verify(logger, Mockito.times(1)).error("US08", ind1, fam, "Individual born before marriage.");
        verify(logger, Mockito.times(1)).error("US08", ind2, fam, "Individual born 9 or more months divorce.");
        verifyNoMoreInteractions(logger);
        Assert.assertFalse(result);
    }

    @Test
    public void bornBeforeOrAfterMarriageShouldBeSuccess() throws ParseException {
        //arrange
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Individual ind = new Individual();
        ind.Id = "US08_IID1";
        ind.Birthday = sdf.parse("09/20/1981");
        ind.ChildOfFamily = new ArrayList<String>();
        ind.ChildOfFamily.add("US08_FID1");
        List<Individual> indList = new ArrayList<Individual>();
        indList.add(ind);
        Family fam = new Family();
        fam.Id = "US08_FID1";
        fam.Married = sdf.parse("02/15/1981");
        fam.Divorced = sdf.parse("01/15/1981");
        fam.ChildrenIds = new ArrayList<String>();
        fam.ChildrenIds.add(ind.Id);
        List<Family> famList = new ArrayList<Family>();
        famList.add(fam);
        GedLogger logger = mock(GedLogger.class);
        IndividualValidator validator = new IndividualValidator(famList, indList, logger);
        //act
        boolean result = validator.bornBeforeOrAfterMarriage();
        //assert
        verifyNoMoreInteractions(logger);
        Assert.assertTrue(result);
    }

}
