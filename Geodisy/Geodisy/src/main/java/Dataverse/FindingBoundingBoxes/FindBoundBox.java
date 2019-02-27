package Dataverse.FindingBoundingBoxes;

import Dataverse.FindingBoundingBoxes.LocationTypes.Country;

public interface FindBoundBox {
    GeographicUnit getDVBoundingBox(String country);
    GeographicUnit getDVBoundingBox(String country, String state);
    GeographicUnit getDVBoundingBox(String country, String state, String city);
    GeographicUnit getDVBoundingBox(String country, String state, String city, String other);
    GeographicUnit getDVBoundingBoxOther(String other);

}