package com.rasodu.gedcom.Validation;

import com.rasodu.gedcom.Fixtures;
import com.rasodu.gedcom.Utils.GedLogger;
import com.rasodu.gedcom.core.Family;
import com.rasodu.gedcom.core.Individual;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class FamilyValidatorTest extends ValidatorLoader {

    private GedLogger logger;
    private FamilyValidator famVali;

    @Before
    public void setup() {
        Fixtures.SetupTestFixtures();
        logger = mock(GedLogger.class);
        famVali = new FamilyValidator(null, null, logger);
    }

    @Test
    public void noDivorceAfterDeathTestShouldProduceError() {
        //arrange
        List<Individual> indList = new ArrayList<Individual>();
        indList.add(Fixtures.indiList.get(3));
        indList.add(Fixtures.indiList.get(1));
        List<Family> famList = new ArrayList<Family>();
        famList.add(Fixtures.famList.get(6));
        famVali.setFamilyList(famList);
        famVali.setIndividualList(indList);
        //act
        boolean result = famVali.noDivorceAfterDeath();
        //assert
        verify(logger, Mockito.times(1)).error("US06", indList.get(0), famList.get(0), "Individual divorced after death.");
        verifyNoMoreInteractions(logger);
        Assert.assertFalse(result);
    }

    @Test
    public void noDivorceAfterDeathTestShouldBeSuccess(){
        //arrange
        List<Individual> indList = new ArrayList<Individual>();
        indList.add(Fixtures.indiList.get(3));
        indList.add(Fixtures.indiList.get(1));
        List<Family> famList = new ArrayList<Family>();
        famList.add(Fixtures.famList.get(5));
        famVali.setFamilyList(famList);
        famVali.setIndividualList(indList);
        //act
        boolean result = famVali.noDivorceAfterDeath();
        //assert
        verifyNoMoreInteractions(logger);
        Assert.assertTrue(result);
    }

    @Test //US04
    public void noDivorceBeforeMarriageTestShouldProduceError(){
        //arrange
        List<Individual> indList = new ArrayList<Individual>();
        indList.add(Fixtures.indiList.get(0));
        indList.add(Fixtures.indiList.get(1));
        List<Family> famList = new ArrayList<Family>();
        famList.add(Fixtures.famList.get(7));
        famVali.setFamilyList(famList);
        famVali.setIndividualList(indList);
        //act
        boolean result = famVali.validateMarriageDate();
        //assert
        verify(logger, Mockito.times(1)).error("US04", null, famList.get(0), "Divorce Date is before marriage date.");
        verifyNoMoreInteractions(logger);
        Assert.assertFalse(result);
    }

    @Test //US04
    public void noDivorceBeforeMarriageTestShouldBeSuccess(){
        //arrange
        List<Individual> indList = new ArrayList<Individual>();
        indList.add(Fixtures.indiList.get(0));
        indList.add(Fixtures.indiList.get(1));
        List<Family> famList = new ArrayList<Family>();
        famList.add(Fixtures.famList.get(8));
        famVali.setFamilyList(famList);
        famVali.setIndividualList(indList);
        //act
        boolean result = famVali.validateMarriageDate();
        Assert.assertTrue(result);
    }

    @Test
    public void bornBeforeOrAfterMarriageShouldProduceError() throws ParseException {
        //arrange
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Individual ind1 = new Individual();
        ind1.Id = "US08_IID1";
        ind1.Birthday = sdf.parse("01/10/1981");
        ind1.ChildOfFamily = "US08_FID1";
        Individual ind2 = new Individual();
        ind2.Id = "US08_IID2";
        ind2.Birthday = sdf.parse("01/10/1982");
        ind2.ChildOfFamily = "US08_FID1";
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
        verify(logger, Mockito.times(1)).error("US08", ind2, fam, "Individual born 9 or more months after divorce.");
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
        ind.ChildOfFamily = "US08_FID1";
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
    
    @Test
    public void testHusbandWifeUniquenessSuccess() {
    	//arrange
    	List<Individual> indList = new ArrayList<Individual>();
        indList.add(Fixtures.indiList.get(0));
        indList.add(Fixtures.indiList.get(1));
        indList.add(Fixtures.indiList.get(2));
        List<Family> famList = new ArrayList<Family>();
        famList.add(Fixtures.famList.get(0));
        famList.add(Fixtures.famList.get(1));
        famVali.setFamilyList(famList);
        famVali.setIndividualList(indList);
        //act
        boolean result = famVali.validateHusbandWifeUniqueness();
        //assert
       verifyNoMoreInteractions(logger);
        Assert.assertTrue(result);
    }
    
    @Test
    public void testHusbandWifeUniquenessCollision() {
    	//arrange
    	List<Individual> indList = new ArrayList<Individual>();
        indList.add(Fixtures.indiList.get(0));
        indList.add(Fixtures.indiList.get(1));
        indList.add(Fixtures.indiList.get(4));
        indList.add(Fixtures.indiList.get(5));
        indList.add(Fixtures.indiList.get(7));
        List<Family> famList = new ArrayList<Family>();
        famList.add(Fixtures.famList.get(0));
        famList.add(Fixtures.famList.get(9));
        famVali.setFamilyList(famList);
        famVali.setIndividualList(indList);
        //act
        boolean result = famVali.validateHusbandWifeUniqueness();
        //assert
        verify(logger, Mockito.times(1)).error("US24", null, famList.get(1),
        		"Family has the same husband (indi00) and wife (indi01) as family fam00");
        verifyNoMoreInteractions();
        Assert.assertFalse(result);

    //US11
    @Test
    public void noBigamyShouldProduceError() throws ParseException {
        //arrange
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        List<Individual> indList = new ArrayList<Individual>();
        List<Family> famList = new ArrayList<Family>();

        Individual ind = new Individual();
        ind.Id = "US11_IID1";
        ind.Birthday = sdf.parse("02/14/1980");
        ind.Death = null;

        Individual ind2 = new Individual();
        ind2.Id = "US11_IID2";
        ind2.Birthday = sdf.parse("03/20/1985");
        ind2.Death = null;

        Family fam = new Family();
        fam.Id = "US11_FID1";
        fam.Divorced = null;
        fam.Married = sdf.parse("06/04/2000");
        fam.HusbandId = ind.Id;
        fam.WifeId = ind2.Id;

        Family fam2 = new Family();
        fam2.Id = "US11_FID2";
        fam2.Divorced = null;
        fam2.Married = sdf.parse("03/17/2005");
        fam2.HusbandId = ind.Id;
        fam2.WifeId = ind2.Id;

        ind.SpouseInFamily.add(fam.Id);
        ind.SpouseInFamily.add(fam2.Id);
        ind2.SpouseInFamily.add(fam.Id);
        ind2.SpouseInFamily.add(fam2.Id);

        indList.add(ind);
        indList.add(ind2);
        famList.add(fam);
        famList.add(fam2);

        GedLogger logger = mock(GedLogger.class);
        FamilyValidator validator = new FamilyValidator(famList, indList, logger);
        //act
        boolean result = validator.noBigamy();
        //assert
        verify(logger, Mockito.times(1)).error("US11", ind, fam, "Individual cannot be in more than one marriage.");
        verify(logger, Mockito.times(1)).error("US11", ind2, fam, "Individual cannot be in more than one marriage.");
        verify(logger, Mockito.times(1)).error("US11", ind, fam2, "Individual cannot be in more than one marriage.");
        verify(logger, Mockito.times(1)).error("US11", ind2, fam2, "Individual cannot be in more than one marriage.");
        //assert
        verifyNoMoreInteractions(logger);
        Assert.assertFalse(result);
    }

    //US11
    @Test
    public void noBigamyShouldBeSuccess() throws ParseException {
        //arrange
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        List<Individual> indList = new ArrayList<Individual>();
        List<Family> famList = new ArrayList<Family>();

        Individual ind = new Individual();
        ind.Id = "US11_IID1";
        ind.Birthday = sdf.parse("02/14/1980");
        ind.Death = null;

        Individual ind2 = new Individual();
        ind2.Id = "US11_IID2";
        ind2.Birthday = sdf.parse("03/20/1985");
        ind2.Death = null;

        Family fam = new Family();
        fam.Id = "US11_FID1";
        fam.Divorced = sdf.parse("03/10/2004");
        fam.Married = sdf.parse("06/04/2000");
        fam.HusbandId = ind.Id;
        fam.WifeId = ind2.Id;

        Family fam2 = new Family();
        fam2.Id = "US11_FID2";
        fam2.Divorced = null;
        fam2.Married = sdf.parse("03/17/2005");
        fam2.HusbandId = ind.Id;
        fam2.WifeId = ind2.Id;

        ind.SpouseInFamily.add(fam.Id);
        ind.SpouseInFamily.add(fam2.Id);
        ind2.SpouseInFamily.add(fam.Id);
        ind2.SpouseInFamily.add(fam2.Id);

        indList.add(ind);
        indList.add(ind2);
        famList.add(fam);
        famList.add(fam2);

        GedLogger logger = mock(GedLogger.class);
        FamilyValidator validator = new FamilyValidator(famList, indList, logger);
        //act
        boolean result = validator.noBigamy();
        //assert
        verifyNoMoreInteractions(logger);
        Assert.assertTrue(result);
    }

    //US15
    @Test
    public void noMoreThanFifteenSiblings() {
        //arrange
        GedLogger logger = mock(GedLogger.class);
        Load("test_us15", logger);
        //act
        boolean result = fv.noMoreThanFifteenSiblings();
        //assert
        verify(logger, Mockito.times(1)).anomaly("US15", null, repository.GetFamily("US15_FID1"), "The family has more than 15 siblings.");
        verifyNoMoreInteractions(logger);
        Assert.assertFalse(result);
    }

    //US19
    @Test
    public void firstCousinShouldNotMarry() {
        //arrange
        GedLogger logger = mock(GedLogger.class);
        Load("test_us19", logger);
        //act
        boolean result = fv.firstCousinShouldNotMarry();
        //assert
        verify(logger, Mockito.times(1)).error("US19", repository.GetIndividual("US19_IID2"), repository.GetFamilyForSpouse("US19_IID2", "US19_IID3"), "US19_IID2 is married to first cousin US19_IID3.");
        verifyNoMoreInteractions(logger);
        Assert.assertFalse(result);
    }

    //US20
    @Test
    public void auntAndUncleShouldNotMarryNieceOrNephwe() {
        //arrange
        GedLogger logger = mock(GedLogger.class);
        Load("test_us20", logger);
        //act
        boolean result = fv.auntAndUncleShouldNotMarryNieceOrNephwe();
        //assert
        verify(logger, Mockito.times(1)).error("US20", repository.GetIndividual("US20_IID1"), repository.GetFamilyForSpouse("US20_IID1", "US20_IID3"), "Aunt or uncle US20_IID1 is married to niece or nephew US20_IID3.");
        verifyNoMoreInteractions(logger);
        Assert.assertFalse(result);
    }
}
