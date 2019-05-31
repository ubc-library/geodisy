package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONAstroFieldClasses;

import Dataverse.DataverseJSONFieldClasses.JSONField;

import java.util.LinkedList;
import java.util.List;

public class Redshift extends JSONField {
    private String type;
    float resolution;
    private List<RedshiftExtents> redshifts;
    private String doi;

    public Redshift(String doi) {
        this.type = "";
        this.resolution = 0;
        this.redshifts = new LinkedList<>();
        this.doi = doi;
    }

    @Override
    public String getField(String fieldName) {
        switch(fieldName){
            case("redshiftType"):
                return type;
            case("resolution"):
                return String.valueOf(resolution);
            default:
                logger.error("Tried to return field " + fieldName + " from Redshift in doi: " + doi);
                return "";
        }
    }

    public List getListField(String fieldName){
        if(fieldName.equals("redshiftValue"))
            return redshifts;
        logger.error("Tried to return a list of name " + fieldName + " from Redshift with doi: " + doi);
        return new LinkedList();
    }

    public void setField(String fieldName, String val){
        switch(fieldName){
            case("redshiftType"):
                this.type = val;
                break;
            case("resolution"):
                this.resolution = Float.parseFloat(val);
                break;
            default:
                logger.error("Weird fieldName, " + fieldName + ", in Redshift, with doi: " + doi + " and val = " + val);
        }
    }

    public void addRedshiftExtent(RedshiftExtents red){
        this.redshifts.add(red);
    }

    public void addFullRedshiftExtent(List<RedshiftExtents> reds){
        this.redshifts = reds;
    }
}
