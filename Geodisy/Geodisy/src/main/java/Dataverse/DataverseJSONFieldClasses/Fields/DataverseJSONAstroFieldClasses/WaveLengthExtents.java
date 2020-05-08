package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONAstroFieldClasses;

import Dataverse.DataverseJSONFieldClasses.JSONField;
import org.json.JSONObject;

import static Strings.DVFieldNameStrings.*;


public class WaveLengthExtents extends JSONField {
    private float minWavelength, maxWavelength;


    @Override
    public String getField(String fieldName) {
        if(fieldName.equals(SPECTRAL_MIN_WAVELENGTH_COVERAGE))
            return stringed(minWavelength);
        if(fieldName.equals(SPECTRAL_MAX_WAVELENGTH_COVERAGE))
            return stringed(maxWavelength);
        logger.error("Odd field call when trying to get " + fieldName + " from WaveLengthExtents");
        return "ERROR in WaveLengthExtents";
    }

    public void setMinWavelength(float minWavelength) {
        this.minWavelength = minWavelength;
    }
    private void setMinWavelength(String minWavelength) {
        this.minWavelength = Float.parseFloat(minWavelength);
    }

    public void setMaxWavelength(float maxWavelength) {
        this.maxWavelength = maxWavelength;
    }

    private void setMaxWavelength(String maxWavelength) {
        this.maxWavelength = Float.parseFloat(maxWavelength);
    }

    public void setField(JSONObject field) {
        String title = field.getString(TYPE_NAME);
        String value = field.getString(VAL);
        setField(title, value);
    }

    public void setField(String title, String value){
        switch(title){
            case SPECTRAL_MAX_WAVELENGTH_COVERAGE:
                setMaxWavelength(value);
                break;
            case SPECTRAL_MIN_WAVELENGTH_COVERAGE:
                setMinWavelength(value);
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

}
