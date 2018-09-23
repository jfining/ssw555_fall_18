package com.rasodu.gedcom.Validation;

import java.util.Date;
import java.util.List;

import com.rasodu.gedcom.Utils.GedLogger;
import com.rasodu.gedcom.core.Family;
import com.rasodu.gedcom.core.Individual;

public class GeneralValidator implements IValidator {
	
	GedLogger log;
	
	List<Family> familyList;
	List<Individual> individualList;
	
	public GeneralValidator(List<Family> familyList, List<Individual> individualList, GedLogger log) {
		super();
		this.familyList = familyList;
		this.individualList = individualList;
		this.log = log;
	}
	public List<Family> getFamilyList() {
		return familyList;
	}
	public void setFamilyList(List<Family> familyList) {
		this.familyList = familyList;
	}
	public List<Individual> getIndividualList() {
		return individualList;
	}
	public void setIndividualList(List<Individual> individualList) {
		this.individualList = individualList;
	}
	
	public boolean noDatesBeforeCurrentDate() {
		String userStory = "US01";
		boolean valid = true;
		Date currentDate = new Date();
		
		for (Family fam : familyList) {
			if (fam.Divorced != null) {
				if( fam.Divorced.after(currentDate)) {
					log.error(userStory, null, fam, "Divorced Date is after current date.");
					valid = false;
				}
			}
			if (fam.Married != null) {
				if( fam.Married.after(currentDate)) {
					log.error(userStory, null, fam, "Marriage Date is after current date.");
					valid = false;
				}
			}
		}
		
		for (Individual ind : individualList) {
			if (ind.Birthday == null) {
				log.error(userStory, ind, null, "Individual does not have a birthdate.");
				valid = false;
			}
			else {
				if (ind.Birthday.after(currentDate)) {
					log.error(userStory, ind, null, "Birth Date is after current date.");
					valid = false;
				}
			}
			if (ind.Death != null) {
				if (ind.Death.after(currentDate)) {
					log.error(userStory, ind, null, "Death Date is after current date.");
					valid = false;
				}
			}
		}
		
		return valid;
	}
	
	@Override
	public boolean validate() {
		boolean allTestsValid = true;
		
		if (!noDatesBeforeCurrentDate()) {
			allTestsValid = false;
		}
		
		return allTestsValid;
	}
}
