package com.rasodu.gedcom.Infrastructure;

import com.rasodu.gedcom.core.Family;
import com.rasodu.gedcom.core.IGedcomRepository;
import com.rasodu.gedcom.core.Individual;
import com.rasodu.gedcom.core.Spouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GedcomRepository implements IGedcomRepository {
    private List<Individual> individuals;
    private List<Family> families;
    private Map<String, Individual> individualsMap;
    private Map<String, Family> familiesMap;

    public GedcomRepository(List<Individual> _individuals, List<Family> _families) {
        individuals = _individuals;
        families = _families;
        indexData();
    }

    private void indexData() {
        indexIndivisuals();
        indexFamilies();
    }

    private void indexIndivisuals() {
        individualsMap = new HashMap<>();
        if (individuals == null) {
            return;
        }
        for (Individual ind : individuals) {
            individualsMap.put(ind.Id, ind);
        }
    }

    private void indexFamilies() {
        familiesMap = new HashMap<>();
        if (families == null) {
            return;
        }
        for (Family fam : families) {
            familiesMap.put(fam.Id, fam);
        }
    }

    public List<Individual> GetAllIndividuals() {
        return individuals;
    }

    public List<Family> GetAllFamilies() {
        return families;
    }


    public boolean ContainsIndividual(String IndivisualId) {
        return individualsMap.containsKey(IndivisualId);
    }

    public Individual GetIndividual(String IndividualId) {
        return individualsMap.get(IndividualId);
    }

    public boolean ContainsFamily(String FamilyId) {
        return familiesMap.containsKey(FamilyId);
    }

    public Family GetFamily(String FamilyId) {
        return familiesMap.get(FamilyId);
    }

    public Individual GetParentOfFamily(Family family, Spouse spouse) {
        String parentId;
        if (spouse == Spouse.Husband) {
            parentId = family.HusbandId;
        } else {
            parentId = family.WifeId;
        }
        if (ContainsIndividual(parentId)) {
            return GetIndividual(parentId);
        }
        return null;
    }

    public List<Individual> GetChildrenOfFamily(Family family) {
        List<Individual> children = new ArrayList<Individual>();
        for (String childId : family.ChildrenIds) {
            if (ContainsIndividual(childId)) {
                children.add(GetIndividual(childId));
            }
        }
        return children;
    }

    public Individual GetParentOfFamilyId(String familyId, Spouse spouse) {
        if (ContainsFamily(familyId)) {
            return GetParentOfFamily(GetFamily(familyId), spouse);
        }
        return null;
    }
}
