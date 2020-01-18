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
        DataverseRecordFile tempDRF;
        for (DataverseRecordFile dRF : djo.dataFiles) {
            if(GeodisyStrings.fileTypesToIgnore(dRF.title))
                continue;
             tempDRF = dRF.retrieveFile(djo);

            if(GeodisyStrings.hasGeospatialFile(tempDRF.getTitle())) {
                djo.hasGeospatialFile = true;
                djo = dRF.translateFile(djo);
            }
        }

        int vector = 1;
        int raster = 1;
        for(Iterator<DataverseRecordFile> iterator = djo.geoDataFiles.iterator(); iterator.hasNext();){
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
