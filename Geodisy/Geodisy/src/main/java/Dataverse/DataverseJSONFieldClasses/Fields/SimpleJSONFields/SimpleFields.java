package Dataverse.DataverseJSONFieldClasses.Fields.SimpleJSONFields;

import Dataverse.DataverseJSONFieldClasses.JSONField;
import Dataverse.DataverseJavaObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class SimpleFields extends JSONField {
    /**
     *
     * @param dJO the DataverseJavaObject passed into the class
     * @param value the String value of the value field of the current JSONObject being looked at
     * @param label the String value of the typeName field of the current JSONObject
     * @return
     *
     * This class is for dealing with simple fields in the DataverseJavaObject (i.e. String and List\<String\> fields)
     *  that are at the same level of the heirarchy as the compound fields
     */
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

    /**
     *
     * @param current
     * @param dJO
     * @return
     *
     * This method fills in the simple fields that are in the parts of the JSON hierarchy that are higher up than the compound fields
     */
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

    /**
     *
     * @param ja JSONArray
     * @return the list of Strings for that field
     *
     * This method gets all the Strings in the list for a given JSONArray of Strings and returns as a list
     */
    public List<String> getList(JSONArray ja){
        List<String> answer = new LinkedList<>();
        String s;
        for(Object o: ja){
            s = (String) o;
            answer.add(s);
        }
        return answer;
    }
}
