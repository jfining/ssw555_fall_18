package com.rasodu.gedcom.Validation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rasodu.gedcom.Infrastructure.GedcomRepository;
import com.rasodu.gedcom.Utils.GedLogger;
import com.rasodu.gedcom.core.Family;
import com.rasodu.gedcom.core.IGedcomRepository;
import com.rasodu.gedcom.core.Individual;
import com.rasodu.gedcom.core.Spouse;

public class FamilyValidator implements IValidator {


	GedLogger log;

	List<Family> familyList;
	List<Individual> individualList;
	IGedcomRepository repository;
	
	public FamilyValidator(List<Family> familyList, List<Individual> individualList, GedLogger log) {
		super();
		this.familyList = familyList;
		this.individualList = individualList;
		this.log = log;
		updateRepository();
	}

	public List<Family> getFamilyList() {
		return familyList;
	}

	public void setFamilyList(List<Family> familyList) {
		this.familyList = familyList;
		updateRepository();
	}

	public List<Individual> getIndividualList() {
		return individualList;
	}

	public void setIndividualList(List<Individual> individualList) {
		this.individualList = individualList;
		updateRepository();
	}
	
	private void updateRepository() {
		repository = new GedcomRepository(individualList, familyList);
	}
	
	public boolean validateMarriageDate() {
		boolean valid = true;
		for(Family fam : familyList) {
			if(fam.Married != null) {
				Individual husband = repository.GetParentOfFamily(fam, Spouse.Husband);
				Individual wife = repository.GetParentOfFamily(fam, Spouse.Wife);
				
				//US02: Parent birthdays must be before marriage date
				if(husband != null && husband.Birthday != null && !husband.Birthday.before(fam.Married)) {
					log.error("US02", husband, fam, "Marriage is before Husband birthday.");
					valid = false;
				}
				if(wife != null && wife.Birthday != null && !wife.Birthday.before(fam.Married)) {
					log.error("US02", null, fam, "Marriage is before Wife birthday.");
					valid = false;
				}
				
				//US04: Divorce must be after marriage
				if(fam.Divorced != null) {
					if(!fam.Divorced.after(fam.Married)) {
						log.error("US04", null, fam, "Divorce Date is before marriage date.");
						valid = false;
					}
				}
				
				//US05: Marriage must be before parent deaths
				if(husband != null && husband.Death != null && fam.Married.after(husband.Death)) {
					log.error("US05", husband, fam, "Family was married after husband's date of death");
					valid = false;
				}
				if(wife != null && wife.Death != null && fam.Married.after(wife.Death)) {
					log.error("US05", wife, fam, "Family was married after wife's date of death");
					valid = false;
				}
			}
		}
		return valid;
	}

	//US06
	public boolean noDivorceAfterDeath() {
		boolean valid = true;
		for(Family fam : familyList) {
			if(fam.Divorced != null) {
				Individual husband = repository.GetParentOfFamily(fam, Spouse.Husband);
				Individual wife = repository.GetParentOfFamily(fam, Spouse.Wife);
				
				if(husband != null && husband.Death != null && fam.Divorced.after(husband.Death)) {
					log.error("US06", husband, fam, "Individual divorced after death.");
					valid = false;
				}
				if(wife != null && wife.Death != null && fam.Divorced.after(wife.Death)) {
					log.error("US06", wife, fam, "Individual divorced after death.");
					valid = false;
				}
			}
		}
		return valid;
	}

	@Override
	public boolean validate() {
		boolean allTestsValid = true;

		if (!validateMarriageDate()) {
			allTestsValid = false;
		}
		if (!noDivorceAfterDeath()) {
			allTestsValid = false;
		}

		return allTestsValid;
	}

}
