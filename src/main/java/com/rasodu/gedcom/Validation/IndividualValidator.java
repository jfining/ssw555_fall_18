package com.rasodu.gedcom.Validation;

import java.util.List;

import com.rasodu.gedcom.Utils.GedLogger;
import com.rasodu.gedcom.core.Family;
import com.rasodu.gedcom.core.Individual;

public class IndividualValidator implements IValidator {

	GedLogger log = new GedLogger();

	List<Family> familyList;
	List<Individual> individualList;

	public IndividualValidator(List<Family> familyList, List<Individual> individualList) {
		super();
		this.familyList = familyList;
		this.individualList = individualList;
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
			if (ind.Id != null) {
				if (ind.Birthday.after(ind.Death)) {
					log.error(userStory, ind, null, "Death date is before birthday");
					valid = false;
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

		return allTestsValid;
	}

}
