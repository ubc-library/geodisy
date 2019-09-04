package tests.XMLTestCases;

import Crosswalking.JSONParsing.DataverseParser;
import Crosswalking.XML.XMLTools.XMLDocObject;
import Crosswalking.XML.XMLTools.ISOXMLGen;
import Dataverse.DataverseJavaObject;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class CitationXMLTest {
DataverseJavaObject djo;
    @Before public void initialize(){
        InputStream is = null;

        try {
            is = new FileInputStream("./AllCitationMetadata.json");

            BufferedReader buf = new BufferedReader(new InputStreamReader(is));

            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();

            while(line != null){
                sb.append(line).append("\n");
                line = buf.readLine();
            }
            String jsonData = sb.toString();
            JSONObject jo = new JSONObject(jsonData);
            DataverseParser dataverseParser = new DataverseParser();
            djo = dataverseParser.parse(jo, "another fake server name");
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
        StreamResult result = new StreamResult(new File("xmlTestDJO.xml"));

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
}
