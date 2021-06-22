package Dataverse;


import _Strings.GeodisyStrings;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static _Strings.GeodisyStrings.DATA_DIR_LOC;

public class DownloadDJOFiles {
    DataverseJavaObject djo;

    public DataverseJavaObject downloadFiles(DataverseJavaObject djo){
        this.djo = djo;
        File f = new File(DATA_DIR_LOC + djo.urlized(djo.citationFields.getPID()));
        try {
            djo.deleteDir(f);
        } catch (IOException e) {
            System.out.println(e);
        }
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
