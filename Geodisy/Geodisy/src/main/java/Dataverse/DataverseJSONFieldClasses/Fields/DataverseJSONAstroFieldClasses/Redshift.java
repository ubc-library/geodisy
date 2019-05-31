package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONAstroFieldClasses;

import Dataverse.DataverseJSONFieldClasses.JSONField;

import java.util.List;

public class Redshift extends JSONField {
    String type;
    float resolution;
    List<RedshiftExtents> redshifts;

    //TODO implement Class methods
    @Override
    public String getField(String fieldName) {
        return null;
    }
}
