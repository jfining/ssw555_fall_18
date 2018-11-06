package com.rasodu.gedcom.Utils;

import com.rasodu.gedcom.core.Family;
import com.rasodu.gedcom.core.Individual;

public class GedLogger {

    public void error(String userStory, Individual ind, Family fam, String message) {
        print("Error", userStory, ind, fam, message);
    }

    public void anomaly(String userStory, Individual ind, Family fam, String message) {
        print("Anomaly", userStory, ind, fam, message);
    }
    
    public void info(String userStory, Individual ind, Family fam, String message) {
    	print("Info", userStory, ind, fam, message);
    }
    
    private void print(String category, String userStory, Individual ind, Family fam, String message) {
    	String indId;
        String famId;

        if (ind == null) {
            indId = "n/a";
        } else {
            indId = ind.Id;
        }
        if (fam == null) {
            famId = "n/a";
        } else {
            famId = fam.Id;
        }
        System.out.println(category + " " + userStory + ": IndividualId=" + indId + " FamilyId=" + famId + " message=" + message);
    }
}
