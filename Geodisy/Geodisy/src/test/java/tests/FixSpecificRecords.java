package tests;

import BaseFiles.HTTPCallerGeoNames;
import BaseFiles.GeodisyTask;
import Crosswalking.JSONParsing.DataverseParser;
import Dataverse.DataverseAPI;
import Dataverse.DataverseJavaObject;
import Dataverse.SourceJavaObject;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static Strings.GeodisyStrings.SANDBOX_DV_URL;

public class FixSpecificRecords {
    String doi;
    String dvURL;

    private JSONObject getJSON(String doi){
        HTTPCallerGeoNames getMetadata;
        //TODO set base URL programmatically
        String baseURL = dvURL + "api/datasets/export?exporter=dataverse_json&persistentId=";
        getMetadata = new HTTPCallerGeoNames();
        String dataverseJSON = getMetadata.callHTTP(baseURL+doi);
        JSONObject jo = new JSONObject(dataverseJSON);
        return jo;
    }

    private DataverseJavaObject parseDJO(JSONObject jo){
        DataverseParser parser = new DataverseParser();
        DataverseJavaObject djo = parser.parse(jo, dvURL,true);
        return djo;
    }

    @Test
    public void run(){
        dvURL = SANDBOX_DV_URL;
        doi = "doi:10.5072/FK2/SAUHWD";
        JSONObject jo = getJSON(doi);
        DataverseJavaObject djo = parseDJO(jo);
        djo.downloadFiles();
        DataverseAPI api = new DataverseAPI(dvURL);
        djo = api.generateBoundingBox(djo);
        String localDoi = djo.getDOI();
        GeodisyTask geodisyTask = new GeodisyTask();
        List<SourceJavaObject> sJOs = new LinkedList<>();
        sJOs.add(djo);
        DataverseAPI dvapi = new DataverseAPI(djo.getServer());
        dvapi.crosswalkRecord(djo);
        String stop = "Place to pause program";
    }

    public void run(String doi){
        dvURL = SANDBOX_DV_URL;
        this.doi = doi;
        JSONObject jo = getJSON(doi);
        DataverseJavaObject djo = parseDJO(jo);
        djo.downloadFiles();
    }
}
