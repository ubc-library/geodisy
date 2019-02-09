package Dataverse.DataverseJSONFieldClasses;

import org.json.JSONArray;
import org.json.JSONObject;

public class Software extends JSONField{
    private String softwareName, softwareVersion;

    public Software() {
        this.softwareName = "";
        this.softwareVersion = "";
    }

    public String getSoftwareName() {
        return softwareName;
    }

    public void setSoftwareName(String softwareName) {
        this.softwareName = softwareName;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }


    @Override
    public void setField(JSONObject field) {
        String title = field.getString("typeName");
        String value = field.getString("value");
        switch (title) {
            case("softwareName"):
                this.softwareName = value;
                break;
            case("softwareVersion"):
                this.softwareVersion = value;
                break;
            default:
                System.out.println("Something went wrong with Software parsing");
        }
    }
}
