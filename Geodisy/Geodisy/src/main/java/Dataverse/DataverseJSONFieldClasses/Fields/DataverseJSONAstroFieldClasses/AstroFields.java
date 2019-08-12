package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONAstroFieldClasses;

import BaseFiles.GeoLogger;
import Dataverse.DataverseJSONFieldClasses.MetadataType;
import Dataverse.DataverseJSONFieldClasses.MetadataWSimple;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import static Dataverse.DVFieldNameStrings.*;

public class AstroFields extends MetadataWSimple {
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
    public void setFields(JSONObject jo) {
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
