package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONAstroFieldClasses;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class Spectral extends CompoundJSONField {
    private String resolution, bandpass, coverage, doi;
    private float centralWavelength;
    private List<WaveLengthExtents> waveLengths;

    public Spectral(String doi) {
        this.resolution = "";
        this.bandpass = "";
        this.coverage = "";
        this.centralWavelength = 0;
        this.doi = doi;
        this.waveLengths = new LinkedList<>();
    }

    //TODO implement Class methods
    @Override
    public void setField(JSONObject field) {

    }

    @Override
    public String getField(String fieldName) {
        return null;
    }
}
