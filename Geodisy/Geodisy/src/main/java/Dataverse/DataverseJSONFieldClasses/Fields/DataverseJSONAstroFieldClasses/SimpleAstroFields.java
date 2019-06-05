package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONAstroFieldClasses;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import Dataverse.DataverseJSONFieldClasses.JSONField;
import org.json.JSONObject;

import static Dataverse.DVFieldNameStrings.*;

public class SimpleAstroFields extends CompoundJSONField {
    private String astroType, astroFacility, astroInstrument, astroObject, polarization;
    private float depth, objectDensity, skyFraction;
    private int objectCount;

    public SimpleAstroFields() {
        this.astroType = "";
        this.astroFacility = "";
        this.astroInstrument = "";
        this.astroObject = "";
        this.polarization = "";

    }

    @Override
    public void setField(JSONObject field) {
        String fieldName = field.getString(TYPE_NAME);
        String value = field.getString(VAL);
        switch(fieldName){
            case ASTRO_TYPE:
                setAstroType(value);
                break;
            case ASTRO_FACILITY:
                setAstroFacility(value);
                break;
            case ASTRO_INSTRUMENT:
                setAstroInstrument(value);
                break;
            case ASTRO_OBJECT:
                setAstroObject(value);
                break;
            case POLARIZATION_COVERAGE:
                setPolarization(value);
                break;
            case DEPTH_COVERAGE:
                setDepth(Float.parseFloat(value));
                break;
            case OBJECT_DENSITY_COVERAGE:
                setObjectDensity(Float.parseFloat(value));
                break;
            case SKY_FRACTION_COVERAGE:
                setSkyFraction(Float.parseFloat(value));
                break;
            case OBJECT_COUNT_COVERAGE:
                setObjectCount(Integer.parseInt(value));
                break;
            default:
                errorParsing(this.getClass().getName(),fieldName);
                break;
        }
    }

    @Override
    public String getField(String fieldName) {
        switch(fieldName){
            case ASTRO_TYPE:
                return astroType;
            case ASTRO_FACILITY:
                return astroFacility;
            case ASTRO_INSTRUMENT:
                return astroInstrument;
            case ASTRO_OBJECT:
                return astroObject;
            case POLARIZATION_COVERAGE:
                return polarization;
            case DEPTH_COVERAGE:
                return stringed(depth);
            case OBJECT_DENSITY_COVERAGE:
                return stringed(objectDensity);
            case SKY_FRACTION_COVERAGE:
                return stringed(skyFraction);
            case OBJECT_COUNT_COVERAGE:
                return stringed(objectCount);
                default:
                    errorParsing(this.getClass().getName(),fieldName);
                    return "";
        }
    }


    private void setAstroType(String astroType) {
        this.astroType = astroType;
    }


    private void setAstroFacility(String astroFacility) {
        this.astroFacility = astroFacility;
    }


    private void setAstroInstrument(String astroInstrument) {
        this.astroInstrument = astroInstrument;
    }


    private void setAstroObject(String astroObject) {
        this.astroObject = astroObject;
    }


    private void setPolarization(String polarization) {
        this.polarization = polarization;
    }


    private void setDepth(float depth) {
        this.depth = depth;
    }

    private void setObjectDensity(float objectDensity) {
        this.objectDensity = objectDensity;
    }

    private void setSkyFraction(float skyFraction) {
        this.skyFraction = skyFraction;
    }

    private void setObjectCount(int objectCount) {
        this.objectCount = objectCount;
    }
}
