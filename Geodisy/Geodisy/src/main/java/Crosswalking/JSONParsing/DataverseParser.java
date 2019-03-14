package Crosswalking.JSONParsing;

import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicFields;
import Dataverse.DataverseJavaObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import static Dataverse.DataverseJSONFieldClasses.DVFieldNames.*;


public class DataverseParser {

    private JSONObject dataverseJSON;
    private DataverseJavaObject dJO;
    static Logger logger = LogManager.getLogger(DataverseParser.class);
    //TODO compartimentalize Citation metadata, Geographic Metadata, etc
    public DataverseParser(String dataverseJSONString) {
        this.dataverseJSON = new JSONObject(dataverseJSONString);
        this.dJO = new DataverseJavaObject();
        parse();
    }

    /**
     * The appropriate JSONField class is created and its parseCompoundData() method or setField() method is called
     * to parse compound or simple field data, respectively.
     * @throws JSONException
     */
    private void parse() throws JSONException {
        JSONObject current = (JSONObject) dataverseJSON.get("data");
        dJO.parseCitationFields(current);
        JSONObject metadata = current.getJSONObject("latestVersion").getJSONObject("metadataBlocks");
        if(metadata.has(GEOSPATIAL))
            dJO.parseGeospatialFields(metadata.getJSONObject(GEOSPATIAL).getJSONArray(FIELD));
        String prodPlace = dJO.getProductionPlace();
        if(!prodPlace.matches("")&&!dJO.hasBoundingBox()){
            GeographicFields gf = dJO.getGeographicFields();
            gf.addGeoBBox(getBBFromProdPlace(prodPlace));
        }

    }

    //TODO get bounding box from production name using geonames, if possible
    private JSONObject getBBFromProdPlace(String prodPlace) {
        return null;
    }



    public DataverseJavaObject getdJO() {
        return dJO;
    }

    public void setdJO(DataverseJavaObject dJO) {
        this.dJO = dJO;
    }
}
