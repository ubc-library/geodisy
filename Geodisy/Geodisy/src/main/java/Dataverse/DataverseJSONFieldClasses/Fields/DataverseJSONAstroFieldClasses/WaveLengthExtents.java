package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONAstroFieldClasses;

import Dataverse.DataverseJSONFieldClasses.JSONField;


public class WaveLengthExtents extends JSONField {
    float minWavelength, maxWavelength;

    //TODO implement Class methods

    @Override
    public String getField(String fieldName) {
        return null;
    }

    public void setMinWavelength(float minWavelength) {
        this.minWavelength = minWavelength;
    }
    public void setMinWavelength(String minWavelength) {
        this.minWavelength = Float.parseFloat(minWavelength);
    }

    public void setMaxWavelength(float maxWavelength) {
        this.maxWavelength = maxWavelength;
    }

    public void setMaxWavelength(String maxWavelength) {
        this.maxWavelength = Float.parseFloat(maxWavelength);
    }
}
