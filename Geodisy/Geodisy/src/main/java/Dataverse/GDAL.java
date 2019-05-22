package Dataverse;

import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicBoundingBox;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicFields;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static BaseFiles.GeodisyStrings.GDAL_LOCATION;

public class GDAL {
    Logger logger = LogManager.getLogger(this.getClass());

    public DataverseJavaObject generateBB(DataverseJavaObject djo) {
        for(DataverseRecordFile dRF: djo.getDataFiles()){
            String filePath = "./datasetFiles/" + dRF.doi + "/" + dRF.title;
            boolean isWindows = System.getProperty("os.name")
                    .toLowerCase().startsWith("windows");
            Process process;
            String gdalString = "";
            String s;

            BoundingBox temp;
            try {
                if (isWindows) {
                    process = Runtime.getRuntime()
                            .exec(String.format("cmd.exe %s", GDAL_LOCATION + filePath));
                } else {
                    process = Runtime.getRuntime()
                            .exec(String.format("sh %s", GDAL_LOCATION + filePath));
                }

                BufferedReader stdInput = new BufferedReader(new
                        InputStreamReader(process.getInputStream()));

                BufferedReader stdError = new BufferedReader(new
                        InputStreamReader(process.getErrorStream()));
                while((s = stdInput.readLine()) != null) {
                    gdalString+= s;
                }
                int start = gdalString.indexOf("Extent: (");
                if(start != -1){
                    int end = gdalString.indexOf(", ",start);
                    temp = getLatLong(gdalString,start,end, djo);
                   GeographicFields gf = djo.getGeoFields();
                   List<GeographicBoundingBox> bboxes = gf.getGeoBBoxes();
                   GeographicBoundingBox gBB =  new GeographicBoundingBox(djo.getDOI());
                    gBB.setEastLongitude(String.valueOf(temp.getLongEast()));
                    gBB.setWestLongitude(String.valueOf(temp.getLongWest()));
                    gBB.setNorthLatitude(String.valueOf(temp.getLatNorth()));
                    gBB.setEastLongitude(String.valueOf(temp.getLatSouth()));
                    gf.addBB(bboxes,gBB);
                    djo.setGeoFields(gf);
                }

            } catch (IOException e) {
                logger.error("Something went wrong trying to call GDAL with " + dRF.title);
            }
        }


        return djo;
    }

    private BoundingBox compare(BoundingBox temp, BoundingBox fullExtent) {
        if(fullExtent.getLongWest()==361||temp.getLongWest()<fullExtent.getLongWest()||(fullExtent.getLongWest()<0 && temp.getLongWest()>=0 && temp.getLongEast()< temp.getLongWest()))
            fullExtent.setLongWest(temp.getLongWest());
        if(fullExtent.getLongEast()==361 || temp.getLongEast()>fullExtent.getLongEast() ||(fullExtent.getLongEast()>=0 && temp.getLongEast()<0 && temp.getLongWest()>temp.getLongEast()))
            fullExtent.setLongEast(temp.getLongEast());
        if(fullExtent.getLatNorth()==361 || temp.getLatNorth()>fullExtent.getLatNorth())
            fullExtent.setLatNorth(temp.getLatNorth());
        if(fullExtent.getLatSouth()==361 || temp.getLatSouth()<fullExtent.getLatSouth())
            fullExtent.setLatSouth(temp.getLatSouth());
        return fullExtent;
    }

    private BoundingBox getLatLong(String gdalString, int start, int end, DataverseJavaObject djo) {
        String long1 = gdalString.substring(start,end);
        start = end+2;
        end = gdalString.indexOf(")",start);
        String lat1 = gdalString.substring(start,end);
        start = gdalString.indexOf("- (",end);
        end = gdalString.indexOf(", ",start);
        String long2 = gdalString.substring(start,end);
        start = end+2;
        end = gdalString.indexOf(")",start);
        String lat2 = gdalString.substring(start,end);
        BoundingBox bb =  new BoundingBox();
        bb.setLongWest(long2);
        bb.setLongEast(long1);
        bb.setLatNorth(lat2);
        bb.setLatSouth(lat1);;
        return bb;
    }
}
