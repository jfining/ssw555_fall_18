package com.rasodu.gedcom.Validation;

import com.rasodu.gedcom.Infrastructure.GedcomRepository;
import com.rasodu.gedcom.Utils.GedLogger;
import com.rasodu.gedcom.Utils.Helper;
import com.rasodu.gedcom.core.Family;
import com.rasodu.gedcom.core.IGedcomRepository;
import com.rasodu.gedcom.core.Individual;
import com.rasodu.gedcom.core.Spouse;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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

	// US07
	private boolean lessThan150YearsOld() {
		String userStory = "US07";
		boolean valid = true;
		Calendar calendar = Calendar.getInstance();
		Date today = new Date();

		for (Individual ind : individualList) {
			if (ind.Id != null) {
				if (ind.Birthday != null) {
					calendar.setTime(ind.Birthday);
					calendar.add(Calendar.YEAR, 150);
					if (ind.Death == null) {
						if (calendar.getTime().before(today)) {
							log.error(userStory, ind, null,
									"Individual " + ind.Id + " is living and over 150 years old");
							valid = false;
						}
					} else {
						if (calendar.getTime().before(ind.Death)) {
							log.error(userStory, ind, null, "Individual " + ind.Id + " is dead and over 150 years old");
							valid = false;
						}
					}
				}
			}
		}
		return valid;
	}

	// US08
	public boolean bornBeforeOrAfterMarriage() {
		Calendar calendar = Calendar.getInstance();
		// find family with anomaly
		boolean valid = true;
		for (Individual ind : individualList) {
			if (ind.Birthday == null || ind.ChildOfFamily == null || !repository.ContainsFamily(ind.ChildOfFamily)) {
				continue;
			}
			// logic to check error
			if (repository.GetFamily(ind.ChildOfFamily).Married != null
					&& ind.Birthday.before(repository.GetFamily(ind.ChildOfFamily).Married)) {
				valid = false;
				log.error("US08", ind, repository.GetFamily(ind.ChildOfFamily), "Individual born before marriage.");
			} else if (repository.GetFamily(ind.ChildOfFamily).Divorced != null) {
				calendar.setTime(repository.GetFamily(ind.ChildOfFamily).Divorced);
				calendar.add(Calendar.MONTH, 9);
				if (ind.Birthday.after(calendar.getTime())) {
					valid = false;
					log.error("US08", ind, repository.GetFamily(ind.ChildOfFamily),
							"Individual born 9 or more months after divorce.");
				}
			}
		}
		return valid;
	}

	// US22
	public boolean validateIdUniqueness() {
		boolean valid = true;
		HashMap<String, String> table = new HashMap<String, String>();
		for (Individual ind : individualList) {
			if (table.containsKey(ind.Id)) {
				valid = false;
				if (ind.Name.equals(table.get(ind.Id))) {
					log.error("US22", ind, null, "Individual is listed multiple times in the input data");
				} else {
					log.error("US22", ind, null,
							"Individual " + ind.Name + " shares their ID with " + table.get(ind.Id));
				}
			} else {
				table.put(ind.Id, ind.Name);
			}
		}
		return valid;
	}

	// US09
	public boolean bornAfterParentsDeath() {
		boolean valid = true;

		for (Individual ind : repository.GetAllIndividuals()) {

			Individual husband = repository.GetParentOfFamilyId(ind.ChildOfFamily, Spouse.Husband);
			Individual wife = repository.GetParentOfFamilyId(ind.ChildOfFamily, Spouse.Wife);

			if (null != husband && null != husband.Death
					&& Helper.DateGapLargerThenOnTimeLine(husband.Death, ind.Birthday, 9, Helper.PeriodUnit.Months)) {
				valid = false;
				log.error("US09", ind, repository.GetFamily(ind.ChildOfFamily),
						"Individual is born after 9 months of fathers death.");
			}
			if (null != wife && null != wife.Death && wife.Death.compareTo(ind.Birthday) < 0) {
				valid = false;
				log.error("US09", ind, repository.GetFamily(ind.ChildOfFamily),
						"Individual is bord after mother's death.");
			}
		}
		return valid;
	}

	// US12
	public boolean parentsNotTooOld() {
		String userStory = "US12";
		boolean valid = true;
		for (Individual ind : individualList) {
			if (ind.Birthday == null) {
				continue;
			}
			if (ind.ChildOfFamily != null) {
				Family fam = repository.GetFamily(ind.ChildOfFamily);
				if (fam == null || fam.HusbandId == null || fam.WifeId == null) {
					continue;
				}
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

	// US23
	public boolean uniqueNameBirthday() {
		String userStory = "US23";
		boolean valid = true;
		Set<String> s = new HashSet<>();
		for (Individual ind : individualList) {

			if (ind.Birthday != null && ind.Name != null) {

				String indNameBday = ind.Name + ind.Birthday.toString();

				if (s.add(indNameBday) == false) {
					log.error(userStory, ind, null, "Individual has same name and birthday as another user");
					valid = false;
				}
			}
		}

		return valid;
	}

	// US29
    public void printDeceased() {
	    for(Individual ind : repository.GetAllIndividuals()){
	        if(ind.Death != null){
	            log.info("US29", ind, null, ind.Id + " is deceased");
            }
        }
    }

    // US33
    public void printOrphans() {
	    for (Family fam : repository.GetAllFamilies()) {
	        Individual husband = repository.GetParentOfFamily(fam, Spouse.Husband);
            Individual wife = repository.GetParentOfFamily(fam, Spouse.Wife);
            if( husband != null && husband.Death != null && wife != null && wife.Death != null) {
                for(Individual ind : repository.GetChildrenOfFamily(fam)) {
                    log.info("US33", ind, null, ind.Id + " is orphan");
                }
            }
        }
    }

	// US35

	private boolean recentBirths() {
		String userStory = "US35";
		boolean valid = true;
		Date today = new Date();

		for (Individual ind : individualList) {
			if (ind.Id != null) {
				if (ind.Birthday != null) {
					long diff = today.getTime() - ind.Birthday.getTime();
					float daysBetween = (diff / (1000 * 60 * 60 * 24));
					if (daysBetween < 30) {
						log.error(userStory, ind, null, "Individual " + ind.Id + " was born within 30 days");
						valid = false;
					}
				}
			}
		}
		return valid;
	}

	// US36

	private boolean recentDeaths() {
		String userStory = "US36";
		boolean valid = true;
		Date today = new Date();

		for (Individual ind : individualList) {
			if (ind.Id != null) {
				if (ind.Death != null) {
					long diff = today.getTime() - ind.Death.getTime();
					float daysBetween = (diff / (1000 * 60 * 60 * 24));
					if (daysBetween < 30) {
						log.error(userStory, ind, null, "Individual " + ind.Id + " has passed away within 30 days");
						valid = false;
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
		if (!lessThan150YearsOld()) {
			allTestsValid = false;
		}
		if (!bornBeforeOrAfterMarriage()) {
			allTestsValid = false;
		}
		if (!bornAfterParentsDeath()) {
			allTestsValid = false;
		}
		if (!parentsNotTooOld()) {
			allTestsValid = false;
		}
		if (!uniqueNameBirthday()) {
			allTestsValid = false;
		}
		if (!validateIdUniqueness()) {
			allTestsValid = false;
		}
        printDeceased();
        printOrphans();
		if (!recentBirths()) {
			allTestsValid = false;
		}
		if (!recentDeaths()) {
			allTestsValid = false;
		}
		return allTestsValid;
	}

}
