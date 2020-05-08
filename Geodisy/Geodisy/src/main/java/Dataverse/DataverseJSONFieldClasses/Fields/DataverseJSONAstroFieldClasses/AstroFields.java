package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONAstroFieldClasses;

import BaseFiles.GeoLogger;
import Dataverse.DataverseJSONFieldClasses.MetadataSimple;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import static Strings.DVFieldNameStrings.*;

public class AstroFields extends MetadataSimple {
    private SimpleAstroFields simpleAstroFields;
    private Spatial spatialFields;
    private Spectral spectralFields;
    private Temporal temporalFields;
    private Redshift redshiftFields;
    protected String doi;
    GeoLogger logger = new GeoLogger(AstroFields.class);

    public AstroFields(String doi) {
        this.doi = doi;
        this.simpleAstroFields = new SimpleAstroFields();
        this.spatialFields = new Spatial(doi);
        this.spectralFields = new Spectral(doi);
        this.temporalFields = new Temporal(doi);
        this.redshiftFields = new Redshift(doi);
    }

    @Override
    public AstroFields setFields(JSONObject jo) {
        String fieldName = jo.getString(TYPE_NAME).toLowerCase();
        Object val =  jo.get(VAL);
        if(fieldName.contains(REDSHIFT_FIELDS))
            redshiftFields.setFields((JSONArray) val);
        else if(fieldName.contains(TEMPORAL_FIELDS))
            temporalFields.setFields((JSONArray) val);
        else if(fieldName.contains(SPECTRAL_FIELDS))
            spectralFields.setFields((JSONArray) val);
        else if(fieldName.contains(SPATIAL_FIELDS))
            spatialFields.setFields((JSONArray) val);
        else
            simpleAstroFields.setFields((JSONArray) val);
        return this;
    }
    @Override
    public String getField(String field){
        String fieldName = field.toLowerCase();
        if(fieldName.contains(REDSHIFT_FIELDS))
            return redshiftFields.getField(field);
        if(fieldName.contains(TEMPORAL_FIELDS))
            return temporalFields.getField(field);
        if(fieldName.contains(SPECTRAL_FIELDS))
            return spectralFields.getField(field);
        if(fieldName.contains(SPATIAL_FIELDS))
            return spatialFields.getField(field);
        return simpleAstroFields.getField(field);
    }
    @Override
    public List getListField(String field) {
        String fieldName = field.toLowerCase();
        if(fieldName.contains(REDSHIFT_FIELDS))
            return redshiftFields.getListField(field);
        if(fieldName.contains(TEMPORAL_FIELDS))
            return temporalFields.getListField(field);
        if(fieldName.contains(SPECTRAL_FIELDS))
            return spectralFields.getListField(field);
        logger.error("Tried to get an invalid list, \"" + field + "\" from Astrofields");
        return new LinkedList<>();
    }

    @Override
    public String getDoi() {
        return doi;
    }

    @Override
    public void setDoi(String doi) {
        this.doi = doi;
    }


}
