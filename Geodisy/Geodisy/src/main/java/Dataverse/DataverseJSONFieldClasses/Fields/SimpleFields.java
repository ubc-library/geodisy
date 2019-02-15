package Dataverse.DataverseJSONFieldClasses.Fields;

import Dataverse.DataverseJSONFieldClasses.JSONField;
import Dataverse.DataverseJavaObject;
import org.json.JSONObject;

public class SimpleFields extends JSONField {

    public DataverseJavaObject setField(DataverseJavaObject dJO, String value, String label) {
        switch(label) {
            case("title"):
                dJO.setTitle(value);
                break;
            case("subtitle"):
                dJO.setSubtitle(value);
                break;
            case("alternativeTitle"):
                dJO.setAlternativeTitle(value);
                break;
            case("alternativeURL"):
                dJO.setAlternativeURL(filterURL(value));
                break;
            case("license"):
                dJO.setLicense(value);
                break;
            case("notesText"):
                dJO.setNotesText(value);
                break;
            case("productionDate"):
                dJO.setProductionDate(filterForDate(value));
                break;
            case("productionPlace"):
                dJO.setProductionPlace(value);
                break;
            case("distributionDate"):
                dJO.setDistributionDate(filterForDate(value));
                break;
            case("depositor"):
                dJO.setDepositor(value);
                break;
            case("dateOfDeposit"):
                dJO.setDataOfDeposit(filterForDate(value));
                break;
            case("originOfSources"):
                dJO.setOriginOfSources(value);
                break;
            case("characteristicOfSources"):
                dJO.setCharacteristicOfSources(value);
                break;
            case("accessToSources"):
                dJO.setAccessToSources(value);
                break;
            default:
                logger.error("Something has gone wrong with parsing. Label found is : %s", label);
                System.out.println("Something has gone wrong with parsing. Label found is : " + label);
        }
        return dJO;
    }

    public DataverseJavaObject setBaseFields(JSONObject current, DataverseJavaObject dJO){

        dJO.setAlternativeURL(getValue(current,"persistentUrl"));
        dJO.setPublishDate(getValueDate(current,"publicationDate"));
        dJO.setPublisher(getValue(current,"publisher"));
        current = current.getJSONObject("latestVersion");
        dJO.setProductionDate(getValueDate(current,"productionDate"));
        dJO.setDataOfDeposit(getValueDate(current,"createTime"));
        dJO.setDistributionDate(getValueDate(current,"releaseTime"));
        dJO.setLicense(getValue(current,"license"));

        return dJO;
    }
}
