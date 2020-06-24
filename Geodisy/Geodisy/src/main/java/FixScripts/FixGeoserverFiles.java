package FixScripts;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import static _Strings.GeodisyStrings.BACKEND_ADDRESS;

public class FixGeoserverFiles {
    LinkedList<GeoserverFile> files;
    LinkedList<DVJSONFileInfo> dvJSONInfoFiles;

    public FixGeoserverFiles() {
        files = new LinkedList<>();
        dvJSONInfoFiles = new LinkedList<>();
    }

    //Get Info From GeoBlacklight JSONs
    public void searchJSONS(){
        String startingLocation = "/var/www/" + BACKEND_ADDRESS + "/html/geodisy/";
        File file =  new File(startingLocation);
        getFiles(file);

    }

    private void getFiles(File file) {
        if(file.getName().equals("geoblacklight.json")){
            GeoserverFile gf = parse(file);
            if(gf.isGF()) {
                getDVInfo(gf);
                //TODO Have gotten GBLJSON info and DV info, need to send files to geoserver and update the GBL json
            }
        }if(file.isDirectory()){
            for(File f:file.listFiles()){
                getFiles(f);
            }
        }
    }

    private GeoserverFile parse(File file) {
        ParseGBLJSON parser = new ParseGBLJSON();
        GeoserverFile gf = new GeoserverFile();
        try {
            JSONObject jO = parser.readJSON(file);
            if(parser.isGeospatial(jO)) {
                gf.setIsGF(true);
                gf.setDbID(parser.getDBID(jO));
                gf.setDoi(parser.getDOI(jO));
                gf.setGeoserverLabel(parser.getGeoserverLabel(jO));
                gf.setBoundBoxNumberAndType(parser.getBoundingBoxNumberAndType(jO));
                gf.setGeoblacklightJSON(jO.toString());
                gf.setGblJSONFilePath(file.getAbsolutePath());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return gf;
    }

    private GeoserverFile getDVInfo(GeoserverFile gf){
        DVJSON dvjson = new DVJSON();
        gf.setDvjsonFileInfo(dvjson.getFileInfo(gf.getDoi()));
        return gf;
    }
}
