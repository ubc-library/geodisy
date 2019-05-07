package Crosswalking.JSONParsing;

import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicFields;
import Dataverse.DataverseJavaObject;
import Dataverse.DataverseRecordInfo;
import Dataverse.ExistingSearches;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static Dataverse.DVFieldNameStrings.*;


public class DataverseParser implements JSONParser {

    static Logger logger = LogManager.getLogger(DataverseParser.class);


    /**
     * The appropriate JSONField class is created and its parseCompoundData() method or setField() method is called
     * to parse compound or simple field data, respectively.
     * @throws JSONException
     */
    public DataverseJavaObject parse(JSONObject jo, String server)  {
        DataverseJavaObject dJO = new DataverseJavaObject(server);
        JSONObject dataverseJSON;
        try {
            dataverseJSON = jo;
            JSONObject current;
            if(dataverseJSON.has("data"))
                current = (JSONObject) dataverseJSON.get("data");
            else
                current = dataverseJSON;
            dJO.parseCitationFields(current);
            ExistingSearches es = ExistingSearches.getExistingSearches();
            DataverseRecordInfo dRI = dJO.generateDRI();
            if(!dRI.younger(es.getRecordInfo(dRI.getDoi())))
                return new DataverseJavaObject("");
            JSONObject metadata;
            metadata = dJO.getVersionSection(current).getJSONObject("metadataBlocks");
            if (metadata.has(GEOSPATIAL))
                dJO.parseGeospatialFields(metadata.getJSONObject(GEOSPATIAL).getJSONArray(FIELDS));
            else
                dJO.setGeoFields(new GeographicFields(dJO.getDOI()));
            String prodPlace = dJO.getSimpleFieldVal(PROD_PLACE);
            if (!prodPlace.matches("") && !dJO.hasBoundingBox()) {
                GeographicFields gf = dJO.getGeoFields();
                gf.setFullBB(getBBFromProdPlace(prodPlace,dJO));
            }
            if(dJO.getVersionSection(current).has("files"))
                dJO.parseFiles((JSONArray) dJO.getVersionSection(current).get("files"));
        }catch (JSONException e){
            logger.error("Something was malformed with the JSON string returned from Dataverse");
        }
        return dJO;
    }

    // We decided that we aren't trying to get a bounding box from Production Place but will instead log it to
    // the manual check log
    private BoundingBox getBBFromProdPlace(String prodPlace, DataverseJavaObject dJO) {
        BoundingBox b = new BoundingBox();
        logger.info("The following record has no geographic location info other than a Production Place. Please manually check: %s", dJO.getDOI());
        return b;
    }


}
