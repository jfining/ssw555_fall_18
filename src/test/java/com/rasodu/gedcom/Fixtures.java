package com.rasodu.gedcom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import com.rasodu.gedcom.core.*;
public class Fixtures {

	private static SimpleDateFormat dateFormat;
	public static ArrayList<Individual> indiList;
	public static ArrayList<Family> famList;
	
	public static void SetupTestFixtures() {
		dateFormat = new SimpleDateFormat("yyyyMMdd");
		try {
			SetupIndividualFixtures();
			SetupFamilyFixtures();
		} catch(Exception e){
			
		}
	}
	
	private static void AddIndividual(String id, String birthday, String name,
			char gender, String death) throws ParseException {
		Individual indi = new Individual();
		indi.Id = id;
		if(!birthday.isEmpty()) {
			indi.Birthday = dateFormat.parse(birthday);
		}
		indi.Name = name;
		indi.Gender = gender;
		if(!death.isEmpty()) {
			indi.Death = dateFormat.parse(death);
		}
		indiList.add(indi);
	}
	
	private static void AddFamily(String id, String married, String husbId, String wifeId,
			String divorced, String[] childIds) throws ParseException{
		Family fam = new Family();
		fam.Id = id;
		if(!married.isEmpty()) {
			fam.Married = dateFormat.parse(married);
		}
		fam.HusbandId = husbId;
		fam.WifeId = wifeId;
		if(!divorced.isEmpty()) {
			fam.Divorced = dateFormat.parse(divorced);
		}
		fam.ChildrenIds = Arrays.asList(childIds);
		famList.add(fam);
	}
	
	private static void SetupIndividualFixtures() throws ParseException {
		indiList = new ArrayList<Individual>();
		
		AddIndividual("indi00", "19600101", "Mister /Max", 'M', "");
		AddIndividual("indi01", "19650312", "SecondWife /Max", 'F', "");
		AddIndividual("indi02", "19580731", "DeadWife /Max", 'F', "19850101");
		AddIndividual("indi03", "19720804", "Dead /Husband", 'M', "19881230");
		
	}
	
	private static void SetupFamilyFixtures() throws ParseException {
		famList = new ArrayList<Family>();
		
		//Family 00: Tests US05 husband and wife alive
		AddFamily("fam00", "19920229", "indi00", "indi01", "", new String[0]);
		
		//Family 01: Tests US05 wife early death, husband alive
		AddFamily("fam01", "19900101", "indi00", "indi02", "", new String[0]);
		
		//Family 02: Tests US05 husband early death, wife alive
		AddFamily("fam02", "19890101", "indi03", "indi01", "", new String[0]);
		
		//Family 03: Tests US05 wife and husband early death
		AddFamily("fam03", "20000101", "indi02", "indi03", "", new String[0]);
		
		//Family 04: Tests US05 husband alive, wife death after marriage
		AddFamily("fam04", "19830515", "indi00", "indi02", "", new String[0]);
		
		//Family 05: Tests US01 divorce before death
		AddFamily("fam05", "19700101", "indi03", "indi01", "19860101", new String[0]);
		
		//Family 06: Tests US01 divorce after death
		AddFamily("fam06", "19700101", "indi03", "indi01", "20160214", new String[0]);
		
		//Family 07: Tests US04 divorce before marriage
		AddFamily("fam07", "19800101", "indi00", "indi01", "19790101", new String[0]);
		
		//Family 08: Tests US04 divorce after marriage
		AddFamily("fam08", "19790101", "indi00", "indi01", "19800101", new String[0]);
	}
}
