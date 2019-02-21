package Dataverse.DataverseJSONFieldClasses.Fields.CompoundField;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

public class Software extends CompoundJSONField {
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
                errorParsing(this.getClass().getName(),title);
        }
    }

    @Override
    protected String getSpecifiedField(String title) {
        switch (title) {
            case("softwareName"):
                return getSoftwareName();
            case("softwareVersion"):
                return getSoftwareVersion();
            default:
                errorGettingValue(this.getClass().getName(),title);
                return "Bad field name";
        }
    }
}
