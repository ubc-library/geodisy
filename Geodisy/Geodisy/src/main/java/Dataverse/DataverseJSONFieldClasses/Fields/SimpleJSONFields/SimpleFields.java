package Dataverse.DataverseJSONFieldClasses.Fields.SimpleJSONFields;

import Dataverse.DataverseJSONFieldClasses.JSONField;
import Dataverse.DataverseJavaObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class SimpleFields extends JSONField {
    String title, subtitle, alternativeTitle, alternativeURL, license,notesText,productionPlace,depositor,originOfSources,characteristicOfSources, accessToSources;
    Date productionDate,distributionDate,dateOfDeposit;

    public SimpleFields() {
        this.title = "";
        this.subtitle = "";
        this.alternativeTitle = "";
        this.alternativeURL = "";
        this.license = "";
        this.notesText = "";
        this.productionPlace = "";
        this.depositor = "";
        this.originOfSources = "";
        this.characteristicOfSources = "";
        this.accessToSources = "";
    }

    /**
     *
     * @param label Field name label
     * @param value Value to put into that field
     * @return this
     *
     * This method is for using the SimpleFields class as the Class to store the simple fields values rather than have them be individual fields in the larger DataverseJavaObject class.
     */
    public void setField(String label, String value){
        switch(label) {
            case("title"):
                setTitle(value);
                break;
            case("subtitle"):
                setSubtitle(value);
                break;
            case("alternativeTitle"):
                setAlternativeTitle(value);
                break;
            case("alternativeURL"):
                setAlternativeURL(value);
                break;
            case("license"):
                setLicense(value);
                break;
            case("notesText"):
                setNotesText(value);
                break;
            case("productionDate"):
                setProductionDate(value);
                break;
            case("productionPlace"):
                setProductionPlace(value);
                break;
            case("distributionDate"):
                setDistributionDate(value);
                break;
            case("depositor"):
                setDepositor(value);
                break;
            case("dateOfDeposit"):
                setDateOfDeposit(value);
                break;
            case("originOfSources"):
                setOriginOfSources(value);
                break;
            case("characteristicOfSources"):
                setCharacteristicOfSources(value);
                break;
            case("accessToSources"):
                setAccessToSources(value);
                break;
            default:
                errorParsing(this.getClass().getName(),label);
        }
    }

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
    /*
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
                dJO.setDateOfDeposit(filterForDate(value));
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
                errorParsing(this.getClass().getName(),label);
        }
        return dJO;
    }*/

    /**
     *
     * @param current
     * @param dJO
     * @return
     *
     * This method fills in the simple fields that are in the parts of the JSON hierarchy that are higher up than the compound fields
     */
    public DataverseJavaObject setBaseFields(JSONObject current, DataverseJavaObject dJO){

        dJO.setAlternativeURL(parseSimpleValue(current,"persistentUrl"));
        dJO.setPublishDate(getValueDate(current,"publicationDate"));
        dJO.setPublisher(parseSimpleValue(current,"publisher"));
        current = current.getJSONObject("latestVersion");
        dJO.setProductionDate(getValueDate(current,"productionDate"));
        dJO.setDateOfDeposit(getValueDate(current,"createTime"));
        dJO.setDistributionDate(getValueDate(current,"releaseTime"));
        dJO.setLicense(parseSimpleValue(current,"license"));

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
    //TODO fix this to work when simple fields are in this class
    @Override
    public String getField(String fieldName) {
        switch (fieldName) {
            case ("title"):
                return getTitle();
            case ("subtitle"):
                return getSubtitle();
            case ("alternativeTitle"):
                return getAlternativeTitle();
            case ("alternativeURL"):
                return getAlternativeURL();
            case ("license"):
                return getLicense();
            case ("notesText"):
                return getNotesText();
            case ("productionDate"):
                return getProductionDate();
            case ("productionPlace"):
                return getProductionPlace();
            case ("distributionDate"):
                return getDistributionDate();
            case ("depositor"):
                return getDepositor();
            case ("dateOfDeposit"):
                return getDateOfDeposit();
            case ("originOfSources"):
                return getOriginOfSources();
            case ("characteristicOfSources"):
                return getCharacteristicOfSources();
            case ("accessToSources"):
                return getAccessToSources();
            default:
                errorParsing(this.getClass().getName(), fieldName);
                return "Bad Field Name";
        }

    }
    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setAlternativeTitle(String alternativeTitle) {
        this.alternativeTitle = alternativeTitle;
    }

    public void setAlternativeURL(String alternativeURL) {
        this.alternativeURL = filterURL(alternativeURL);
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public void setNotesText(String notesText) {
        this.notesText = notesText;
    }

    public void setProductionPlace(String productionPlace) {
        this.productionPlace = productionPlace;
    }

    public void setDepositor(String depositor) {
        this.depositor = depositor;
    }

    public void setOriginOfSources(String originOfSources) {
        this.originOfSources = originOfSources;
    }

    public void setCharacteristicOfSources(String characteristicOfSources) {
        this.characteristicOfSources = characteristicOfSources;
    }

    public void setAccessToSources(String accessToSources) {
        this.accessToSources = accessToSources;
    }

    public void setProductionDate(String productionDate) {
        this.productionDate = new Date(productionDate);
    }

    public void setDistributionDate(String distributionDate) {
        this.distributionDate = new Date(distributionDate);
    }

    public void setDateOfDeposit(String dateOfDeposit) {
        this.dateOfDeposit = new Date(dateOfDeposit);
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getAlternativeTitle() {
        return alternativeTitle;
    }

    public String getAlternativeURL() {
        return alternativeURL;
    }

    public String getLicense() {
        return license;
    }

    public String getNotesText() {
        return notesText;
    }

    public String getProductionPlace() {
        return productionPlace;
    }

    public String getDepositor() {
        return depositor;
    }

    public String getOriginOfSources() {
        return originOfSources;
    }

    public String getCharacteristicOfSources() {
        return characteristicOfSources;
    }

    public String getAccessToSources() {
        return accessToSources;
    }

    public String getProductionDate() {
        return productionDate.getDateAsString();
    }

    public String getDistributionDate() {
        return distributionDate.getDateAsString();
    }

    public String getDateOfDeposit() {
        return dateOfDeposit.getDateAsString();
    }
}
