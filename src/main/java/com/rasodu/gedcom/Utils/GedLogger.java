package com.rasodu.gedcom.Utils;

import com.rasodu.gedcom.core.Family;
import com.rasodu.gedcom.core.Individual;

public class GedLogger {

    public void error(String userStory, Individual ind, Family fam, String message) {
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
        System.out.println("Error " + userStory + ": IndividualId=" + indId + " FamilyId=" + famId + " message=" + message);
    }

    public void anomaly(String userStory, Individual ind, Family fam, String message) {
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
        System.out.println("Anomaly " + userStory + ": IndividualId=" + indId + " FamilyId=" + famId + " message=" + message);
    }
}
