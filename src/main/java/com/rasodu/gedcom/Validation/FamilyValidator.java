package com.rasodu.gedcom.Validation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.rasodu.gedcom.Utils.GedLogger;
import com.rasodu.gedcom.core.Family;
import com.rasodu.gedcom.core.Individual;

public class FamilyValidator implements IValidator {

	GedLogger log = new GedLogger();

	List<Family> familyList;
	List<Individual> individualList;

	public FamilyValidator(List<Family> familyList, List<Individual> individualList) {
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

	// US02

	public boolean birthBeforeMarriage() {
		String userStory = "US02";
		boolean valid = true;

		for (Family fam : familyList) {
			for (Individual ind : individualList) {
				if (fam.Married != null) {
					if (fam.HusbandId.equals(ind.Id)) {
						if (fam.Married.before(ind.Birthday)) {
							log.error(userStory, null, fam, "Marriage is before Husband birthday.");
							valid = false;
						}
						if (fam.WifeId.equals(ind.Id)) {
							if (fam.Married.before(ind.Birthday)) {
								log.error(userStory, null, fam, "Marriage is before Wife birthday.");
								valid = false;
							}
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

		if (!birthBeforeMarriage()) {
			allTestsValid = false;
		}

		return allTestsValid;
	}

}
