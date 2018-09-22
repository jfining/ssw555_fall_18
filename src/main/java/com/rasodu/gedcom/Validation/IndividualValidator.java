package com.rasodu.gedcom.Validation;

import java.util.List;
import java.util.Calendar;
import java.util.Date;

import com.rasodu.gedcom.Utils.GedLogger;
import com.rasodu.gedcom.core.Family;
import com.rasodu.gedcom.core.Individual;

public class IndividualValidator implements IValidator {

	GedLogger log;

	List<Family> familyList;
	List<Individual> individualList;

	public IndividualValidator(List<Family> familyList, List<Individual> individualList, GedLogger log) {
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

	// US03

	public boolean birthBeforeDeath() {
		String userStory = "US03";
		boolean valid = true;

		for (Individual ind : individualList) {
			if (ind.Id != null && ind.Birthday != null && ind.Death != null) {
				if (ind.Birthday.after(ind.Death)) {
					log.error(userStory, ind, null, "Death date is before birthday");
					valid = false;
				}
			}
		}

		return valid;

	}

	
	//US07
	private boolean lessThan150YearsOld() {
		String userStory = "US07";
		boolean valid = true;
		Calendar calendar = Calendar.getInstance();
		Date today = new Date();
		
		for(Individual ind : individualList) {
			if(ind.Id != null) {
				if(ind.Birthday != null) {
					calendar.setTime(ind.Birthday);
					calendar.add(Calendar.YEAR, 150);
					if(ind.Death == null) {
						if(calendar.getTime().before(today)) {
							log.error(userStory, ind, null, "Individual " + ind.Id + " is living and over 150 years old");
							valid = false;
						}
					} else {
						if(calendar.getTime().before(ind.Death)) {
							log.error(userStory, ind, null, "Individual " + ind.Id + " is dead and over 150 years old");
							valid = false;
						}
					}
				}
			}
		}
		return valid;
	}
	@Override
	public boolean validate() {
		boolean allTestsValid = true;

		if (!birthBeforeDeath()) {
			allTestsValid = false;
		}
		if(!lessThan150YearsOld()) {
			allTestsValid = false;
		}

		return allTestsValid;
	}

}
