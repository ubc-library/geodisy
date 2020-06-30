package FixScripts;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;

public class FixGeoserverFiles {
    LinkedList<GBLFileToFix> files;
    DVJSONFileInfo dvJSONInfoFile;

    public FixGeoserverFiles() {
        files = new LinkedList<>();
    }

    //Get Info From GeoBlacklight JSONs
    public void startFixProcess(){
        File fileStart = null;
        try {
            String path = FixGeoserverFiles.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            path = path.substring(0,path.indexOf("Geodisy.jar"));
            fileStart = new File(path);
            System.out.println(fileStart.getAbsolutePath());
        } catch (URISyntaxException var5) {
            var5.printStackTrace();
        }
        /*String startingLocation = "/var/www/" + BACKEND_ADDRESS + "/html/geodisy/";
        File file =  new File(startingLocation);*/

        getFiles(fileStart);

    }

    private void getFiles(File file) {
        if(file.isDirectory()){
            boolean isBase = false;
            for(File f: file.listFiles()){
                if(f.getName().contains(".zip")) {
                    isBase = true;
                    System.out.println("Got to zip level");
                }
            }
            //If at a level that contains an ISO XML zip file start dealing with Geoblacklight json files because we've gotten to the single dataset depth in this branch
            if(isBase) {
                LinkedList<GBLFileToFix> records = getSpecificPIDFiles(file, new LinkedList<GBLFileToFix>());
                dealWithGeoFiles(records);
            }else {
                for (File f : file.listFiles()) {
                    getFiles(f);
                }
            }
        }
    }
    //get info from all the GBLJson files for a given PID
    private LinkedList<GBLFileToFix> getSpecificPIDFiles(File file, LinkedList<GBLFileToFix> gBLFs){
        if(file.getName().equals("geoblacklight.json")) {
            GBLFileToFix gBLF = getGBLInfo(file);
            System.out.println("At GBLJSON level");
            if (gBLF.isGF()) {
                System.out.println("Found a geospatial file");
                gBLF = getDVInfo(gBLF);
                if (!gBLF.getDvjsonFileInfo().getFileName().equals("")) {
                    gBLFs.add(gBLF);
                    System.out.println("Got DV info for " +gBLF.getPID());
                }
            }
        }else{
            if(file.isDirectory()){
                for(File f:file.listFiles()){
                    gBLFs = getSpecificPIDFiles(f,gBLFs);
                }
            }
        }
        return gBLFs;
    }

    private void dealWithGeoFiles(LinkedList<GBLFileToFix> gBLFs) {
        if(gBLFs.size()>0) {
            GeoFiles geo = new GeoFiles(gBLFs);
            geo.dealWithGBLFs();
        }
    }

    private GBLFileToFix getGBLInfo(File file) {
        ParseGBLJSON parser = new ParseGBLJSON();
        GBLFileToFix gBLF = new GBLFileToFix();
        try {
            JSONObject jO = parser.readJSON(file);
            if(parser.isGeospatial(jO)) {
                gBLF.setIsGF(true);
                gBLF.setDbID(parser.getDBID(jO));
                gBLF.setpID(parser.getDOI(jO));
                gBLF.setGeoserverLabel(parser.getGeoserverLabel(jO));
                gBLF.setBoundBoxNumberAndType(parser.getBoundingBoxNumberAndType(jO));
                gBLF.setGeoblacklightJSON(jO.toString());
                gBLF.setGblJSONFilePath(file.getAbsolutePath());

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return gBLF;
    }

    private GBLFileToFix getDVInfo(GBLFileToFix gBLF){
        DVJSON dvjson = new DVJSON();
        gBLF.setDvjsonFileInfo(dvjson.getFileInfo(gBLF.getPID(),gBLF.getDbID()));
        return gBLF;
    }
}
