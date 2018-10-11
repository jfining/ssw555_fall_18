package com.rasodu.gedcom.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Family {
    public String Id = null;
    public Date Married = null;
    public String HusbandName = null;
    public Date Divorced = null;
    public String HusbandId = null;
    public String WifeId = null;
    public List<String> ChildrenIds = new ArrayList<String>();
}
