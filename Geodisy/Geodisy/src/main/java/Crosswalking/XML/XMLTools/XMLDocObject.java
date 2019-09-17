package Crosswalking.XML.XMLTools;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


/**
 * Wrapper for the Document class so I can control it more easily
 * This Class generates the XMLDocument Object (not an XML file)
 */
public class XMLDocObject {
    Document doc;
    String doi;


    public XMLDocObject() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

    }

    public Document getDoc(){
        return doc;
    }

    public Element createMDBElement(String s){
        return doc.createElement(addMDB(s));
    }

    //May never get used
    public Element createGCOElement(String s){
        return doc.createElement(addGCO(s));
    }

    public Element create_Element(String s){
        return doc.createElement(s);
    }

    // MDB is either a description or parent element w/o a value
    private String addMDB(String s){
        return "mdb:" + s;

    }

    // GCO indicates generally a value rather than a description
    private String addGCO(String s) {
        return "gco:" + s;
    }

    //Adds the element at the lowest level of the hierarchy that holds the value
    public Element addGCOVal(String value, String label) {
        Element val = doc.createElement(addGCO(label));
        val.setTextContent(value);
        return val;
    }

    public Element addMDBVal(String value, String label) {
        Element val = doc.createElement(addMDB(label));
        val.setTextContent(value);
        return val;
    }

    public Element addRoleCode(String altTitleVal) {
        Element val = doc.createElement(addMDB("CI_RoleCode"));
        val.setTextContent(altTitleVal);
        return val;
    }

    public void addRoot(Element root){
        doc.appendChild(root);
    }

    public String getDoi(){return doi;}

    public void setDoi(String doi){this.doi = doi;}

}
