package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses;

import Dataverse.DataverseJSONFieldClasses.JSONField;

public class GeographicUnit extends JSONField {
    private String geoUnitName;

    public GeographicUnit(String geoUnitName) {
        this.geoUnitName = geoUnitName;
    }

    public void setGeoUnitName(String geoUnitName) {
        this.geoUnitName = geoUnitName;
    }

    @Override
    public String getField(String fieldName) {
        return geoUnitName;
    }
}
