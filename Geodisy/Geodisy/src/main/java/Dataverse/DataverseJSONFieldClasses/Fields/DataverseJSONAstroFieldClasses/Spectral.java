package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONAstroFieldClasses;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONArray;
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
        JSONArray array = (JSONArray) field.get("value");
        WaveLengthExtents waveLengthExtents =  new WaveLengthExtents();
        for(Object o: array){
            JSONObject jo = (JSONObject) o;
            String typeName = jo.getString("typeName");
            String value = jo.getString("value");
            switch(typeName){
                case("minimumWavelength"):
                    waveLengthExtents.setMinWavelength(value);
                    break;
                case("maximumWavelength"):
                    waveLengthExtents.setMaxWavelength(value);
                    break;
                default:
                    logger.error("Something went wrong with field " + typeName + " in Spectral when trying to parse");
            }
        }

    }

    @Override
    public String getField(String fieldName) {
        return null;
    }
}
