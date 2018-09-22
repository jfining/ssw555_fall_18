package com.rasodu.gedcom.Validation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rasodu.gedcom.Utils.GedLogger;
import com.rasodu.gedcom.core.Family;
import com.rasodu.gedcom.core.Individual;

public class FamilyValidator implements IValidator {


	GedLogger log;

	List<Family> familyList;
	List<Individual> individualList;
	
	public FamilyValidator(List<Family> familyList, List<Individual> individualList, GedLogger log) {
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
	
	//US05
	public boolean MarriageBeforeDeath() {
		String userStory = "US05";
		boolean valid = true;
		
		for(Family fam : familyList) {
			for(Individual ind : individualList) {
				if(fam.Married != null && ind.Death != null) {
					if(ind.Id.equals(fam.HusbandId)) {
						if(fam.Married.after(ind.Death)) {
							log.error(userStory, ind, fam, "Family " + fam.Id + " was married after husband " + fam.HusbandId + "'s date of death");
							valid = false;
						}
					}
					else if(ind.Id.equals(fam.WifeId)) {
						if(fam.Married.after(ind.Death)) {
							log.error(userStory, ind, fam, "Family " + fam.Id + " was married after wife " + fam.WifeId + "'s date of death");
							valid = false;
						}
					}
				}
			}
		}
		return valid;
	}

	//US06
	public boolean noDivorceAfterDeath() {
		//index family by id
		Map<String, Family> findFamily = new HashMap<String, Family>();
		for(Family fam : familyList) {
			findFamily.put(fam.Id, fam);
		}
		//find family with anomaly
		boolean valid = true;
		for(Individual ind : individualList) {
			if(ind.Death == null) {
				continue;
			}
			for(String famId : ind.SpouseInFamily) {
				if(!findFamily.containsKey(famId)) {
					continue;
				}
				Family fam = findFamily.get(famId);
				if(fam.Divorced.compareTo(ind.Death) > 0){
					valid = false;
					log.error("US06", ind, fam, "Individual divorced after death.");
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
		if (!MarriageBeforeDeath()) {
			allTestsValid = false;
		}
		if (!noDivorceAfterDeath()) {
			allTestsValid = false;
		}

		return allTestsValid;
	}

}
