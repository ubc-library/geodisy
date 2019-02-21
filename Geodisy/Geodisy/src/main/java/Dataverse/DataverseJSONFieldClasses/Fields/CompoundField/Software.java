package Dataverse.DataverseJSONFieldClasses.Fields.CompoundField;

import Dataverse.DataverseJSONFieldClasses.CompoundJSONField;
import org.json.JSONObject;

import static Dataverse.DataverseJSONFieldClasses.DVFieldNames.*;

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
        String title = field.getString(TYPE_NAME);
        String value = field.getString(VAL);
        switch (title) {
            case SOFTWARE_NAME:
                this.softwareName = value;
                break;
            case SOFTWARE_VERSION:
                this.softwareVersion = value;
                break;
            default:
                errorParsing(this.getClass().getName(),title);
        }
    }

    @Override
    protected String getSpecifiedField(String title) {
        switch (title) {
            case SOFTWARE_NAME:
                return getSoftwareName();
            case SOFTWARE_VERSION:
                return getSoftwareVersion();
            default:
                errorGettingValue(this.getClass().getName(),title);
                return "Bad field name";
        }
    }
}
