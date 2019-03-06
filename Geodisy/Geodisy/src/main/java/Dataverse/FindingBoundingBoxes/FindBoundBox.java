package Dataverse.FindingBoundingBoxes;

import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;

import java.io.IOException;
import java.net.HttpURLConnection;

public abstract class FindBoundBox {
    abstract BoundingBox getDVBoundingBox(String country);
    abstract BoundingBox getDVBoundingBox(String country, String state) throws IOException;
    abstract BoundingBox getDVBoundingBox(String country, String state, String city);
    abstract BoundingBox getDVBoundingBox(String country, String state, String city, String other);
    abstract BoundingBox getDVBoundingBoxOther(String other);
    abstract HttpURLConnection getHttpURLConnection(String country);
    //TODO get HTTP response (XML) and parse for boundingbox coordinates
    BoundingBox readResponse(int status,HttpURLConnection con){
        BoundingBox box = new BoundingBox();

        return box;
    }


}