package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONAstroFieldClasses;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import static Dataverse.DVFieldNameStrings.*;

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

    @Override
    public void setField(JSONObject field) {
        JSONArray array = (JSONArray) field.get(VAL);
        WaveLengthExtents waveLengthExtents =  new WaveLengthExtents();
        for(Object o: array) {
            waveLengthExtents.setField((JSONObject) o);
        }
    }

    @Override
    public String getField(String fieldName) {
        switch (fieldName) {
            case SPECTRAL_RESOLUTION:
                return resolution;
            case SPECTRAL_BANDPASS_COVERAGE:
                return bandpass;
            case SPECTRAL_CENTRAL_WAVELENGTH_COVERAGE:
                return Float.toString(centralWavelength);
            case ("doi"):
                return doi;
            default:
                logger.error("Strange fieldName requested: " + fieldName + ". Record doi: " + doi);
                return "ERROR in Spectral get";
        }
    }

        public List<WaveLengthExtents> getListField(String fieldName){
            if(fieldName.equals(SPECTRAL_WAVELENGTH_COVERAGE))
                return waveLengths;
            else{
                logger.error("Strange fieldName requested: " + fieldName + ". Record doi: " + doi);
                return new LinkedList<>();
        }
    }
}
