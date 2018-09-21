package com.rasodu.gedcom.Validation;

import java.util.List;

import com.rasodu.gedcom.core.Family;
import com.rasodu.gedcom.core.Individual;

public class FamilyValidator implements IValidator {

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

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return true;
	}

}
