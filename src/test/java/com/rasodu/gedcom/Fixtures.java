package com.rasodu.gedcom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.rasodu.gedcom.core.*;
public class Fixtures {

	private static SimpleDateFormat dateFormat;
	public static ArrayList<Individual> indiList;
	public static ArrayList<Family> famList;
	
	public static void SetupTestFixtures() {
		try {
			SetupIndividualFixtures();
			SetupFamilyFixtures();
		} catch(Exception e){
			
		}
	}
	
	private static void SetupIndividualFixtures() throws ParseException {
		indiList = new ArrayList<Individual>();
		dateFormat = new SimpleDateFormat("yyyyMMdd");
		
		//Individual 00
		Individual indi00 = new Individual();
		indi00.Id = "indi00";
		indi00.Birthday = dateFormat.parse("19600101");
		indi00.Name = "Mister /Max/";
		indi00.Gender = 'M';
		indiList.add(indi00);
		
		//Individual 01
		Individual indi01 = new Individual();
		indi01.Id = "indi01";
		indi01.Birthday = dateFormat.parse("19650312");
		indi01.Name = "SecondWife /Max/";
		indi01.Gender = 'F';
		indiList.add(indi01);
		
		//Individual 02
		Individual indi02 = new Individual();
		indi02.Id = "indi02";
		indi02.Birthday = dateFormat.parse("19580731");
		indi02.Name = "DeadWife /Max/";
		indi02.Gender = 'F';
		indi02.Death = dateFormat.parse("19850101");
		indiList.add(indi02);
		
		//Individual 03
		Individual indi03 = new Individual();
		indi03.Id = "indi03";
		indi03.Birthday = dateFormat.parse("19720804");
		indi03.Name = "Dead /Husband/";
		indi03.Gender = 'M';
		indi03.Death = dateFormat.parse("19881230");
		indiList.add(indi03);
	}
	
	private static void SetupFamilyFixtures() throws ParseException {
		famList = new ArrayList<Family>();
		
		//Family 00: Tests US05 husband and wife alive
		Family fam00 = new Family();
		fam00.Id = "fam00";
		fam00.Married = dateFormat.parse("19920229");
		fam00.HusbandId = "indi00";
		fam00.WifeId = "indi01";
		famList.add(fam00);
		
		//Family 01: Tests US05 wife early death, husband alive
		Family fam01 = new Family();
		fam01.Id = "fam01";
		fam01.Married = dateFormat.parse("19900101");
		fam01.HusbandId = "indi00";
		fam01.WifeId = "indi02";
		famList.add(fam01);
		
		//Family 02: Tests US05 husband early death, wife alive
		Family fam02 = new Family();
		fam02.Id = "fam02";
		fam02.Married = dateFormat.parse("19890101");
		fam02.HusbandId = "indi03";
		fam02.WifeId = "indi01";
		famList.add(fam02);
		
		//Family 03: Tests US05 wife and husband early death
		Family fam03 = new Family();
		fam03.Id = "fam03";
		fam03.Married = dateFormat.parse("20000101");
		fam03.HusbandId = "indi02";
		fam03.WifeId = "indi03";
		famList.add(fam03);
		
		//Family 04: Tests US05 husband alive, wife death after marriage
		Family fam04 = new Family();
		fam04.Id = "fam04";
		fam04.Married = dateFormat.parse("19830515");
		fam04.HusbandId = "indi00";
		fam04.WifeId = "indi02";
		famList.add(fam04);
	}
}
