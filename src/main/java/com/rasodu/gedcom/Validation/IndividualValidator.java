package com.rasodu.gedcom.Validation;

import java.util.List;
import java.util.Map;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import com.rasodu.gedcom.Infrastructure.GedcomRepository;
import com.rasodu.gedcom.Utils.GedLogger;
import com.rasodu.gedcom.core.Family;
import com.rasodu.gedcom.core.IGedcomRepository;
import com.rasodu.gedcom.core.Individual;

public class IndividualValidator implements IValidator {

	GedLogger log;

	List<Family> familyList;
	List<Individual> individualList;
	IGedcomRepository repository;

	public IndividualValidator(List<Family> familyList, List<Individual> individualList, GedLogger log) {
		super();
		this.familyList = familyList;
		this.individualList = individualList;
		this.log = log;
		repository = new GedcomRepository(individualList, familyList);
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

	//US08
	public boolean bornBeforeOrAfterMarriage() {
		Calendar calendar = Calendar.getInstance();
		//find family with anomaly
		boolean valid = true;
		for(Individual ind : individualList) {
			if(ind.Birthday == null || ind.ChildOfFamily == null || !repository.ContainsFamily(ind.ChildOfFamily) ) {
				continue;
			}
			//logic to check error
			if(repository.GetFamily(ind.ChildOfFamily).Married != null && ind.Birthday.before(repository.GetFamily(ind.ChildOfFamily).Married)) {
				valid = false;
				log.error("US08", ind, repository.GetFamily(ind.ChildOfFamily), "Individual born before marriage.");
			}
			else if(repository.GetFamily(ind.ChildOfFamily).Divorced != null) {
				calendar.setTime(repository.GetFamily(ind.ChildOfFamily).Divorced);
				calendar.add(Calendar.MONTH, 9);
				if(ind.Birthday.after(calendar.getTime())) {
					valid = false;
					log.error("US08", ind, repository.GetFamily(ind.ChildOfFamily), "Individual born 9 or more months after divorce.");
				}
			}
		}
		return valid;
	}
	
	//US22
	public boolean validateIdUniqueness() {
		boolean valid = true;
		HashMap<String, String> table = new HashMap<String, String>();
		for(Individual ind: individualList) {
			if(table.containsKey(ind.Id)) {
				valid = false;
				if(ind.Name.equals(table.get(ind.Id))){
					log.error("US22", ind, null, "Individual is listed multiple times in the input data");
				} else {
					log.error("US22", ind, null, "Individual " + ind.Name + " shares their ID with " + table.get(ind.Id));
				}
			} else {
				table.put(ind.Id, ind.Name);
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
		if(!bornBeforeOrAfterMarriage()) {
			allTestsValid = false;
		}
		if(!validateIdUniqueness()) {
			allTestsValid = false;
		}

		return allTestsValid;
	}

}
