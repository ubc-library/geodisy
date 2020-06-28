package FixScripts;

import Dataverse.DataverseJavaObject;
import GeoServer.GeoServerAPI;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import static _Strings.GeoBlacklightStrings.*;
import static _Strings.GeodisyStrings.BACKEND_ADDRESS;
import static _Strings.GeodisyStrings.DATA_DIR_LOC;

public class GeoFiles {
    LinkedList<GBLFileToFix> gBLFs;
    String pID;
    String folder;
    public GeoFiles(LinkedList<GBLFileToFix> gBLFs) {
        this.gBLFs = gBLFs;
        pID = gBLFs.getFirst().getPID();
        folder = pID.replace(".","/");
    }
    public void dealWithGBLFs(){
        System.out.println("Starting to find data files for" + pID);
        File datafolder = new File(DATA_DIR_LOC + folder);
        if (!datafolder.exists()) {
            System.out.println("Bad file name path: " + DATA_DIR_LOC+folder);
            return;
        }
        int raster = 1;
        int vector = 1;
        LinkedList<Record> records = new LinkedList<>();
        HashSet<String> processed = new HashSet<>();
        for(GBLFileToFix gBLF:gBLFs){
            for(File f: datafolder.listFiles()){
                if(processed.contains(f.getName()))
                    continue;
                String fileName = f.getName();
                System.out.println("DATA file name: " + fileName + ", and GBLFile Name: " + gBLF.getDvjsonFileInfo().getFileName());
                if(!fileName.contains("."))
                    continue;
                String fileBaseName = fileName.substring(0,fileName.lastIndexOf("."));
                String fileExtension = fileName.substring(fileName.lastIndexOf(".")+1);
                String uploaded;
                String dVFileName = gBLF.getDvjsonFileInfo().getFileName();
                System.out.println("DV file name: " + dVFileName);
                String baseDVF;
                if(dVFileName.contains("."))
                    baseDVF  = dVFileName.substring(0,dVFileName.lastIndexOf("."));
                else
                    baseDVF = dVFileName;
                int basefileEnd = Math.min(dVFileName.length(),3);
                boolean isFile = fileBaseName.startsWith(baseDVF.substring(0,basefileEnd));
                System.out.println("Dataset file name contains first few letters of DV file name? " + isFile);
                if(gBLF.isRaster &&  isFile && (fileExtension.equals("tif"))){
                    uploaded = uploadRasterFile(f,gBLF,raster);
                    if(!uploaded.isEmpty()){
                        Record record = new Record();
                        record.g = gBLF;
                        record.geoserverLabel = "geodisy:"+ uploaded;
                        if(records.size()>0) {
                            Record first = records.get(0);
                            record.geoserverIDs.addAll(first.geoserverIDs);
                            record.geoserverIDs.add(first.geoserverLabel);
                            updateRecords(records,uploaded);
                        }

                        records.add(record);
                    }
                    raster++;
                    processed.add(f.getName());
                    break;
                }else if(!gBLF.isRaster&& isFile &&(fileExtension.equals("shp"))){
                    uploaded = uploadVectorFile(f,gBLF,vector);
                    if(!uploaded.isEmpty()){
                        Record record = new Record();
                        record.g = gBLF;
                        record.geoserverLabel = uploaded;
                        System.out.println("Record's geoserver label: " + record.geoserverLabel);
                        if(records.size()>0) {
                            Record first = records.get(0);
                            record.geoserverIDs.addAll(first.geoserverIDs);
                            record.geoserverIDs.add(first.geoserverLabel);
                            updateRecords(records,uploaded);
                        }
                        records.add(record);
                    }
                    vector++;
                    processed.add(f.getName());
                    break;
                }

            }
        }
        rewriteGBLJSONS(records);
    }

    private void rewriteGBLJSONS(LinkedList<Record> records) {
        for(Record r: records){
            String origLabel = r.g.geoserverLabel;
            String gBLJSON = r.g.geoblacklightJSON;
            gBLJSON = gBLJSON.replace(origLabel,r.geoserverLabel);
            if(records.size()>1){
                int start = gBLJSON.indexOf("dc_source_sm\":[");
                if(start!=-1){
                    start = start+15;
                    int end = gBLJSON.indexOf("]",start);
                    String recordsToAdd = "";
                    for(String s: r.geoserverIDs){
                        System.out.println("Writing related record IDs: " + s);
                        recordsToAdd += "," + quoted(s);
                    }
                    recordsToAdd = recordsToAdd.substring(1);
                    gBLJSON = gBLJSON.substring(0,start) + recordsToAdd + gBLJSON.substring(end);
                } else {
                    start = gBLJSON.indexOf("\"dc_rights_s\":\"Public\",")+23;
                    String recordsToAdd = "\"dc_source_sm\":[";
                    boolean first = true;
                    for(String s: r.geoserverIDs){
                        if(first)
                            recordsToAdd += quoted(s);
                        else
                            recordsToAdd += "," + quoted(s);
                    }
                    recordsToAdd += "],";
                    gBLJSON = gBLJSON.substring(0,start) + recordsToAdd + gBLJSON.substring(start);
                }
            }

                try {
                    JSONObject jo = new JSONObject(gBLJSON);
                    //ADD WMS functionality
                    JSONObject refs = jo.getJSONObject(EXTERNAL_SERVICES);
                    refs.put(WMS, GEOSERVER_WMS_LOCATION);
                    if (!r.g.isRaster)
                        refs.put(WFS, GEOSERVER_WFS_LOCATION);
                    jo.put(EXTERNAL_SERVICES, refs);
                    try (PrintWriter out = new PrintWriter(r.g.getGblJSONFilePath())) {
                        out.println(jo.toString());
                    } catch (FileNotFoundException e) {
                        System.out.println("Something went wrong updating the GBLJSON at " + r.g.getGblJSONFilePath() + " with " + gBLJSON);
                    }
                } catch (JSONException err) {
                    System.out.println("Error parsing json: " + gBLJSON);
                }
            }
        }


    private String quoted(String s){
        return "\"" + s + "\"";
    }
    private String uploadVectorFile(File f, GBLFileToFix gBLF, int vector) {
        String geoserverLabel = "g_"+gBLF.pID.replace(".","_").replace("/","_")+ "v" + vector;
        System.out.println("Geoserver Label created: " + geoserverLabel);
        geoserverLabel = geoserverLabel.toLowerCase();
        DataverseJavaObject djo = new DataverseJavaObject("server");
        djo.setPID(gBLF.getPID());
        GeoServerAPI geoServerAPI = new GeoServerAPI(djo);
        boolean success = geoServerAPI.addVector(f.getName(),geoserverLabel);
        if(success)
            return geoserverLabel;
        else
            return "";
    }

    private String uploadRasterFile(File f, GBLFileToFix gBLF, int raster) {
        String geoserverLabel = "g_"+gBLF.pID.replace(".","_").replace("/","_")+ "r" + raster;
        geoserverLabel = geoserverLabel.toLowerCase();
        DataverseJavaObject djo = new DataverseJavaObject("server");
        djo.setPID(gBLF.getPID());
        GeoServerAPI geoServerAPI = new GeoServerAPI(djo);
        System.out.println("Uploading Raster: Name = "+f.getName() + ", geoserverLabel = " + geoserverLabel);
        boolean success = geoServerAPI.addRaster(f.getName(),geoserverLabel);
        if(success)
            return geoserverLabel;
        else
            return "";
    }

    private LinkedList<Record> updateRecords(LinkedList<Record> records, String uploaded) {
        for(Record r: records){
            r.geoserverIDs.add(uploaded);
        }
        return records;
    }


    class Record{
        String geoserverLabel;
        GBLFileToFix g;
        LinkedList<String> geoserverIDs;
        Record(){
            geoserverIDs = new LinkedList<>();
        }
    }
}
