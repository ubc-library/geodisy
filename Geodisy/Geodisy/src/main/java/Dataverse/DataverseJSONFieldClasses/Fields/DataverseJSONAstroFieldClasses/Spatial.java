package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONAstroFieldClasses;

import Dataverse.DataverseJSONFieldClasses.JSONField;

public class Spatial extends JSONField {
    private String resolution, coverage, doi;

    public Spatial(String doi) {
        this.resolution = "";
        this.coverage = "";
        this.doi = doi;
    }

    //TODO implement Class methods

    public void setField(String label, String value) {
        switch(label) {
            case ("resolution"):
                this.resolution = value;
                break;
            case("coverage"):
                this.coverage = value;
                break;
            default:
                    logger.error("Tried to set a non-existent field in the Spatial fields of the Astrophysics metadata.");
        }

    }

    @Override
    public String getField(String fieldName) {
        switch (fieldName){
            case ("resolution"):
               return resolution;
            case("coverage"):
                return coverage;
            default:
                logger.error("Tried to get a non-existent field in the Spatial fields of the Astrophysics metadata.");
                return "";
        }
    }
}
