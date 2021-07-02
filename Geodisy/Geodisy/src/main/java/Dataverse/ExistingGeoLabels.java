package Dataverse;

import BaseFiles.FileWriter;
import BaseFiles.GeoLogger;
import _Strings.GeodisyStrings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

import static _Strings.GeodisyStrings.EXISTING_GEO_LABELS;


public class ExistingGeoLabels extends ExisitingFile implements Serializable {
    private static final long serialVersionUID = 5416844497895403201L;
    private HashMap<String, String> geoFiles;
    private static ExistingGeoLabels single_instance = null;

    public static ExistingGeoLabels getExistingLabels() {
        if (single_instance == null) {
            single_instance = new ExistingGeoLabels();
        }
        return single_instance;
    }

    private ExistingGeoLabels(){
        geoFiles = readExistingGeoLabels();
    }

    public void addOrReplaceGeoLabel(String geoserverID, String pID, String fileName){
        if(!hasGeoFile(pID,fileName))
            geoFiles.put(geoserverID, pID+fileName);
    }

    public HashMap<String, String> readExistingGeoLabels(){
        String path = GeodisyStrings.replaceSlashes(EXISTING_GEO_LABELS);
        HashMap<String, String> newFile = new HashMap<>();
        FileWriter fw = new FileWriter();
        File checkBlank = new File(path);
        if(checkBlank.toString().isEmpty())
            return newFile;
        try {
            geoFiles =  (HashMap<String, String>) fw.readSavedObject(path);
            return geoFiles;
        } catch (FileNotFoundException e){
            return newFile;
        } catch (IOException e) {
            getLogger().error("IO Exception: Something went wrong reading " + path);
            return newFile;
        } catch (ClassNotFoundException e) {
            getLogger().error("Class Not Found Error: Something went wrong parsing " + path + " or file was empty");
            return newFile;
        }
    }
    public void removeRecord(String pID, String filename){
        Set<String> keys = geoFiles.keySet();
        for(String k: keys){
            if(geoFiles.get(k).equals(pID+filename)){
                geoFiles.remove(k);
                break;
            }

        }
    }

    public HashMap<String, String> getGeoLabels() {
        return geoFiles;
    }

    public boolean hasGeoFile(String pID, String filename){
        Set<String> keys = geoFiles.keySet();
        for(String k: keys){
            if(geoFiles.get(k).equals(pID+filename)){
                return true;
            }
        }
        return false;
    }

    public boolean hasGeoLabel(String label){
        return geoFiles.keySet().contains(label);
    }

    public String getGeoLabel(String pID, String filename){
        Set<String> keys = geoFiles.keySet();
        for(String k: keys) {
            if (geoFiles.get(k).equals(pID + filename)) {
                return k;
            }
        }
        return "error";
    }

    @Override
    protected GeoLogger getLogger() {
        if (logger == null) {
            logger = new GeoLogger(this.getClass());
        }
        return logger;
    }
}
