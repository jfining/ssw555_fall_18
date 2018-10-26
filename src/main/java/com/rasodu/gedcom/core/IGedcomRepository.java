package com.rasodu.gedcom.core;

import java.util.List;

public interface IGedcomRepository {
    public List<Individual> GetAllIndividuals();

    public List<Family> GetAllFamilies();

    public boolean ContainsIndividual(String IndivisualId);

    public Individual GetIndividual(String IndividualId);

    public boolean ContainsFamily(String FamilyId);

    public Family GetFamily(String FamilyId);

    public Individual GetParentOfFamily(Family family, Spouse spouse);

    public List<Individual> GetChildrenOfFamily(Family family);

    public Individual GetParentOfFamilyId(String familyId, Spouse spouse);

    public boolean HasFamilyForSpouse(String spouse1Id, String spouse2Id);

    public Family GetFamilyForSpouse(String spouse1Id, String spouse2Id);

    public List<Individual> GetChildrenAtLevel(Family family, int level);
}
