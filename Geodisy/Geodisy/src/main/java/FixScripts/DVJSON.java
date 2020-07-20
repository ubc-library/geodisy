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
        System.out.println("Accessing DV");
        String dvJSON = getDVJSON(pid);
        JSONObject base = new JSONObject(dvJSON);
        base = base.getJSONObject("datasetVersion");
        JSONArray files = base.getJSONArray("files");
        DVJSONFileInfo dvji = new DVJSONFileInfo();
        for(Object o: files){
            JSONObject j = (JSONObject) o;
            j = j.getJSONObject("dataFile");
            String id = String.valueOf(j.getInt("id"));
            if (!id.equals(gBLID))
                continue;


            String fileName = j.getString("filename");
            int period = fileName.lastIndexOf(".");
            if(period==-1)
                return dvji;
            dvji.setDbID(String.valueOf(j.getInt("id")));
            dvji.setFileName(fileName.substring(0,period));
            dvji.setFileExtension(fileName.substring(period+1));
            return dvji;
        }
        return dvji;
    }
}
