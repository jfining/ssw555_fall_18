package com.rasodu.gedcom;

import com.rasodu.gedcom.Infrastructure.GedComDataSetFile;
import com.rasodu.gedcom.Infrastructure.TablePrinter;
import com.rasodu.gedcom.Processor.ListToTableData;
import com.rasodu.gedcom.Utils.GedLogger;
import com.rasodu.gedcom.Validation.FamilyValidator;
import com.rasodu.gedcom.Validation.GeneralValidator;
import com.rasodu.gedcom.Validation.IndividualValidator;
import com.rasodu.gedcom.core.Family;
import com.rasodu.gedcom.core.IGedcomDataset;
import com.rasodu.gedcom.core.Individual;

import java.io.Console;
import java.io.IOException;
import java.util.List;

/**
 * @author Abhijit Amin
 * This is 'Project 3' for SSW 555WS Fall 2018
 * This application will parse and validate tags used in GEDCOM file
 */
public class App {
    public static void main(String[] args) {
        if (args.length >= 1) {
            ParseAndPrintDEDCOMFile(args[0]);
        }
    }

    public static void ParseAndPrintDEDCOMFile(String filePath) {
        try {
            //load all data
            GedComDataSetFile.LoadFile(filePath);
            IGedcomDataset dataSet = GedComDataSetFile.GetInstance();
            List<Individual> individuals = dataSet.GetAllIndividuals();
            List<Family> families = dataSet.GetAllFamilies();
            
            //Validate data
            GedLogger log = new GedLogger();
            GeneralValidator gv = new GeneralValidator(families, individuals);
            FamilyValidator fv = new FamilyValidator(families, individuals, log);
            IndividualValidator iv = new IndividualValidator(families, individuals);
            
            boolean generalValid = gv.validate();
            System.out.println("General Validation: "+String.valueOf(generalValid));
            boolean familyValid = fv.validate();
            System.out.println("Family Validation: "+String.valueOf(familyValid));
            boolean individualValid = iv.validate();
            System.out.println("Individual Validation: "+String.valueOf(individualValid));
            
            //get data to print
            ListToTableData lstotd = new ListToTableData(individuals, families);
            List<List<String>> individualsTable = lstotd.GetIndivisualTableData();
            List<List<String>> familiesTable = lstotd.GetFamilyTableData();
            
            //print data
            TablePrinter tp = new TablePrinter();
            tp.PrintTable(individualsTable);
            tp.PrintTable(familiesTable);
            System.out.println();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
