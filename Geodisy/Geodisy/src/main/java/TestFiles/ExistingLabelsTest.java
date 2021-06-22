package TestFiles;

import Dataverse.ExistingGeoLabels;
import Dataverse.ExistingGeoLabelsVals;

public class ExistingLabelsTest implements Test{
    ExistingGeoLabels eGL;
    ExistingGeoLabelsVals eGLV;

    public void run(){
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
