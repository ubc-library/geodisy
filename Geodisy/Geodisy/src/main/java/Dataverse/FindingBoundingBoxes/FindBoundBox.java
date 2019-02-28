package Dataverse.FindingBoundingBoxes;

import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import Dataverse.FindingBoundingBoxes.LocationTypes.Country;

public interface FindBoundBox {
    BoundingBox getDVBoundingBox(String country);
    BoundingBox getDVBoundingBox(String country, String state);
    BoundingBox getDVBoundingBox(String country, String state, String city);
    BoundingBox getDVBoundingBox(String country, String state, String city, String other);
    BoundingBox getDVBoundingBoxOther(String other);

}