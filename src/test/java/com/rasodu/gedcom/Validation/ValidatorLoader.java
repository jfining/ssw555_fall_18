package com.rasodu.gedcom.Validation;

import com.rasodu.gedcom.Infrastructure.GedComDataSetFile;
import com.rasodu.gedcom.Infrastructure.GedcomRepository;
import com.rasodu.gedcom.Utils.GedLogger;
import com.rasodu.gedcom.core.Family;
import com.rasodu.gedcom.core.IGedcomDataset;
import com.rasodu.gedcom.core.IGedcomRepository;
import com.rasodu.gedcom.core.Individual;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ValidatorLoader {

    protected IGedcomRepository repository;
    protected GeneralValidator gv;
    protected FamilyValidator fv;
    protected IndividualValidator iv;

    protected void Load(String path, GedLogger log) {
        try {
            //create path
            File resourcesDirectory = new File("Deliverables/UserStories");
            String actualPath = resourcesDirectory.getAbsolutePath() + "/" + path + ".ged";
            //load data from file
            GedComDataSetFile.LoadFile(actualPath);
            IGedcomDataset dataSet = GedComDataSetFile.GetInstance();
            List<Individual> individuals = dataSet.GetAllIndividuals();
            List<Family> families = dataSet.GetAllFamilies();
            repository = new GedcomRepository(individuals, families);
            //create validators
            gv = new GeneralValidator(families, individuals, log);
            fv = new FamilyValidator(families, individuals, log);
            iv = new IndividualValidator(families, individuals, log);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
