package Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses;

import org.json.JSONObject;

public class GeographicBoundingBox extends CompoundJSONField {
    private String westLongitude, eastLongitude, northLongitude, southLongitude;

    public GeographicBoundingBox() {
        this.westLongitude = "";
        this.eastLongitude = "";
        this.northLongitude = "";
        this.southLongitude = "";
    }

    public String getWestLongitude() {
        return westLongitude;
    }

    public void setWestLongitude(String westLongitude) {
        this.westLongitude = westLongitude;
    }

    public String getEastLongitude() {
        return eastLongitude;
    }

    public void setEastLongitude(String eastLongitude) {
        this.eastLongitude = eastLongitude;
    }

    public String getNorthLongitude() {
        return northLongitude;
    }

    public void setNorthLongitude(String northLongitude) {
        this.northLongitude = northLongitude;
    }

    public String getSouthLongitude() {
        return southLongitude;
    }

    public void setSouthLongitude(String southLongitude) {
        this.southLongitude = southLongitude;
    }


    @Override
    public void setField(JSONObject field) {
        String title = field.getString("typeName");
        String value = field.getString("value");
        switch (title) {
            case("westLongitude"):
                this.westLongitude = value;
                break;
            case("eastLongitude"):
                this.eastLongitude = value;
                break;
            case("northLongitude"):
                this.northLongitude = value;
                break;
            case("southLongitude"):
                this.southLongitude = value;
                break;
            default:
                logger.error("Something wrong parsing Geographic bounding box. Title is %s", title);
                System.out.println("Something went wrong with Geographic Boundary parsing");
        }
    }
}
