package com.rasodu.gedcom.Validation;

import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
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
		for (Family fam : familyList) {
			if (fam.Married != null) {
				Individual husband = repository.GetParentOfFamily(fam, Spouse.Husband);
				Individual wife = repository.GetParentOfFamily(fam, Spouse.Wife);

				// US02: Parent birthdays must be before marriage date
				if (husband != null && husband.Birthday != null && !husband.Birthday.before(fam.Married)) {
					log.error("US02", husband, fam, "Marriage is before Husband birthday.");
					valid = false;
				}
				if (wife != null && wife.Birthday != null && !wife.Birthday.before(fam.Married)) {
					log.error("US02", null, fam, "Marriage is before Wife birthday.");
					valid = false;
				}

				// US04: Divorce must be after marriage
				if (fam.Divorced != null) {
					if (!fam.Divorced.after(fam.Married)) {
						log.error("US04", null, fam, "Divorce Date is before marriage date.");
						valid = false;
					}
				}

				// US05: Marriage must be before parent deaths
				if (husband != null && husband.Death != null && fam.Married.after(husband.Death)) {
					log.error("US05", husband, fam, "Family was married after husband's date of death");
					valid = false;
				}
				if (wife != null && wife.Death != null && fam.Married.after(wife.Death)) {
					log.error("US05", wife, fam, "Family was married after wife's date of death");
					valid = false;
				}

				// US10 Marriage must be after 14
				if ((husband != null && husband.Birthday != null) && (wife != null && wife.Birthday != null) ) {

					long husDiff = fam.Married.getTime() - husband.Birthday.getTime();
					long wifeDiff = fam.Married.getTime() - wife.Birthday.getTime();

					if (husDiff < 14) {
						log.error("US10", husband, fam, "Husband married before 14");
						valid = false;
					}
					if (wifeDiff < 14) {
						log.error("US10", wife, fam, "Wife married before 14");
						valid = false;
					}
				}
			}
		}
		return valid;
	}

	// US06
	public boolean noDivorceAfterDeath() {
		boolean valid = true;
		for (Family fam : familyList) {
			if (fam.Divorced != null) {
				Individual husband = repository.GetParentOfFamily(fam, Spouse.Husband);
				Individual wife = repository.GetParentOfFamily(fam, Spouse.Wife);

				if (husband != null && husband.Death != null && fam.Divorced.after(husband.Death)) {
					log.error("US06", husband, fam, "Individual divorced after death.");
					valid = false;
				}
				if (wife != null && wife.Death != null && fam.Divorced.after(wife.Death)) {
					log.error("US06", wife, fam, "Individual divorced after death.");
					valid = false;
				}
			}
		}
		return valid;
	}

	//US13, US14
	public boolean validateChildBirthdays() {
		boolean valid = true;
		for(Family fam: familyList) {
			List<Individual> children = repository.GetChildrenOfFamily(fam);
			
			//sort children by birthday
			children = insertionSortByBirthday(children);
			if(children.isEmpty()) {
				continue;
			}
			
			//if more than 5 children born at once, throw error US14
			Calendar calendar = Calendar.getInstance();
			List<Individual> simultaneousBirths;
			while(children.size() > 0) {
				simultaneousBirths = new ArrayList<Individual>();
				calendar.setTime(children.get(0).Birthday);
				calendar.add(Calendar.DATE, 1);
				Date maxTwinBirthday = calendar.getTime();
				calendar.add(Calendar.DATE, -1);
				calendar.add(Calendar.MONTH, 9);
				Date minSiblingBirthday = calendar.getTime();
				String oldestChildId = children.get(0).Id;
				Individual[] tempChildren = children.toArray(new Individual[0]);
				//iterate over children, tracking birthdays
				//if child is between 1 day and 9 months apart from siblings, throw error US13
				for(Individual child : tempChildren) {
					if(!child.Birthday.before(minSiblingBirthday)) {
						//We don't care about kids born 9 months later right now
						break;
					}
					if(child.Birthday.after(maxTwinBirthday)) {
						log.error("US13", child, fam, "Child born between 1 day and 9 months after older sibling " + oldestChildId);
						valid = false;
						continue;
					}
					simultaneousBirths.add(child);
					children.remove(child);
				}
				if(simultaneousBirths.size() > 5) {
					valid = false;
					for(Individual child : simultaneousBirths){
						log.error("US14", child, fam, "Child has 5 or more siblings born at the same time");
					}
				}
			}
		}
		return valid;
	}

	// US15
	public boolean noMoreThanFifteenSiblings() {
		boolean valid = true;
		for(Family family : repository.GetAllFamilies()){
			if(15 < family.ChildrenIds.size()) {
				valid = false;
				log.anomaly("US15", null, family, "The family has more than 15 siblings");
			}
		}
		return valid;
	}
	
	// US16 Male last names
	public boolean noMaleDifferentName() {
		boolean valid = true;
		for (Family fam : familyList) {
			if (fam.Married != null && fam.ChildrenIds != null) {
				Individual husband = repository.GetParentOfFamily(fam, Spouse.Husband);
				if(husband == null) {
					continue;
				}
				String[] husbNameSplit = husband.Name.split("/");
				if(husbNameSplit.length < 2) {
					continue;
				}
				for(Individual child : repository.GetChildrenOfFamily(fam)) {
					String[] indNamesplit = child.Name.split("/");
					if(indNamesplit.length < 2) {
						continue;
					}
					if (!husbNameSplit[1].equals(indNamesplit[1])) {
						valid = false;
						log.error("US16", child, fam,
								"Individual last name is different than Father'");
					}
				}
				
			}
		}
		return valid;
	}
	
	//US11
	public boolean noBigamy() {
		String userStory = "US11";
		boolean valid = true;
		for (Individual ind : individualList) {
			if (ind.SpouseInFamily.size() > 1) {
				List<Family> families = new ArrayList<Family>();
				for (String famId : ind.SpouseInFamily) {
					families.add(repository.GetFamily(famId));
				}
				for (Family fam : families) {
					for (Family fam2 : families) {
						if (fam == fam2) {
							continue;
						}
						if (fam.Divorced == null) {
							if (fam2.Divorced == null) { 
								log.error(userStory, ind, fam, "Individual cannot be in more than one marriage.");
								valid = false;
							}
							else if (fam.Married.before(fam2.Divorced)) {
								log.error(userStory, ind, fam, "Individual cannot be in more than one marriage.");
								valid = false;
							}
						}
						else {
							if (fam2.Divorced == null) {
								if (fam2.Married.before(fam.Divorced)) {
									log.error(userStory, ind, fam, "Individual cannot be in more than one marriage.");
									valid = false;
								}
							}
							else if (fam2.Married.before(fam.Divorced) || fam.Married.before(fam2.Divorced)) {
								log.error(userStory, ind, fam, "Individual cannot be in more than one marriage.");
								valid = false;
							}
						}
						
					}
				}
			}
		}
		return valid;
	}
	
	private List<Individual> insertionSortByBirthday(List<Individual> people){
		int count = people.size();
		int oldestIndex = -1;
		Date oldestBirthday;
		boolean nonNullFound = true;
		List<Individual> sorted = new ArrayList<Individual>();
		while(sorted.size() < count && nonNullFound ) {
			nonNullFound = false;
			oldestBirthday = new Date();
			oldestIndex = -1;
			//Get the oldest non-null birthday and add the person to the list
			for(int i = 0; i < people.size(); i++) {
				Date birthday = people.get(i).Birthday;
				if(birthday == null) {
					continue;
				}
				nonNullFound = true;
				if(birthday.before(oldestBirthday)) {
					oldestIndex = i;
					oldestBirthday = birthday;
				}
			}
			if(nonNullFound) {
				sorted.add(people.get(oldestIndex));
				people.remove(oldestIndex);
			}
		}
		return sorted;
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
		if (!validateChildBirthdays()) {
			allTestsValid = false;
		}
		if (!noMaleDifferentName()) {
			allTestsValid= false;
		}
		if (!noBigamy()) {
			allTestsValid = false;
		}
		if (!noMoreThanFifteenSiblings()){
			allTestsValid = false;
		}

		return allTestsValid;
	}

}
