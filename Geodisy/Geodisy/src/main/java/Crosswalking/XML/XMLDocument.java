package Crosswalking.XML;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


/**
 * Wrapper for the Document class so I can control it more easily
 */
public class XMLDocument {
    Document doc;
    XMLStack stack;

    public XMLDocument() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = null;
            docBuilder = docFactory.newDocumentBuilder();

            doc = docBuilder.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        stack = new XMLStack(this);
    }

    public Document getDoc(){
        return doc;
    }

    public Element createGMDElement(String s){
        return doc.createElement(addGMD(s));
    }

    public Element createGCOElement(String s){
        return doc.createElement(addGCO(s));
    }
    public Element create_Element(String s){
        return doc.createElement(s);
    }
    // GMD is either a description or parent element w/o a value
    private String addGMD(String s){
        return "gmd:" + s;
    }

    // GCO indicates generally a value rather than a description
    private String addGCO(String s) {
        return "gco:" + s;
    }

    //Adds the element at the lowest level of the hierarchy that holds the value
    public Element addGCOVal(String altTitleVal, String label) {
        Element val = doc.createElement(addGCO(label));
        val.setNodeValue(altTitleVal);
        return val;
    }

    public Element addGMDVal(String altTitleVal, String label) {
        Element val = doc.createElement(addGMD(label));
        val.setNodeValue(altTitleVal);
        return val;
    }

    public Element addRoleCode(String altTitleVal) {
        Element val = doc.createElement(addGMD("CI_RoleCode"));
        val.setNodeValue(altTitleVal);
        return val;
    }

    public void stackElement(Element e){
        XMLElement x = new XMLElement(this,e);
        stack.push(x);
    }
    //return the bottom element of the stack, which each higher up stack element being assigned as a child of the element below it
    public Element zip(){
        return stack.zip();
    }
}
