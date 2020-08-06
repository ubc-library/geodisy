package com.company;

import HTTPCallerGeoNames;
import JSONParsing.DataverseParser;
import DataverseJavaObject;
import org.json.JSONObject;

import static BaseFiles.GeodisyStrings.SANDBOX_DV_URL;

public class Downloader {
    String doi;
    String dvURL;
    public void run(){
        dvURL = SANDBOX_DV_URL;
        doi = "doi:10.5072/FK2/KZRG9F";
        JSONObject jo = getJSON(doi);
        DataverseJavaObject djo = parseDJO(jo);
        djo.downloadFiles();
    }

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
}