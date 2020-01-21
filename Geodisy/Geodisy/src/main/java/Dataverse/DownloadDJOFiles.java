package Dataverse;


import BaseFiles.GeodisyStrings;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DownloadDJOFiles {
    DataverseJavaObject djo;

    public DataverseJavaObject downloadFiles(DataverseJavaObject djo){
        this.djo = djo;
        File f = new File("datasetFiles/" + djo.urlized(djo.citationFields.getDOI()));
        djo.deleteDir(f);
        List<DataverseRecordFile> temp = new LinkedList<>();
        DataverseRecordFile tempDRF = new DataverseRecordFile();
        for (DataverseRecordFile dRF : djo.dataFiles) {
            if(GeodisyStrings.fileTypesToIgnore(dRF.title))
                continue;
             dRF.retrieveFile(djo);
            DataverseGeoRecordFile tempGeoRecord = new DataverseGeoRecordFile();
            if(GeodisyStrings.hasGeospatialFile(tempDRF.getTitle())) {
                tempGeoRecord = dRF.translateFile(djo);
                if(tempGeoRecord.hasValidBB())
                    djo.addGeoDataFile(tempGeoRecord);
            }
        }

        int vector = 1;
        int raster = 1;
        for(Iterator<DataverseGeoRecordFile> iterator = djo.geoDataFiles.iterator(); iterator.hasNext();){
            DataverseRecordFile geoDRF = iterator.next();
            if(geoDRF.getTitle().endsWith(".shp")){
                if(geoDRF.hasValidBB()) {
                    geoDRF.setFileNumber(vector);
                    vector++;
                }else
                    iterator.remove();
            }else if(geoDRF.getTitle().endsWith(".tif")){
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
