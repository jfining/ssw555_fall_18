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

public class GeneralValidatorTest extends ValidatorLoader {
	@Test //US01
    public void noDatesBeforeCurrentDateShouldProduceError() throws ParseException {
		//arrange
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        List<Individual> indList = new ArrayList<Individual>();
        List<Family> famList = new ArrayList<Family>();
        
        Individual ind = new Individual();
        ind.Id = "US01_IID1";
        ind.Birthday = sdf.parse("02/14/2050");
        ind.Death = sdf.parse("02/14/2020");
        
        Individual ind2 = new Individual();
        ind2.Id = "US01_IID2";
        ind2.Birthday = sdf.parse("03/20/2085");
        ind2.Death = sdf.parse("02/14/2114");
        
        Individual ind3 = new Individual();
        ind3.Id = "US01_IID3";
        ind3.Birthday = sdf.parse("09/20/2085");
        ind3.Death = sdf.parse("05/24/2114");

        indList.add(ind);
        indList.add(ind2);
        indList.add(ind3);
        
        Family fam = new Family();
        fam.Id = "US01_FID1";
        fam.Divorced = sdf.parse("02/28/2050");
        fam.Married = sdf.parse("06/04/2019");
        
        Family fam2 = new Family();
        fam2.Id = "US01_FID2";
        fam2.Divorced = sdf.parse("02/14/2034");
        fam2.Married = sdf.parse("03/17/2027");
        
        famList.add(fam);
        famList.add(fam2);
        
        GedLogger logger = mock(GedLogger.class);
        GeneralValidator validator = new GeneralValidator(famList, indList, logger);
        //act
        boolean result = validator.noDatesBeforeCurrentDate();
        //assert
        verify(logger, Mockito.times(1)).error("US01", null, fam, "Divorced Date is after current date.");
        verify(logger, Mockito.times(1)).error("US01", null, fam, "Marriage Date is after current date.");
        verify(logger, Mockito.times(1)).error("US01", null, fam2, "Divorced Date is after current date.");
        verify(logger, Mockito.times(1)).error("US01", null, fam2, "Marriage Date is after current date.");
        verify(logger, Mockito.times(1)).error("US01", ind, null, "Birth Date is after current date.");
        verify(logger, Mockito.times(1)).error("US01", ind, null, "Death Date is after current date.");
        verify(logger, Mockito.times(1)).error("US01", ind2, null, "Birth Date is after current date.");
        verify(logger, Mockito.times(1)).error("US01", ind2, null, "Death Date is after current date.");
        verify(logger, Mockito.times(1)).error("US01", ind3, null, "Birth Date is after current date.");
        verify(logger, Mockito.times(1)).error("US01", ind3, null, "Death Date is after current date.");
        
        verifyNoMoreInteractions(logger);
        Assert.assertFalse(result);
    }
}
