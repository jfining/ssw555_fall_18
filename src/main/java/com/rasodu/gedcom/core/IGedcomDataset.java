package com.rasodu.gedcom.core;

import java.util.List;

public interface IGedcomDataset {
    public List<Individual> GetAllIndividuals();

    public List<Family> GetAllFamilies();
}
