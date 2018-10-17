package com.rasodu.gedcom.Infrastructure;

import com.rasodu.gedcom.core.Family;
import com.rasodu.gedcom.core.IGedcomRepository;
import com.rasodu.gedcom.core.Individual;
import com.rasodu.gedcom.core.Spouse;

import java.util.*;


public class GedcomRepository implements IGedcomRepository {
    private List<Individual> individuals;
    private List<Family> families;
    private Map<String, Individual> individualsMap;
    private Map<String, Family> familiesMap;
    private Map<String, Family> familiesMapOnHusbandAndWife;
    private Map<String, HashSet<Family>> familiesMapOnIndividualId;

    public GedcomRepository(List<Individual> _individuals, List<Family> _families) {
        individuals = _individuals;
        families = _families;
        indexData();
    }

    private void indexData() {
        indexIndivisuals();
        indexFamilies();
        indexFamiliesOnSpouse();
        indexFamiliesOnIndividualId();
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

    private void indexFamiliesOnSpouse() {
        familiesMapOnHusbandAndWife = new HashMap<>();
        if (families == null) {
            return;
        }
        for (Family fam : families) {
            if (fam.HusbandId != null && fam.WifeId != null) {
                familiesMapOnHusbandAndWife.put(fam.HusbandId + fam.WifeId, fam);
                familiesMapOnHusbandAndWife.put(fam.WifeId + fam.HusbandId, fam);
            }
        }
    }

    private void indexFamiliesOnIndividualId() {
        familiesMapOnIndividualId = new HashMap<>();
        if (families == null) {
            return;
        }
        HashSet<Family> familyListForIndividual;
        for (Family fam : families) {
            //add husband family
            if (fam.HusbandId != null) {

                if (familiesMapOnIndividualId.containsKey(fam.HusbandId)) {
                    familyListForIndividual = familiesMapOnIndividualId.get(fam.HusbandId);
                } else {
                    familyListForIndividual = new HashSet<>();
                }
                familyListForIndividual.add(fam);
                familiesMapOnIndividualId.put(fam.HusbandId, familyListForIndividual);

            }
            //add wife family
            if (fam.WifeId != null) {
                if (familiesMapOnIndividualId.containsKey(fam.WifeId)) {
                    familyListForIndividual = familiesMapOnIndividualId.get(fam.WifeId);
                } else {
                    familyListForIndividual = new HashSet<>();
                }
                familyListForIndividual.add(fam);
                familiesMapOnIndividualId.put(fam.WifeId, familyListForIndividual);
            }
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

    private List<Individual> GetChildrenOfFamilyId(String familyId) {
        if (ContainsFamily(familyId)) {
            return GetChildrenOfFamily(GetFamily(familyId));
        }
        return new ArrayList<>();
    }

    public Individual GetParentOfFamilyId(String familyId, Spouse spouse) {
        if (ContainsFamily(familyId)) {
            return GetParentOfFamily(GetFamily(familyId), spouse);
        }
        return null;
    }

    public boolean HasFamilyForSpouse(String spouse1Id, String spouse2Id) {
        return familiesMapOnHusbandAndWife.containsKey(spouse1Id + spouse2Id);
    }

    public Family GetFamilyForSpouse(String spouse1Id, String spouse2Id) {
        return familiesMapOnHusbandAndWife.get(spouse1Id + spouse2Id);
    }

    /*
    level = 0;//Siblings
    level = 1;//First cousin
    ...
     */
    public List<Individual> GetChildrenAtLevel(Family family, int level) {
        HashSet<Family> families = new HashSet<Family>();
        families.add(family);//For cases when HusbandId and WifeId is null
        families.addAll(GetFamiliesForSpouse(family.HusbandId));
        families.addAll(GetFamiliesForSpouse(family.WifeId));
        return GetChildrenAtLevel(families, level);
    }

    private List<Individual> GetChildrenAtLevel(HashSet<Family> families, int level) {
        HashSet<Individual> children = new HashSet<Individual>();
        for (Family family : families) {
            children.addAll(GetChildrenOfFamilyId(family.Id));
        }
        if (level == 0 || families.size() == 0) {
            return new ArrayList<>(children);
        } else {
            HashSet<Family> familiesOfChildren = new HashSet<Family>();
            for (Individual child : children) {
                familiesOfChildren.addAll(GetFamiliesForSpouse(child.Id));
            }
            return GetChildrenAtLevel(familiesOfChildren, level - 1);
        }
    }

    private List<Family> GetFamiliesForSpouse(String IndividualId) {
        if (familiesMapOnIndividualId.containsKey(IndividualId)) {
            return new ArrayList<>(familiesMapOnIndividualId.get(IndividualId));
        }
        return new ArrayList<>();
    }
}
