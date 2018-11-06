package com.rasodu.gedcom.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.lang.Iterable;

public class Individual {
    public String Id = null;
    public String Name = null;
    public char Gender = 'N';
    public Date Birthday = null;
    public Date Death = null;
    public String ChildOfFamily = null;
    public List<String> SpouseInFamily = new ArrayList<String>();
    
    //List a collection of people by id and name
    public static String ListPeople(Iterable<Individual> people) {
    	List<String> ids = new ArrayList<String>();
    	for(Individual person : people) {
    		ids.add(person.NameAndId());
    	}
    	return String.join(", ", ids);
    }
    
    public String NameAndId() {
    	String tempId;
		if(Id == null || Id.isEmpty()) {
			tempId = "No ID";
		} else {
			tempId = Id;
		}
		String tempName;
		if(Name == null || Name.isEmpty()) {
			tempName = "No Name";
		}
		else {
			tempName = Name;
		}
		return tempId + " (" + tempName + ")";
    }
}
