package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONAstroFieldClasses;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

import java.util.List;

public class Spectral extends CompoundJSONField {
    private String resolution, bandpass, centralWavelength, coverage;
    private List<WaveLengthExtents> waveLengths;

    //TODO implement Class methods
    @Override
    public void setField(JSONObject field) {

    }

    @Override
    public String getField(String fieldName) {
        return null;
    }
}
