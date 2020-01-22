package Dataverse;


import BaseFiles.GeodisyStrings;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static BaseFiles.GeodisyStrings.DATASET_FILES_PATH;

public class DownloadDJOFiles {
    DataverseJavaObject djo;

    public DataverseJavaObject downloadFiles(DataverseJavaObject djo){
        this.djo = djo;
        File f = new File(DATASET_FILES_PATH + djo.urlized(djo.citationFields.getDOI()));
        djo.deleteDir(f);
        List<DataverseRecordFile> temp = new LinkedList<>();
        DataverseRecordFile tempDRF = new DataverseRecordFile();
        for (DataverseRecordFile dRF : djo.dataFiles) {
            if(GeodisyStrings.fileTypesToIgnore(dRF.translatedTitle))
                continue;
             dRF.retrieveFile(djo);
            DataverseGeoRecordFile tempGeoRecord = new DataverseGeoRecordFile();
            if(GeodisyStrings.hasGeospatialFile(tempDRF.getTranslatedTitle())) {
                dRF.translateFile(djo);

            }
        }

        int vector = 1;
        int raster = 1;
        Iterator<DataverseGeoRecordFile> iterator = djo.geoDataFiles.iterator();
        while(iterator.hasNext()){
            DataverseGeoRecordFile geoDRF = iterator.next();
            if(geoDRF.getTranslatedTitle().endsWith(".shp")){
                if(geoDRF.hasValidBB()) {
                    geoDRF.setFileNumber(vector);
                    vector++;
                }else
                    iterator.remove();
            }else if(geoDRF.getTranslatedTitle().endsWith(".tif")){
                if(geoDRF.hasValidBB()) {
                    geoDRF.setFileNumber(raster);
                    raster++;
                }else
                    iterator.remove();
            }else
                iterator.remove();
        }
        return djo;
    }
}
