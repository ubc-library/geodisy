package FixScripts;

import BaseFiles.GeoLogger;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicBoundingBox;
import Dataverse.DataverseJavaObject;
import Dataverse.ExistingGeoLabels;
import Dataverse.ExistingGeoLabelsVals;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import Dataverse.GDAL;
import GeoServer.GeoServerAPI;
import _Strings.GeodisyStrings;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import java.util.*;
import java.util.regex.Pattern;

import static _Strings.DVFieldNameStrings.GEOMETRY;
import static _Strings.GeoBlacklightStrings.*;
import static _Strings.GeodisyStrings.DATA_DIR_LOC;

public class GeoFiles {
    LinkedList<GBLFileToFix> gBLFs;
    String pID;
    String folder;
    ExistingGeoLabelsVals eGLV;
    GeoLogger logger = new GeoLogger(this.getClass());
    public GeoFiles(LinkedList<GBLFileToFix> gBLFs) {
        this.gBLFs = gBLFs;
        pID = gBLFs.getFirst().getPID();
        folder = GeodisyStrings.removeHTTPSAndReplaceAuthority(pID).replace(".","/");
        eGLV = ExistingGeoLabelsVals.getExistingGeoLabelsVals();
    }
    public void dealWithGBLFs(){
        System.out.println("Starting to find data files for" + pID);
        File datafolder = new File(DATA_DIR_LOC + folder);
        if (!datafolder.exists()) {
            System.out.println("Bad file name path: " + DATA_DIR_LOC+folder);
            return;
        }
        LinkedList<Record> records = new LinkedList<>();
        HashSet<String> processed = new HashSet<>();
        gBLFs.sort(new SortByGeoLabel());
        int counter = 0;
        for(GBLFileToFix gBLF:gBLFs){
            File[] files = datafolder.listFiles();
            Arrays.sort(files, new SortFileByFileName());
            for(File f: files){
                if(processed.contains(f.getName()))
                    continue;
                String fileName = f.getName();
                if(!fileName.contains(".")||fileName.endsWith(".aux.xml")) {
                    processed.add(fileName);
                    continue;
                }
                String fileBaseName = fileName.substring(0,fileName.lastIndexOf("."));
                String fileExtension = fileName.substring(fileName.lastIndexOf(".")+1);
                String geoserverLable;
                String dVFileName = gBLF.getDvjsonFileInfo().getFileName();
                String baseDVF;
                if(dVFileName.contains("."))
                    baseDVF  = dVFileName.substring(0,dVFileName.lastIndexOf("."));
                else
                    baseDVF = dVFileName;
                int basefileEnd = Math.min(dVFileName.length(),3);
                boolean isFile = fileBaseName.startsWith(baseDVF.substring(0,basefileEnd));
                if(gBLF.isRaster &&  isFile && (fileExtension.equals("tif"))){
                    Record record = new Record();
                    GDAL gdal = new GDAL();
                    record.gbb = gdal.generateBB(f,"ddd","1");
                    if(record.gbb.hasBB()) {
                        geoserverLable = uploadRasterFile(f, gBLF);
                        if (!geoserverLable.isEmpty()) {
                            record.g = gBLF;
                            record.geoserverLabel = geoserverLable;
                            record.format = "GeoTIFF";
                            record.geomType = "Raster";
                            if (records.size() > 0) {
                                Record first = records.get(0);
                                record.geoserverIDs.addAll(first.geoserverIDs);
                                record.geoserverIDs.add(first.geoserverLabel);
                                updateRecords(records, geoserverLable);
                            }

                            records.add(record);
                        }
                        processed.add(f.getName());
                        counter++;
                        break;
                    }
                }else if(!gBLF.isRaster&& isFile && (fileExtension.equals("shp"))) {
                    GDAL gdal = new GDAL();
                    Record record = new Record();
                    record.gbb = gdal.generateBB(f, "ddd", "1");
                    if (record.gbb.hasBB()) {
                        geoserverLable = uploadVectorFile(f, gBLF);
                        if (!geoserverLable.isEmpty()) {
                            record.gbb = gdal.generateBB(f, "ddd", "1");
                            record.g = gBLF;
                            record.geoserverLabel = geoserverLable;
                            record.format = "Shapefile";
                            record.geomType = record.gbb.getField(GEOMETRY);
                            if (records.size() > 0) {
                                Record first = records.get(0);
                                record.geoserverIDs.addAll(first.geoserverIDs);
                                record.geoserverIDs.add(first.geoserverLabel);
                                updateRecords(records, geoserverLable);
                            }
                            records.add(record);
                        }
                        processed.add(f.getName());
                        counter++;
                        break;
                    }
                }
            }
        }
        if(gBLFs.size()>counter && counter > 0){
            GBLFileToFix first = gBLFs.getFirst();
            String folder = first.gblJSONFilePath.substring(0,first.gblJSONFilePath.lastIndexOf("/")+1);
            int end = gBLFs.size();
            for(int i = end; i>counter; i--){
                File json = new File(folder+i+"geoblacklight.json");
                File rename = new File(folder+i+"obsolete_geoblacklight_json");
                json.renameTo(rename);
            }
        }
        rewriteGBLJSONS(records);
    }

    private void rewriteGBLJSONS(LinkedList<Record> records) {
        records.sort(new SortRecordByLabel());
        for(Record r: records){
            String origLabel = r.g.geoserverLabel;
            String gBLJSON = r.g.geoblacklightJSON;
            gBLJSON = gBLJSON.replace(origLabel,r.geoserverLabel);
            try {

                JSONObject gBLObject = new JSONObject(gBLJSON);
                JSONArray source;
                //Related Records
                if(r.geoserverIDs.size()>0) {
                    source = new JSONArray();
                    for (String s : r.geoserverIDs) {
                        source.put(s);
                    }
                    gBLObject.put("dc_source_sm", source);
                }
                //UPDATE Bounding Box
                if(r.gbb.hasBB()) {
                    String bb = "ENVELOPE(" + getBBString(r.gbb.getBB()) + ")";
                    gBLObject.remove("solr_geom");
                    gBLObject.put("solr_geom", bb);
                }
                //UPDATE GEOM and Format
                gBLObject.remove("layer_geom_type_s");
                gBLObject.put("layer_geom_type_s", r.gbb.getField(GEOMETRY));
                gBLObject.remove("dc_format_s");
                gBLObject.put("dc_format_s",r.format);

                //UPDATE slug and layer_id
                String slug = r.geoserverLabel.substring(r.geoserverLabel.indexOf(":")+1);
                gBLObject.put("layer_slug_s",slug);


                //ADD WMS functionality
                JSONObject refs = new JSONObject(gBLObject.getString(EXTERNAL_SERVICES));
                refs.put(WMS, GEOSERVER_WMS_LOCATION);
                //Add WFS if vector data
                if (!r.g.isRaster)
                    refs.put(WFS, GEOSERVER_WFS_LOCATION);
                gBLObject.put(EXTERNAL_SERVICES, refs.toString());
                try (PrintWriter out = new PrintWriter(r.g.getGblJSONFilePath())) {
                    out.println(gBLObject.toString());
                } catch (FileNotFoundException e) {
                    logger.error("Something went wrong updating the GBLJSON at " + r.g.getGblJSONFilePath() + " with " + gBLJSON);
                }
            } catch (JSONException err) {
                logger.error("Error parsing json: " + gBLJSON);
            }
        }
    }

    private String getBBString(BoundingBox bb){
        return bb.getLongWest() + ", " + bb.getLongEast() + ", " + bb.getLatNorth() + ", " + bb.getLatSouth();
    }

    private String uploadVectorFile(File f, GBLFileToFix gBLF) {
        String geoserverLabel = eGLV.addVector(gBLF.getPID(), f.getName());
        DataverseJavaObject djo = new DataverseJavaObject("server");
        djo.setPID(gBLF.getPID());
        GeoServerAPI geoServerAPI = new GeoServerAPI(djo);
        boolean success = geoServerAPI.addVector(f.getName(),geoserverLabel);
        if(success) {
            ExistingGeoLabels eGL = ExistingGeoLabels.getExistingLabels();
            eGL.addOrReplaceGeoLabel(geoserverLabel, djo.getPID(), f.getName());
            return geoserverLabel;
        }
        else
            return "";
    }

    private String uploadRasterFile(File f, GBLFileToFix gBLF) {
        String geoserverLabel = eGLV.addRaster(gBLF.getPID(),f.getName());
        DataverseJavaObject djo = new DataverseJavaObject("server");
        djo.setPID(gBLF.getPID());
        GeoServerAPI geoServerAPI = new GeoServerAPI(djo);
        gBLF.geoserverLabel = geoserverLabel;
        boolean success = geoServerAPI.addRaster(f.getName(),geoserverLabel);
        if(success) {
            ExistingGeoLabels eGL = ExistingGeoLabels.getExistingLabels();
            eGL.addOrReplaceGeoLabel(geoserverLabel, djo.getPID(), f.getName());
            return geoserverLabel;
        }
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
        GeographicBoundingBox gbb;
        String format;
        String geomType;
        Record(){
            geoserverIDs = new LinkedList<>();
            gbb = new GeographicBoundingBox("a");
        }
    }

    class SortByGeoLabel implements Comparator<GBLFileToFix>{
        public int compare(GBLFileToFix a, GBLFileToFix b){
            return sortLabelNumber(a.geoserverLabel,b.geoserverLabel);
        }


    }

    class SortFileByFileName implements Comparator<File>{
        public int compare(File a, File b){
            return a.getName().compareToIgnoreCase(b.getName());
        }
    }

    class SortRecordByLabel implements Comparator<Record>{
        public int compare(Record o1, Record o2) {
            return sortLabelNumber(o1.geoserverLabel,o2.geoserverLabel);
        }
    }

    public int sortLabelNumber(String label1, String label2){
        int one = findNumber(label1);
        int two = findNumber(label2);
        return one-two;
    }

    public int findNumber(String label){
        int slugLength = label.length();
        String num = "";
        for(int i = slugLength-1; i>0; i--){
            String character = label.substring(i,i+1);
            if(character.equals("v")||character.equals("r"))
                break;
            if(Pattern.matches("\\d",character))
                num = character + num;
        }
        return Integer.parseInt(num);
    }
}
