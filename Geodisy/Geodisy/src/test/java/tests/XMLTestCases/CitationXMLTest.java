package tests.XMLTestCases;

import Crosswalking.GeoBlacklightJson.DataGBJSON;
import Crosswalking.ISO_19139;
import Crosswalking.JSONParsing.DataverseParser;
import Crosswalking.XML.XMLTools.XMLDocObject;
import Crosswalking.XML.XMLTools.ISOXMLGen;
import Crosswalking.XML.XMLTools.XMLValidator;
import Dataverse.DataverseJavaObject;
import _Strings.GeodisyStrings;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

import static _Strings.GeodisyStrings.*;
import static org.junit.Assert.assertTrue;

public class CitationXMLTest {
DataverseJavaObject djo;
    @Before public void initialize(){
        GeodisyStrings.load();

        try (BufferedReader buf = new BufferedReader(new InputStreamReader(new FileInputStream(ALL_CITATION_METADATA)))) {
            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();

            while(line != null){
                sb.append(line).append("\n");
                line = buf.readLine();
            }
            String jsonData = sb.toString();
            JSONObject jo = new JSONObject(jsonData);
            DataverseParser dataverseParser = new DataverseParser();
            djo = dataverseParser.parse(jo, "another fake server name", true);
            System.out.println(djo.getBoundingBox().getLatNorth());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testDJO(){
        try {
        System.out.println(djo.getBoundingBox().getLatNorth());
        ISOXMLGen xmlGenerator = new ISOXMLGen(djo);
        XMLDocObject xmlFile = xmlGenerator.generateXMLFile();
        DOMSource source = new DOMSource(xmlFile.getDoc());
        StreamResult result = new StreamResult(new File(XML_TEST_FILE));

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;

            transformer = transformerFactory.newTransformer();

        // Beautify the format of the resulted XML
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testXMLValidation(){
        XMLValidator xmlValidator = new XMLValidator();
        assertTrue(xmlValidator.validateXML(XML_TEST_FILE));
    }

    @Test
    public void testXMLSave(){
        ISOXMLGen xmlGenerator = new ISOXMLGen(djo);
        XMLDocObject xmlFile = xmlGenerator.generateXMLFile();
        ISO_19139 iso = new ISO_19139();
        iso.saveXMLToFile(xmlFile.getDoc(), xmlFile.getDoi(), true);
    }

    @Test
    public void testJSONSave(){
        DataGBJSON dataGBJSON = new DataGBJSON(djo);
        dataGBJSON.createJson();
    }
}
