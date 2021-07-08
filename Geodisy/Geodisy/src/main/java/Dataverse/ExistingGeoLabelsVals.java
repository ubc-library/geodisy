package Dataverse;

import BaseFiles.FileWriter;
import BaseFiles.GeoLogger;
import _Strings.GeodisyStrings;

import java.io.IOException;
import java.io.Serializable;

import static _Strings.GeodisyStrings.*;

/**
 * Class that holds what id numbers have been used for Geoserver already
 */
public class ExistingGeoLabelsVals extends ExistingSearches implements Serializable {
    private static final long serialVersionUID = 8947945525774008362L;
    private static int[] lastRasterAndVectorIDs;
    private static ExistingGeoLabelsVals single_instance = null;
    private ExistingGeoLabels eGL;

    public static ExistingGeoLabelsVals getExistingGeoLabelsVals(){

        if (single_instance == null) {
            single_instance = new ExistingGeoLabelsVals();
        }

        return single_instance;
    }

    private ExistingGeoLabelsVals(){
        readExistingIDVals();
        eGL = ExistingGeoLabels.getExistingLabels();

    }

    public String addRaster(String pID, String fileName){
        if(eGL.hasGeoFile(pID,fileName))
            return eGL.getGeoLabel(pID,fileName);
        lastRasterAndVectorIDs[0] = lastRasterAndVectorIDs[0] +1;
        String label = "r" + extendedNum(String.valueOf(lastRasterAndVectorIDs[0]));
        eGL.addOrReplaceGeoLabel(label,pID,fileName);
        return getCurrentRaster();
    }
    public String addVector(String pID, String fileName){
        if(eGL.hasGeoFile(pID,fileName))
            return eGL.getGeoLabel(pID,fileName);
        lastRasterAndVectorIDs[1] = lastRasterAndVectorIDs[1] +1;
        String label = "v" + extendedNum(String.valueOf(lastRasterAndVectorIDs[1]));
        eGL.addOrReplaceGeoLabel(label,pID,fileName);
        return getCurrentVector();
    }

    public String getCurrentRaster(){
        String answer = String.valueOf(lastRasterAndVectorIDs[0]);
        return "r" + extendedNum(answer);
    }

    public String getCurrentVector(){
        String answer = String.valueOf(lastRasterAndVectorIDs[1]);
        return "v" + extendedNum(answer);
    }

    private String extendedNum(String val){
        while(val.length()<10){
            val = "0" + val;
        }
        return val;
    }



    public void testSaveExistingGeoLabels(){
        FileWriter fw  = new FileWriter();
        try {
            fw.writeObjectToFile(lastRasterAndVectorIDs,TEST_EXISTING_BBOXES);
        } catch (IOException e) {
            getLogger().error("Something went wrong saving existing bboxes");
        }
    }

    public void readExistingIDVals(){
        int[] newVals = {0,0};
        FileWriter fw = new FileWriter();
        try {
            lastRasterAndVectorIDs =  (int[]) fw.readSavedObject(GeodisyStrings.replaceSlashes(EXISTING_GEO_LABELS_VALS));
        } catch (IOException e) {
            getLogger().error("Something went wrong reading the Existing GeoLabels file");
            lastRasterAndVectorIDs = newVals;
        } catch (ClassNotFoundException e) {
            getLogger().error("Something went wrong parsing the Existing GeoLabels file");
            lastRasterAndVectorIDs = newVals;
        }catch (NullPointerException e){
            lastRasterAndVectorIDs = newVals;
        }
    }
    public int[] getValues(){
        return lastRasterAndVectorIDs;
    }

    public void saveExistingGeoLabels(){
        saveExistingFile(ExistingGeoLabelsVals.lastRasterAndVectorIDs,EXISTING_GEO_LABELS_VALS,this.getClass().getName());
        ExistingGeoLabels e = ExistingGeoLabels.getExistingLabels();
        saveExistingFile(e.getGeoLabels(),EXISTING_GEO_LABELS,e.getClass().getName());
    }

    @Override
    protected GeoLogger getLogger() {
        if (logger == null) {
            logger = new GeoLogger(this.getClass());
        }
        return logger;
    }
}
