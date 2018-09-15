package com.rasodu.gedcom.Infrastructure;

import de.vandermeer.asciitable.AsciiTable;

import java.util.List;

/**
 * public static void PrintTable(){
 * //setup data
 * List<String> row1 = new ArrayList<String>();
 * row1.add("First Name");
 * row1.add("Last Name");
 * List<String> row2 = new ArrayList<String>();
 * row2.add("Abhijit");
 * row2.add("Amin");
 * List<List<String>> tableData = new ArrayList<List<String>>();
 * tableData.add(row1);
 * tableData.add(row2);
 * //print table
 * TablePrinter tp = new TablePrinter();
 * tp.PrintTable(tableData);
 * }
 */
public class TablePrinter {
    public void PrintTable(List<List<String>> tableData) {
        AsciiTable at = new AsciiTable();
        for (List<String> row : tableData) {
            at.addRule();
            at.addRow(row);
        }
        at.addRule();
        System.out.println(at.render());
    }
}
