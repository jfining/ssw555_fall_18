package com.rasodu.gedcom.Validation;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.Calendar;
import java.util.Date;

import com.rasodu.gedcom.Infrastructure.GedcomRepository;
import com.rasodu.gedcom.Utils.GedLogger;
import com.rasodu.gedcom.Utils.Helper;
import com.rasodu.gedcom.core.Family;
import com.rasodu.gedcom.core.IGedcomRepository;
import com.rasodu.gedcom.core.Individual;
import com.rasodu.gedcom.core.Spouse;

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

	//US09
	public boolean bornAfterParentsDeath()
	{
		boolean valid = true;
		for(Individual ind : repository.GetAllIndividuals())
		{
			Individual husband = repository.GetParentOfFamilyId(ind.ChildOfFamily, Spouse.Husband);
			Individual wife = repository.GetParentOfFamilyId(ind.ChildOfFamily, Spouse.Wife);
			if(null != husband && null != husband.Death && Helper.DateGapLargerThenOnTimeLine(husband.Death, ind.Birthday,9, Helper.PeriodUnit.Months)) {
				valid = false;
				log.error("US09", ind, repository.GetFamily(ind.ChildOfFamily), "Individual is born after 9 months of fathers death.");
			}
			if(null != wife && null != wife.Death && wife.Death.compareTo(ind.Birthday) < 0){
				valid = false;
				log.error("US09", ind, repository.GetFamily(ind.ChildOfFamily), "Individual is bord after mother's death.");
			}
		}
		return valid;
	}
	
	//US12
	public boolean parentsNotTooOld() {
		String userStory = "US12";
		boolean valid = true;
		for (Individual ind : individualList) {
			if (ind.Birthday == null) {
				continue;
			}
			if (ind.ChildOfFamily != null) {
				Family fam = repository.GetFamily(ind.ChildOfFamily);
				Individual father = repository.GetIndividual(fam.HusbandId);
				Individual mother = repository.GetIndividual(fam.WifeId);
				if (father == null || mother == null) {
					continue;
				}
				if (father.Birthday == null || mother.Birthday == null) {
					continue;
				}
				long sixtyYearsInDays = 60 * 365;
				long eightyYearsInDays = 80 * 365;
				long diffFromMotherInMillies = ind.Birthday.getTime() - mother.Birthday.getTime();
				long diffFromFatherInMillies = ind.Birthday.getTime() - father.Birthday.getTime();
				long diffFromMotherInDays = TimeUnit.DAYS.convert(diffFromMotherInMillies, TimeUnit.MILLISECONDS);
				long diffFromFatherInDays = TimeUnit.DAYS.convert(diffFromFatherInMillies, TimeUnit.MILLISECONDS);

				if (diffFromMotherInDays > sixtyYearsInDays) {
					log.error(userStory, ind, fam, "Individual cannot be greater than 60 years younger than mother.");
					valid = false;
				}
				if (diffFromFatherInDays > eightyYearsInDays) {
					log.error(userStory, ind, fam, "Individual cannot be greater than 80 years younger than father.");
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
		if(!lessThan150YearsOld()) {
			allTestsValid = false;
		}
		if(!bornBeforeOrAfterMarriage()) {
			allTestsValid = false;
		}
		if (!bornAfterParentsDeath()) {
			allTestsValid = false;
		}
		if (!parentsNotTooOld()) {
			allTestsValid = false;
		}

		return allTestsValid;
	}

}
