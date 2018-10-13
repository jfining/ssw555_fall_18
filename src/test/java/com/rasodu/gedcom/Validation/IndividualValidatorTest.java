package com.rasodu.gedcom.Validation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.rasodu.gedcom.Utils.GedLogger;
import com.rasodu.gedcom.core.Family;
import com.rasodu.gedcom.core.Individual;

public class IndividualValidatorTest {
    //US12
    @Test
    public void parentsNotTooOldShouldProduceError() throws ParseException {
    	//arrange
    	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        List<Individual> indList = new ArrayList<Individual>();
        List<Family> famList = new ArrayList<Family>();
        
        Individual father = new Individual();
        father.Id = "US11_IID1";
        father.Birthday = sdf.parse("02/14/1900");
        father.Death = null;
        
        Individual mother = new Individual();
        mother.Id = "US11_IID2";
        mother.Birthday = sdf.parse("03/20/1910");
        mother.Death = null;
        
        Individual child = new Individual();
        child.Id = "";
        child.Birthday = sdf.parse("02/10/1985");
        child.Death = null;
        
        Family fam = new Family();
        fam.Id = "US11_FID1";
        fam.Divorced = null;
        fam.Married = sdf.parse("06/04/1930");
        fam.HusbandId = father.Id;
        fam.WifeId = mother.Id;
        fam.ChildrenIds = new ArrayList<String>();
        fam.ChildrenIds.add(child.Id);

        
        father.SpouseInFamily.add(fam.Id);
        mother.SpouseInFamily.add(fam.Id);
        child.ChildOfFamily = fam.Id;

        
        indList.add(father);
        indList.add(mother);
        indList.add(child);
        famList.add(fam);

        
        GedLogger logger = mock(GedLogger.class);
        IndividualValidator validator = new IndividualValidator(famList, indList, logger);
        //act
        boolean result = validator.parentsNotTooOld();
        //assert
        verify(logger, Mockito.times(1)).error("US12", child, fam, "Individual cannot be greater than 60 years younger than mother.");
        verify(logger, Mockito.times(1)).error("US12", child, fam, "Individual cannot be greater than 80 years younger than father.");
        //assert
        verifyNoMoreInteractions(logger);
        Assert.assertFalse(result);
    }
    
    //US12
    @Test
    public void noBigamyShouldBeSuccess() throws ParseException {
    	//arrange
    	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        List<Individual> indList = new ArrayList<Individual>();
        List<Family> famList = new ArrayList<Family>();
        
        Individual father = new Individual();
        father.Id = "US11_IID1";
        father.Birthday = sdf.parse("02/14/1900");
        father.Death = null;
        
        Individual mother = new Individual();
        mother.Id = "US11_IID2";
        mother.Birthday = sdf.parse("03/20/1910");
        mother.Death = null;
        
        Individual child = new Individual();
        child.Id = "";
        child.Birthday = sdf.parse("02/10/1945");
        child.Death = null;
        
        Family fam = new Family();
        fam.Id = "US11_FID1";
        fam.Divorced = null;
        fam.Married = sdf.parse("06/04/1930");
        fam.HusbandId = father.Id;
        fam.WifeId = mother.Id;
        fam.ChildrenIds = new ArrayList<String>();
        fam.ChildrenIds.add(child.Id);
        
        father.SpouseInFamily.add(fam.Id);
        mother.SpouseInFamily.add(fam.Id);
        child.ChildOfFamily = fam.Id;

        indList.add(father);
        indList.add(mother);
        indList.add(child);
        famList.add(fam);
        
        GedLogger logger = mock(GedLogger.class);
        IndividualValidator validator = new IndividualValidator(famList, indList, logger);
        //act
        boolean result = validator.parentsNotTooOld();
        //assert
        verifyNoMoreInteractions(logger);
        Assert.assertTrue(result);
    }
}
