package Dataverse.FindingBoundingBoxes;

import Dataverse.FindingBoundingBoxes.LocationTypes.Country;

public interface FindBoundBox {
    public Country getDVBoundingBox(String country);
    public Country getDVBoundingBox(String country, String state);
    public Country getDVBoundingBox(String country, String state, String city);
    public Country getDVBoundingBox(String country, String state, String city, String other);
    public Country getDVBoundingBoxOther(String other);

}