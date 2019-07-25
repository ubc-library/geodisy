package tests.XMLTestCases;

import Crosswalking.JSONParsing.DataverseParser;
import Crosswalking.XML.XMLGenerator;
import Dataverse.DataverseJavaObject;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

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
        System.out.println(djo.getBoundingBox().getLatNorth());
        XMLGenerator xmlGenerator = new XMLGenerator(djo);
        Document xmlFile = xmlGenerator.generateXMLFile();
        xmlFile.toString();
    }
}
