package com.rasodu.gedcom.Processor;

import com.rasodu.gedcom.core.Individual;

import java.util.Comparator;

public class SortbyIndID implements Comparator<Individual> {
    public int compare(Individual a, Individual b) {
        return a.Id.compareTo(b.Id);
    }
}
