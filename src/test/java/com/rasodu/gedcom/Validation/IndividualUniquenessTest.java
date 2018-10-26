package com.rasodu.gedcom.Validation;

import com.rasodu.gedcom.Fixtures;
import com.rasodu.gedcom.Utils.GedLogger;
import com.rasodu.gedcom.core.Individual;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class IndividualUniquenessTest {

	private GedLogger logger;
	private IndividualValidator vali;
	
	@Before
	public void setup() {
		Fixtures.SetupTestFixtures();
		logger = mock(GedLogger.class);
		vali = new IndividualValidator(null, null, logger);
	}
	
	@Test
    public void idUniquenessSuccess(){
        //arrange
    	List<Individual> indList = new ArrayList<Individual>();
        indList.add(Fixtures.indiList.get(0));
        indList.add(Fixtures.indiList.get(1));
        vali.setIndividualList(indList);
        //act
        boolean result = vali.validateIdUniqueness();
        //assert
        verifyNoMoreInteractions(logger);
        Assert.assertTrue(result);
    }
	
	@Test
    public void idUniquenessCollision(){
        //arrange
    	List<Individual> indList = new ArrayList<Individual>();
        indList.add(Fixtures.indiList.get(0));
        indList.add(Fixtures.indiList.get(12));
        vali.setIndividualList(indList);
        //act
        boolean result = vali.validateIdUniqueness();
        //assert
        verify(logger, Mockito.times(1)).error("US22", indList.get(1), null, "Individual Bizarro /Max/ shares their ID with Mister /Max/");
        verifyNoMoreInteractions(logger);
        Assert.assertFalse(result);
    }
	
	@Test
    public void idUniquenessDuplication(){
        //arrange
    	List<Individual> indList = new ArrayList<Individual>();
        indList.add(Fixtures.indiList.get(0));
        indList.add(Fixtures.indiList.get(0));
        vali.setIndividualList(indList);
        //act
        boolean result = vali.validateIdUniqueness();
        //assert
        verify(logger, Mockito.times(1)).error("US22", indList.get(1), null, "Individual is listed multiple times in the input data");
        verifyNoMoreInteractions(logger);
        Assert.assertFalse(result);
    }
}
