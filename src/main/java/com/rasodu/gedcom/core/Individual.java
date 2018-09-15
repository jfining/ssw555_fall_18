package com.rasodu.gedcom.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Individual {
    public String Id = null;
    public String Name = null;
    public char Gender = 'N';
    public Date Birthday = null;
    public Date Death = null;
    public List<String> ChildOfFamily = new ArrayList<String>();
    public List<String> SpouseInFamily = new ArrayList<String>();
}
