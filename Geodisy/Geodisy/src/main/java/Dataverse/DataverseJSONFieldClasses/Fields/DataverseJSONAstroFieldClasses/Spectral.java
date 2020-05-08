package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONAstroFieldClasses;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import static Strings.DVFieldNameStrings.*;

public class Spectral extends CompoundJSONField {
    private String resolution, doi;
    private List<Float> centralWavelength;
    private List<WaveLengthExtents> waveLengths;
    private List<String> bandpass;

    public Spectral(String doi) {
        this.resolution = "";
        this.bandpass = new LinkedList<>();
        this.centralWavelength = new LinkedList<>();
        this.doi = doi;
        this.waveLengths = new LinkedList<>();
    }

    @Override
    public void setField(JSONObject field) {
        String fieldName  = field.getString(TYPE_NAME);
        if(fieldName.equals(SPECTRAL_WAVELENGTH_COVERAGE)) {
            JSONArray array = (JSONArray) field.get(VAL);
            WaveLengthExtents waveLengthExtents = new WaveLengthExtents();
            for (Object o : array) {
                waveLengthExtents.setField((JSONObject) o);
            }
            waveLengths.add(waveLengthExtents);
        }else{
            String val = field.getString(VAL);
            setField(fieldName, val);
        }
    }

    public void setField(String field, String val){
        switch(field) {
            case SPECTRAL_RESOLUTION: resolution = val; break;
            case SPECTRAL_BANDPASS_COVERAGE: bandpass.add(val); break;
            case SPECTRAL_CENTRAL_WAVELENGTH_COVERAGE: centralWavelength.add(Float.parseFloat(val)); break;
            default: errorParsing(this.getClass().toString(), field); break;
        }
    }
    @Override
    public String getField(String fieldName) {
        switch (fieldName) {
            case SPECTRAL_RESOLUTION:  return resolution;
            case ("doi"): return doi;
            case SPECTRAL_BANDPASS_COVERAGE:
            case SPECTRAL_CENTRAL_WAVELENGTH_COVERAGE:
            default:
                logger.error("Strange fieldName requested because of field name or because field is list and getListField should have been called. The file name: " + fieldName + ". Record doi: " + doi);
                return "ERROR in Spectral get";
        }
    }

        public List getListField(String fieldName){
        switch(fieldName){
            case SPECTRAL_WAVELENGTH_COVERAGE: return waveLengths;
            case SPECTRAL_BANDPASS_COVERAGE: return bandpass;
            case SPECTRAL_CENTRAL_WAVELENGTH_COVERAGE: return centralWavelength;
            default: errorGettingValue(this.getClass().toString(), fieldName); return new LinkedList();
        }
    }
}
