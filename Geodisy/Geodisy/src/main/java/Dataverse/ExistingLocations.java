package Dataverse;

import BaseFiles.FileWriter;
import BaseFiles.GeoLogger;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import static _Strings.GeodisyStrings.*;

/**
 * Class that holds bounding boxes that have already been found and what versions of records have already been downloaded.
 */
public class ExistingLocations extends ExistingSearches implements Serializable {
    private static final long serialVersionUID = 7847943825774008362L;
    private static HashMap<String, BoundingBox> bBoxes;
    private static HashMap<String, String[]> locationNames;
    private static ExistingLocations single_instance = null;

    public static ExistingLocations getExistingLocations(){

        if (single_instance == null) {
            single_instance = new ExistingLocations();
        }

        return single_instance;
    }

    private ExistingLocations(){
        bBoxes = readExistingBoundingBoxes();
        locationNames = readExistingAltNames();
    }

    public void addBBox(String country, BoundingBox boundingBox){
        bBoxes.put(country,boundingBox);
    }

    public void addBBox(String country, String province, BoundingBox boundingBox){
        bBoxes.put(country+"zzz"+province,boundingBox);
    }
    public void addBBox(String country, String province, String city, BoundingBox boundingBox){
        bBoxes.put(country+"zzz"+province+"zzz"+city,boundingBox);
    }

    private boolean hasBBox(String location){
        return bBoxes.containsKey(location);
    }

    private BoundingBox getBBox(String location){
        if(hasBBox(location))
            return bBoxes.get(location);
        return new BoundingBox();
    }

    public boolean hasBB(String country, String province, String city){
        return hasBBox(country+"zzz"+province+"zzz"+city);
    }

    public boolean hasBB(String country, String province){
        return hasBBox(country+"zzz"+province);
    }

    public boolean hasBB(String country){
        return hasBBox(country);
    }

    public BoundingBox getBB(String country){
        return getBBox(country);
    }

    public BoundingBox getBB(String country, String province) {
        return getBBox(country + "zzz" + province);
    }

    public BoundingBox getBB(String country, String province, String city) {
        return getBBox(country + "zzz" + province + "zzz" + city);
    }

    private boolean hasNames(String location){
        return locationNames.containsKey(location);
    }

    public void addNames(String country, String common, String altNamesString, String countryCode){
        String[] names = {common,altNamesString, countryCode};
        locationNames.put(country,names);
    }

    public void addNames(String country, String province, String common,  String altNamesString, String countryCode){
        String[] names = {common,altNamesString,""};
        locationNames.put(country+"zzz"+province,names);
    }

    public void addNames(String country, String province, String city, String common, String altNamesString, String countryCode){
        String[] names = {common,altNamesString,""};
        locationNames.put(country+"zzz"+province+"zzz"+city,names);
    }

    private String[] getNames(String locations){
        if(hasNames(locations))
            return locationNames.get(locations);
        return new String[]{"","",""};
    }

    public boolean hasLocationNames(String country, String province, String city){
        return hasNames(country+"zzz"+province+"zzz"+city);
    }

    public boolean hasLocationNames(String country, String province){
        return hasNames(country+"zzz"+province);
    }

    public boolean hasLocationNames(String country){
        return hasNames(country);
    }

    public String[] getLocationNames(String country){
        return getNames(country);
    }

    public String[] getLocationNames(String country, String province) {
        return getNames(country + "zzz" + province);
    }

    public String[] getLocationNames(String country, String province, String city) {
        return getNames(country + "zzz" + province + "zzz" + city);
    }

    public int numberOfBBoxes(){
        return bBoxes.size();
    }

    public void deleteBBox(String location){
        bBoxes.remove(location);
    }


    public void testSaveExistingSearches(){
        FileWriter fw  = new FileWriter();
        try {
            fw.writeObjectToFile(bBoxes,TEST_EXISTING_BBOXES);
        } catch (IOException e) {
            getLogger().error("Something went wrong saving existing bboxes");
        }
    }

    public HashMap<String, BoundingBox> readExistingBoundingBoxes(){
        HashMap<String, BoundingBox> newFile = new HashMap<>();
        FileWriter fw = new FileWriter();
        try {
            return  (HashMap<String, BoundingBox>) fw.readSavedObject(EXISTING_LOCATION_BBOXES);
        } catch (IOException e) {
            getLogger().error("Something went wrong reading the Existing bBoxes file");
            return newFile;
        } catch (ClassNotFoundException e) {
            getLogger().error("Something went wrong parsing the Existing BBoxes file");
            return newFile;
        }catch (NullPointerException e){
            return newFile;
        }
    }
    public HashMap<String, String[]> readExistingAltNames(){
        HashMap<String, String[]> newFile = new HashMap<>();
        FileWriter fw = new FileWriter();
        try {
            return  (HashMap<String, String[]>) fw.readSavedObject(EXISTING_LOCATION_NAMES);
        } catch (IOException e) {
            getLogger().error("Something went wrong reading the Existing bBoxes file");
            return newFile;
        } catch (ClassNotFoundException e) {
            getLogger().error("Something went wrong parsing the Existing BBoxes file");
            return newFile;
        }catch (NullPointerException e){
            return newFile;
        }
    }


    public HashMap<String, BoundingBox> getbBoxes() {
        return bBoxes;
    }

    public void setbBoxes(HashMap<String, BoundingBox> bboxes){
        bBoxes = bboxes;
    }

    public HashMap<String, String[]> getNames(){
        return locationNames;
    }

    public void setLocationNames(HashMap<String, String[]> names){
        locationNames = names;
    }

    public String getCountryFromCode(String countryCode){
        for(String key: locationNames.keySet()){
            if(key.contains("zzz"))
                continue;
            if(locationNames.get(key)[2].equals(countryCode))
                return key;
        }
        return "";
    }

    @Override
    protected GeoLogger getLogger() {
        if (logger == null) {
            logger = new GeoLogger(this.getClass());
        }
        return logger;
    }
}
