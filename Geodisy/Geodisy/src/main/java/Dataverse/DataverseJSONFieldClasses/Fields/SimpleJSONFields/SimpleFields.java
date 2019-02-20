package Dataverse.DataverseJSONFieldClasses.Fields.SimpleJSONFields;

import Dataverse.DataverseJSONFieldClasses.JSONField;

import org.json.JSONArray;


import java.util.LinkedList;
import java.util.List;

public class SimpleFields extends JSONField {
    private String title, subtitle, alternativeTitle, alternativeURL, license,notesText,productionPlace,depositor, accessToSources, publisher,originOfSources, characteristicOfSources;

    private Date productionDate,distributionDate,dateOfDeposit, publicationDate;

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
        this.publisher = "";
    }

    /**
     *
     * @param label Field name label
     * @param value Value to put into that field
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
            case("publicationDate"):
                setPublicationDate(value);
                break;
            case("publisher"):
                setPublisher(value);
                break;
            default:
                errorParsing(this.getClass().getName(),label);
        }
    }


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
            case("publicationDate"):
                return getPublicationDate();
            case("publisher"):
                return getPublisher();
            default:
                errorParsing(this.getClass().getName(), fieldName);
                return "Bad Field Name";
        }

    }
    private void setTitle(String title) {
        this.title = title;
    }

    private void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    private void setAlternativeTitle(String alternativeTitle) {
        this.alternativeTitle = alternativeTitle;
    }

    private void setAlternativeURL(String alternativeURL) {
        this.alternativeURL = filterURL(alternativeURL);
    }

    private void setLicense(String license) {
        this.license = license;
    }

    private void setNotesText(String notesText) {
        this.notesText = notesText;
    }

    private void setProductionPlace(String productionPlace) {
        this.productionPlace = productionPlace;
    }

    private void setDepositor(String depositor) {
        this.depositor = depositor;
    }

    private void setOriginOfSources(String originOfSources) {
        this.originOfSources = originOfSources;
    }

    private void setCharacteristicOfSources(String characteristicOfSources) {
        this.characteristicOfSources = characteristicOfSources;
    }

    private void setAccessToSources(String accessToSources) {
        this.accessToSources = accessToSources;
    }

    private void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    private void setProductionDate(String productionDate) {
        this.productionDate = new Date(productionDate);
    }

    private void setDistributionDate(String distributionDate) {
        this.distributionDate = new Date(distributionDate);
    }

    private void setDateOfDeposit(String dateOfDeposit) {
        this.dateOfDeposit = new Date(dateOfDeposit);
    }

    private void setPublicationDate(String privateationDate) {
        this.publicationDate = new Date(privateationDate);
    }

    private String getTitle() {
        return title;
    }

    private String getSubtitle() {
        return subtitle;
    }

    private String getAlternativeTitle() {
        return alternativeTitle;
    }

    private String getAlternativeURL() {
        return alternativeURL;
    }

    private String getLicense() {
        return license;
    }

    private String getNotesText() {
        return notesText;
    }

    private String getProductionPlace() {
        return productionPlace;
    }

    private String getDepositor() {
        return depositor;
    }

    private String getOriginOfSources() {
        return originOfSources;
    }

    private String getCharacteristicOfSources() {
        return characteristicOfSources;
    }

    private String getAccessToSources() {
        return accessToSources;
    }

    private String getPublisher() {
        return publisher;
    }

    private String getProductionDate() {
        return productionDate.getDateAsString();
    }

    private String getDistributionDate() {
        return distributionDate.getDateAsString();
    }

    private String getDateOfDeposit() {
        return dateOfDeposit.getDateAsString();
    }

    private String getPublicationDate() {
        return publicationDate.getDateAsString();
    }


}
