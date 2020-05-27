package BaseFiles;

import Crosswalking.JSONParsing.DataverseParser;
import Dataverse.DataverseJavaObject;
import Dataverse.DataverseRecordInfo;
import Dataverse.ExistingHarvests;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import _Strings.GeodisyStrings;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;

import static _Strings.GeodisyStrings.*;
import static org.junit.jupiter.api.Assertions.*;

public class ExistingHarvestsTest {
    DataverseJavaObject djo;
    JSONObject jo;
    @Before
    public void initialize(){
        InputStream is = null;

        try {
            is = new FileInputStream(GeodisyStrings.replaceSlashes(ALL_CITATION_METADATA));

            BufferedReader buf = new BufferedReader(new InputStreamReader(is));

            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();

            while(line != null){
                sb.append(line).append("\n");
                line = buf.readLine();
            }
            String jsonData = sb.toString();
            jo = new JSONObject(jsonData);
            DataverseParser dataverseParser = new DataverseParser();
            djo = dataverseParser.parse(jo, "another fake server name");
            System.out.println(djo.getBoundingBox().getLatNorth());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void setExistingSearchesTest(){
        try {
            Files.deleteIfExists(new File(TEST_EXISTING_BBOXES).toPath());
            Files.deleteIfExists(new File(TEST_EXISTING_RECORDS).toPath());
        } catch (IOException e) {
            System.out.println("One of the test files didn't yet exist, so couldn't be destroyed");
        }
        ExistingHarvests es = ExistingHarvests.getExistingHarvests();
        HashMap<String, BoundingBox> bboxes = new HashMap<>();
        BoundingBox bbox = new BoundingBox();
        bbox.setLatSouth(81);
        bbox.setLatNorth(31);
        bbox.setLongEast(120);
        bbox.setLongWest(-20);
        bboxes.put("fake doi",bbox);
        DataverseRecordInfo dri = new DataverseRecordInfo(djo,this.getClass().toString());
        HashMap<String, DataverseRecordInfo> records = new HashMap<>();
        records.put("fake doi",dri);
        assertEquals (bboxes.size(),1);
        assertEquals (records.size(),1);
        FileWriter writer = new FileWriter();
        try {
            writer.writeObjectToFile(bboxes,TEST_EXISTING_BBOXES);
            writer.writeObjectToFile(records,TEST_EXISTING_RECORDS);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HashMap<String, BoundingBox> bboxes2 = new HashMap<>();
        HashMap<String, DataverseRecordInfo> records2 = new HashMap<>();
        try {
            bboxes2 = (HashMap<String, BoundingBox>) writer.readSavedObject(TEST_EXISTING_BBOXES);

            records2 = (HashMap<String, DataverseRecordInfo>) writer.readSavedObject(TEST_EXISTING_RECORDS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue (bboxes2.keySet().equals(bboxes.keySet()));
        assertTrue(records2.keySet().equals(records.keySet()));
        BoundingBox box2 = new BoundingBox();
        box2.setLongWest(-15);
        box2.setLongEast(35);
        box2.setLatNorth(89);
        box2.setLatSouth(79);
        bboxes.put("fake doi2",box2);
        records.put("fake doi2",new DataverseRecordInfo(djo,this.getClass().toString()+"2"));
        try {
            writer.writeObjectToFile(bboxes,TEST_EXISTING_BBOXES);
            writer.writeObjectToFile(records,TEST_EXISTING_RECORDS);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertFalse (bboxes2.keySet().equals(bboxes.keySet()));
        assertFalse(records2.keySet().equals(records.keySet()));
        try {
            bboxes2 = (HashMap<String, BoundingBox>) writer.readSavedObject(TEST_EXISTING_BBOXES);

            records2 = (HashMap<String, DataverseRecordInfo>) writer.readSavedObject(TEST_EXISTING_RECORDS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue (bboxes2.keySet().equals(bboxes.keySet()));
        assertTrue(records2.keySet().equals(records.keySet()));
    }
    @Test
    public void fixPerms(){
        String answer = FileWriter.fixPath("htheheredoi:1232,23123");
        assertTrue (answer.equals("hthehere1232,23123"));
    }

    @Test
    public void setCoverage(){
        InputStream is = null;

        try {
            is = new FileInputStream(GeodisyStrings.replaceSlashes(TEST_GEO_COVERAGE));

            BufferedReader buf = new BufferedReader(new InputStreamReader(is));

            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();

            while(line != null){
                sb.append(line).append("\n");
                line = buf.readLine();
            }
            String jsonData = sb.toString();
            jo = new JSONObject(jsonData);
            DataverseParser dataverseParser = new DataverseParser();
            djo = dataverseParser.parse(jo, "another fake server name");
            System.out.println(djo.getBoundingBox().getLatNorth());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void parseTest(){
        InputStream is = null;

        try {
            is = new FileInputStream(GeodisyStrings.replaceSlashes(ALL_CITATION_METADATA));

            BufferedReader buf = new BufferedReader(new InputStreamReader(is));

            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();

            while(line != null){
                sb.append(line).append("\n");
                line = buf.readLine();
            }
            String jsonData = sb.toString();
            jo = new JSONObject(jsonData);
            DataverseParser dataverseParser = new DataverseParser();
            djo = dataverseParser.parse(jo, "another fake server name");
            System.out.println(djo.getBoundingBox().getLatNorth());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
