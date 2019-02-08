package Crosswalking.JSONParsing;

import Dataverse.DataverseJavaObject;
import org.json.JSONException;
import org.json.JSONObject;

public class DataverseParser {

    private JSONObject dataverseJSON;
    private DataverseJavaObject dJO;

    public DataverseParser(String dataverseJSONString) {
        this.dataverseJSON = new JSONObject(dataverseJSONString);
        this.dJO = new DataverseJavaObject();
        parse();
    }

    private void parse() throws JSONException {
        JSONObject current = (JSONObject) dataverseJSON.get("data");
        dJO.setAlternativeURL(dataverseJSON.getString("persistentURL"));
    }
}
