package Crosswalking.JSONParsing;

import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicFields;
import Dataverse.DataverseJavaObject;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import static Dataverse.DataverseJSONFieldClasses.DVFieldNames.*;


public class DataverseParser {

    private JSONObject dataverseJSON;
    private DataverseJavaObject dJO;
    static Logger logger = LogManager.getLogger(DataverseParser.class);

    public DataverseParser() {
        this.dJO = new DataverseJavaObject();
    }

    /**
     * The appropriate JSONField class is created and its parseCompoundData() method or setField() method is called
     * to parse compound or simple field data, respectively.
     * @throws JSONException
     */
    public DataverseJavaObject parse(JSONObject jo)  {
        try {
            dataverseJSON = jo;
            JSONObject current;
            if(dataverseJSON.has("data"))
                current = (JSONObject) dataverseJSON.get("data");
            else
                current = dataverseJSON;
            dJO.parseCitationFields(current);
            JSONObject metadata;
            metadata = dJO.getVersionSection(current).getJSONObject("metadataBlocks");
            if (metadata.has(GEOSPATIAL))
                dJO.parseGeospatialFields(metadata.getJSONObject(GEOSPATIAL).getJSONArray(FIELDS));
            else
                dJO.setGeoFields(new GeographicFields());
            String prodPlace = dJO.getProductionPlace();
            if (!prodPlace.matches("") && !dJO.hasBoundingBox()) {
                GeographicFields gf = dJO.getGeographicFields();
                gf.setFullBB(getBBFromProdPlace(prodPlace));
            }
            if(current.has("files"))
                dJO.parseFiles(current.getJSONArray("files"));
        }catch (JSONException e){
            logger.error("Something was malformed with the JSON string returned from Dataverse");
        }
        return dJO;
    }

    // We decided that we aren't trying to get a bounding box from Production Place but will instead log it to
    // the manual check log
    private BoundingBox getBBFromProdPlace(String prodPlace) {
        BoundingBox b = new BoundingBox();
        logger.info("The following record has no geographic location info other than a Production Place. Please manually check: %s", dJO.getDOI());
        return b;
    }



    public DataverseJavaObject getdJO() {
        return dJO;
    }

    public void setdJO(DataverseJavaObject dJO) {
        this.dJO = dJO;
    }
}
