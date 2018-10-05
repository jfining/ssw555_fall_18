package com.rasodu.gedcom.Infrastructure;

import com.rasodu.gedcom.core.Family;
import com.rasodu.gedcom.core.IGedcomDataset;
import com.rasodu.gedcom.core.Individual;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class GedComDataSetFile implements IGedcomDataset {
    //static variables
    private static GedComDataSetFile instance;

    public static GedComDataSetFile GetInstance() {
        return instance;
    }

    public static void LoadFile(String filePath) throws IOException {
        instance = new GedComDataSetFile();
        instance.LoadFileLocal(filePath);
    }

    //class members
    GEDCOMDataNode root = null;

    private GedComDataSetFile() {
    }

    public void LoadFileLocal(String filePath) throws IOException {
        //build tree from gedcom file
        //GEDCOMDiskFileReader gedcomFileReader = new GEDCOMDiskFileReader("/home/abhijitramin/myprojects/SIT/SSW555WS/Project1/proj02test.ged");
        GEDCOMDiskFileReader gedcomFileReader = new GEDCOMDiskFileReader(filePath);
        root = new GEDCOMDataTreeBuilder(gedcomFileReader).Build();
        //parsing operation on the tree
        GEDCOMDataTreeValidityParser treeValidator = new GEDCOMDataTreeValidityParser(root);
        treeValidator.DetermineTreeNodeValidity();
        //read data to list
        readChildNodesIntoList(root);
    }

    //parse to list
    private static TreeNodeToItem parser = new TreeNodeToItem();

    private void readChildNodesIntoList(GEDCOMDataNode node) {
        for (GEDCOMDataNode childNode : node.GetChildren()) {
            switch (childNode.GetTag()) {
                case ("INDI"):
                    Individual indi = parser.ToIndividual(childNode);
                    if (indi != null) {
                        individuals.add(indi);
                    }
                    break;
                case ("FAM"):
                    Family fam = parser.ToFamily(childNode);
                    if (fam != null) {
                        families.add(fam);
                    }
                    break;
                default:
                    readChildNodesIntoList(childNode);
                    break;
            }
        }
    }

    //print file for user to check
    public void PrintFileTree() {
        //print gedcom string
        GEDCOMDataTreePrinter treeOp = new GEDCOMDataTreePrinter(root);
        String printStr = treeOp.TreeToString();
        System.out.println(printStr);
    }

    //return list to user
    List<Individual> individuals = new ArrayList<Individual>();

    @Override
    public List<Individual> GetAllIndividuals() {
        return individuals;
    }

    List<Family> families = new ArrayList<Family>();

    @Override
    public List<Family> GetAllFamilies() {
        return families;
    }
}

class TreeNodeToItem {
    private static DateFormat format = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);

    public Individual ToIndividual(GEDCOMDataNode node) {
        if (!node.IsValid()) {
            return null;
        }
        Individual individual = new Individual();
        individual.Id = node.GetArguments();
        for (GEDCOMDataNode childNode : node.GetChildren()) {
            if (childNode.IsValid()) {
                switch (childNode.GetTag()) {
                    case "NAME":
                        individual.Name = childNode.GetArguments();
                        break;
                    case "SEX":
                        individual.Gender = childNode.GetArguments().charAt(0);
                        break;
                    case "BIRT":
                        individual.Birthday = GetDateFromChildNode(childNode);
                        break;
                    case "DEAT":
                        individual.Death = GetDateFromChildNode(childNode);
                        break;
                    case "FAMC":
                        individual.ChildOfFamily = childNode.GetArguments();
                        break;
                    case "FAMS":
                        individual.SpouseInFamily.add(childNode.GetArguments());
                        break;
                }
            }
        }
        return individual;
    }

    public Family ToFamily(GEDCOMDataNode node) {
        if (!node.IsValid()) {
            return null;
        }
        Family family = new Family();
        family.Id = node.GetArguments();
        for (GEDCOMDataNode childNode : node.GetChildren()) {
            if (childNode.IsValid()) {
                switch (childNode.GetTag()) {
                    case "MARR":
                        family.Married = GetDateFromChildNode(childNode);
                        break;
                    case "DIV":
                        family.Divorced = GetDateFromChildNode(childNode);
                        break;
                    case "HUSB":
                        family.HusbandId = childNode.GetArguments();
                        break;
                    case "WIFE":
                        family.WifeId = childNode.GetArguments();
                        break;
                    case "CHIL":
                        family.ChildrenIds.add(childNode.GetArguments());
                        break;
                }
            }
        }
        return family;

    }

    private Date GetDateFromChildNode(GEDCOMDataNode birtNode) {
        Date birthdate = null;
        for (GEDCOMDataNode childNode : birtNode.GetChildren()) {
            if ("DATE".equals(childNode.GetTag()) && childNode.IsValid()) {
                try {
                    birthdate = format.parse(childNode.GetArguments());
                    break;
                } catch (ParseException e) {

                }
            }
        }
        return birthdate;
    }
}

class GEDCOMDiskFileReader {
    private FileReader gedcomFileReader;
    private BufferedReader gedcomFileBufferedReder;

    public GEDCOMDiskFileReader(String filePath) throws IOException {
        File gedcomFile = new File(filePath);
        gedcomFileReader = new FileReader(gedcomFile);
        gedcomFileBufferedReder = new BufferedReader(gedcomFileReader);
    }

    boolean readingComple = false;
    private String line = null;

    public String ReadCurrentLine() throws IOException {
        if (!readingComple && line == null) {
            line = gedcomFileBufferedReder.readLine();
            if (line == null) {
                readingComple = true;
            }
        }
        return line;
    }

    public void Next() {
        line = null;
    }

    public void Close() throws IOException {
        gedcomFileReader.close();
    }
}

class GEDCOMDataTreeBuilder {
    GEDCOMDiskFileReader gedcomDiskFileReader;

    public GEDCOMDataTreeBuilder(GEDCOMDiskFileReader gedcomFileReader) {
        gedcomDiskFileReader = gedcomFileReader;
    }

    public GEDCOMDataNode Build() throws IOException {
        GEDCOMDataNode root = new GEDCOMDataNode("root");
        DigestRec(0, root);
        return root;
    }

    private void DigestRec(int currentLevel, GEDCOMDataNode currentLevelRootNode) throws IOException {
        int lineLevel;
        for (String line = gedcomDiskFileReader.ReadCurrentLine(); line != null; line = gedcomDiskFileReader.ReadCurrentLine()) {
            lineLevel = GetLineLeve(line);
            if (lineLevel == currentLevel) {
                currentLevelRootNode.AddChild(line);
                gedcomDiskFileReader.Next();
            } else if (lineLevel > currentLevel) {
                DigestRec(lineLevel, currentLevelRootNode.GetLastChild());
            } else {
                break;
            }
        }
    }

    private int GetLineLeve(String line) {
        return Character.getNumericValue(line.charAt(0));
    }
}

class GEDCOMDataNode {
    public static final String ROOT_LINE = "root";
    private String data;
    private List<GEDCOMDataNode> children = new ArrayList<GEDCOMDataNode>();
    //calculate in constructor
    private int level;
    private String tag;
    private String arguments = "";
    //populate data in second parse
    private String nodeTagName = "";
    private Boolean valid = false;

    GEDCOMDataNode(String line) {
        data = line;
        CalculateFilds(line);
    }

    private void CalculateFilds(String line) {
        if (line.equals(ROOT_LINE)) {
            return;
        }
        level = Character.getNumericValue(line.charAt(0));
        line = line.substring(2);
        String[] splittedLine = line.split(" ", 2);
        if (splittedLine.length > 0) {
            tag = splittedLine[0].toUpperCase();
        }
        if (splittedLine.length > 1) {
            arguments = splittedLine[1];
        }
        //swap value of tag and argument for INDI and FAM
        if (arguments.equals("INDI") || tag.equals("INDI") || arguments.equals("FAM") || tag.equals("FAM")) {
            String backup = tag;
            tag = arguments;
            arguments = backup;
        }
    }

    public GEDCOMDataNode AddChild(String line) {
        GEDCOMDataNode node = new GEDCOMDataNode(line);
        children.add(node);
        return node;
    }

    public GEDCOMDataNode GetLastChild() {
        return children.get(children.size() - 1);
    }

    public List<GEDCOMDataNode> GetChildren() {
        return children;
    }

    public String GetData() {
        return data;
    }

    public String GetNodeTagName() {
        return nodeTagName;
    }

    public void SetNodeTagName(String parentNodeTagName) {
        nodeTagName = parentNodeTagName + level + tag;
    }

    public void SetNodeValidity(boolean nodeValidity) {
        valid = nodeValidity;
    }

    public int GetLevel() {
        return level;
    }

    public String GetTag() {
        return tag;
    }

    public boolean IsValid() {
        return valid;
    }

    public String GetValidityString() {
        if (valid) {
            return "Y";
        }
        return "N";
    }

    public String GetArguments() {
        return arguments;
    }
}

class GEDCOMDataTreePrinter {
    private GEDCOMDataNode root;

    public GEDCOMDataTreePrinter(GEDCOMDataNode rootNode) {
        root = rootNode;
    }

    String printStr;

    public String TreeToString() {
        printStr = "";
        PrintTreeInOrder(root, root.GetChildren());
        String returnStr = printStr;
        printStr = "";
        return returnStr;
    }

    public void PrintTreeInOrder(GEDCOMDataNode parent, List<GEDCOMDataNode> children) {
        for (GEDCOMDataNode child : children) {
            printStr += "--> " + child.GetData() + "\n";
            printStr += "<-- " + child.GetLevel() + "|" + child.GetTag() + "|" + child.GetValidityString() + "|" + child.GetArguments() + "\n";
            PrintTreeInOrder(child, child.GetChildren());
        }
    }
}

class GEDCOMDataTreeValidityParser {
    private GEDCOMDataNode root;
    private ValidGEDCOMNode nodeValidator;

    public GEDCOMDataTreeValidityParser(GEDCOMDataNode rootNode) {
        root = rootNode;
        nodeValidator = new ValidGEDCOMNode();
    }

    public void DetermineTreeNodeValidity() {
        SetValidity(root, root.GetChildren());
    }

    private void SetValidity(GEDCOMDataNode parent, List<GEDCOMDataNode> children) {
        String nodeTagName;
        Boolean nodeValidity;
        for (GEDCOMDataNode child : children) {
            child.SetNodeTagName(parent.GetNodeTagName());
            nodeTagName = child.GetNodeTagName();
            nodeValidity = nodeValidator.IsValid(nodeTagName);
            child.SetNodeValidity(nodeValidity);
            SetValidity(child, child.GetChildren());
        }
    }
}

class ValidGEDCOMNode {
    private static Map<String, Boolean> validTagList;

    static {
        validTagList = new HashMap<String, Boolean>();
        validTagList.put("0NOTE", true);
        validTagList.put("0TRLR", true);
        validTagList.put("0HEAD", true);
        validTagList.put("0HEAD1SOUR", true);
        validTagList.put("0HEAD1SOUR2WWW", true);
        validTagList.put("0INDI", true);
        validTagList.put("0INDI1NAME", true);
        validTagList.put("0INDI1SEX", true);
        validTagList.put("0INDI1BIRT", true);
        validTagList.put("0INDI1BIRT2DATE", true);
        validTagList.put("0INDI1DEAT", true);
        validTagList.put("0INDI1DEAT2DATE", true);
        validTagList.put("0INDI1FAMC", true);
        validTagList.put("0INDI1FAMS", true);
        validTagList.put("0FAM", true);
        validTagList.put("0FAM1MARR", true);
        validTagList.put("0FAM1MARR2DATE", true);
        validTagList.put("0FAM1HUSB", true);
        validTagList.put("0FAM1WIFE", true);
        validTagList.put("0FAM1CHIL", true);
        validTagList.put("0FAM1DIV", true);
        validTagList.put("0FAM1DIV2DATE", true);
    }

    public Boolean IsValid(String nodeTagName) {
        if (validTagList.containsKey(nodeTagName)) {
            return true;
        }
        return false;
    }
}
