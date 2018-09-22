package com.rasodu.gedcom.Processor;

import com.rasodu.gedcom.Infrastructure.TablePrinter;
import com.rasodu.gedcom.core.Family;
import com.rasodu.gedcom.core.Individual;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;

public class ListToTableData {
    private List<Individual> _individuals;
    private List<Family> _families;
    private TablePrinter tp;

    public ListToTableData(List<Individual> individuals, List<Family> families) {
        _individuals = individuals;
        _families = families;
        Collections.sort(_individuals, new SortbyIndID());
        Collections.sort(_families, new SortbyFmyID());
        tp = new TablePrinter();
    }

    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public List<List<String>> GetIndivisualTableData() {
        List<List<String>> tableData = new ArrayList<List<String>>();
        String[] header = new String[]{"ID", "Name", "Gender", "Birthday", "Age", "Alive", "Death", "Child", "Spouse"};
        tableData.add(Arrays.asList(header));
        LocalDate date = (new Date()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        for (Individual indivisual : _individuals) {
            List<String> row = new ArrayList<String>();
            row.add(indivisual.Id);
            row.add(indivisual.Name);
            row.add(String.valueOf(indivisual.Gender));
            row.add(df.format(indivisual.Birthday));
            LocalDate birthday = indivisual.Birthday.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (indivisual.Death == null) {
                row.add(String.valueOf(Period.between(birthday, date).getYears()));
            } else {
                LocalDate death = indivisual.Death.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                row.add(String.valueOf(Period.between(birthday, death).getYears()));
            }
            if (indivisual.Death != null) {
                row.add(String.valueOf(true));
                row.add(df.format(indivisual.Death));
            } else {
                row.add(String.valueOf(false));
                row.add("N/A");
            }
            if (indivisual.ChildOfFamily.isEmpty()) {
                row.add("N/A");
            } else {
                String childOf = "";
                for (String family : indivisual.ChildOfFamily) {
                    childOf += family + ";";
                }
                row.add(childOf);
            }
            if (indivisual.SpouseInFamily.isEmpty()) {
                row.add("N/A");
            } else {
                String families = "";
                for (String family : indivisual.SpouseInFamily) {
                    families += family + ";";
                }
                row.add(families);
            }
            tableData.add(row);
        }
        return tableData;
    }

    public List<List<String>> GetFamilyTableData() {
        List<List<String>> tableData = new ArrayList<List<String>>();
        String[] header = new String[]{"ID", "Married", "Divorced", "Husband ID", "Husband Name", "Wife ID", "Wife Name", "Children"};
        tableData.add(Arrays.asList(header));
        for (Family family : _families) {
            List<String> row = new ArrayList<String>();
            row.add(family.Id);
            row.add(df.format(family.Married));
            if (family.Divorced != null) {
                row.add(df.format(family.Divorced));
            } else {
                row.add("N/A");
            }
            row.add(family.HusbandId);
            row.add(IndivisualIdToName(family.HusbandId));
            row.add(family.WifeId);
            row.add(IndivisualIdToName(family.WifeId));
            if (family.ChildrenIds.isEmpty()) {
                row.add("N/A");
            } else {
                String children = "";
                for (String childId : family.ChildrenIds) {
                    children += childId + ";";
                }
                row.add(children);
            }
            tableData.add(row);
        }
        return tableData;
    }

    private String IndivisualIdToName(String indId) {
        for (Individual ind : _individuals) {
            if (ind.Id.equals(indId)) {
                return ind.Name;
            }
        }
        return "";
    }
}

class SortbyIndID implements Comparator<Individual> {
    public int compare(Individual a, Individual b) {
        return a.Id.compareTo(b.Id);
    }
}

class SortbyFmyID implements Comparator<Family> {
    public int compare(Family a, Family b) {
        return a.Id.compareTo(b.Id);
    }
}