package FixScripts;

import BaseFiles.HTTPCallerGeoNames;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;

public class DVJSON {

    private String getDVJSON(String doi){
        HTTPCallerGeoNames getMetadata;
        String baseURL = "https://dataverse.scholarsportal.info/api/datasets/export?exporter=dataverse_json&persistentId=";
        LinkedList<JSONObject> answers =  new LinkedList<>();
                String pid = "doi:" + doi;
                getMetadata = new HTTPCallerGeoNames();
                String dataverseJSON = getMetadata.callHTTP(baseURL+pid);
                if(dataverseJSON.equals("HTTP Fail"))
                    return "";
                return dataverseJSON;
    }

    public DVJSONFileInfo getFileInfo(String pid, String gBLID){
        String dvJSON = getDVJSON(doi);
        String dvJSON = getDVJSON(pid);
        JSONObject base = new JSONObject(dvJSON);
        base = base.getJSONObject("datasetVersion");
        JSONArray files = base.getJSONArray("files");
        LinkedList<DVJSONFileInfo> infoFiles = new LinkedList<>();
        int fileNumber = 0;
        for(Object o: files){
            JSONObject j = (JSONObject) o;
            j = j.getJSONObject("dataFile");
            DVJSONFileInfo dvji = new DVJSONFileInfo();
            dvji.setDbID(j.getString("id"));
            dvji.setFileNumber(fileNumber);
            String fileName = j.getString("filename");
            int period = fileName.lastIndexOf(".");
            dvji.setFileName(fileName.substring(0,period));
            dvji.setFileExtension(fileName.substring(period+1));
            infoFiles.add(dvji);
            fileNumber++;
        }
        return infoFiles;
    }
}
