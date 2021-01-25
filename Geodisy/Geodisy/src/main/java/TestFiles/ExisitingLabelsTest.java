package TestFiles;

import Dataverse.ExistingGeoLabels;
import Dataverse.ExistingGeoLabelsVals;

public class ExisitingLabelsTest {
    ExistingGeoLabels eGL;
    ExistingGeoLabelsVals eGLV;

    public void checkEGL(){
        eGL = ExistingGeoLabels.getExistingLabels();
        eGLV = ExistingGeoLabelsVals.getExistingGeoLabelsVals();
        System.out.println(eGLV.addVector("v1230","test.shp"));
        System.out.println(eGLV.addRaster("r1230","test.tif"));
        eGLV.saveExistingGeoLabels();
        System.out.println(eGLV.addVector("v123","test.shp"));
        System.out.println(eGLV.addRaster("r123","test.tif"));
        eGLV.saveExistingGeoLabels();

    }
}
