package Dataverse.DataverseJSONFieldClasses.Fields.SimpleJSONFields;

import Dataverse.DataverseJSONFieldClasses.JSONField;


import static Dataverse.DataverseJSONFieldClasses.DVFieldNames.*;

public class SimpleFields extends JSONField {
    /**
     *  * Title : String: title
     *  * Subtitle : String: subtitle
     *  * Alternative Title : String : alternativeTitle
     *  * Alternative URL: String : alternativeURL
     *  * License : String : license
     *  * Publisher : String : publisher
     *  * Publication Date : String : Publication Date
     *  * OtherLocation ID : OtherID : otherID
     */
    private String title, subtitle, alternativeTitle, alternativeURL, license,notesText,productionPlace,depositor, accessToSources, publisher,originOfSources, characteristicOfSources;
    private Date productionDate,distributionDate,dateOfDeposit, publicationDate;
    private int versionMajor, versionMinor;

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
        this.versionMajor = 0;
        this.versionMinor = 0;
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
            case TITLE:
                setTitle(value);
                break;
            case SUBTITLE:
                setSubtitle(value);
                break;
            case ALT_TITLE:
                setAlternativeTitle(value);
                break;
            case ALT_URL:
                setAlternativeURL(value);
                break;
            case LICENSE:
                setLicense(value);
                break;
            case NOTES_TEXT:
                setNotesText(value);
                break;
            case PROD_DATE:
                setProductionDate(value);
                break;
            case PROD_PLACE:
                setProductionPlace(value);
                break;
            case DIST_DATE:
                setDistributionDate(value);
                break;
            case DEPOSITOR:
                setDepositor(value);
                break;
            case DEPOS_DATE:
                setDateOfDeposit(value);
                break;
            case ORIG_OF_SOURCES:
                setOriginOfSources(value);
                break;
            case CHAR_OF_SOURCES:
                setCharacteristicOfSources(value);
                break;
            case ACCESS_TO_SOURCES:
                setAccessToSources(value);
                break;
            case PUB_DATE:
                setPublicationDate(value);
                break;
            case PUBLISHER:
                setPublisher(value);
                break;
            case MAJOR_VERSION:
                setVersionMajor(Integer.parseInt(value));
                break;
            case MINOR_VERSION:
                setVersionMinor(Integer.parseInt(value));
                break;
            default:
                errorParsing(this.getClass().getName(),label);
        }
    }


    @Override
    public String getField(String fieldName) {
        switch (fieldName) {
            case TITLE:
                return getTitle();
            case SUBTITLE:
                return getSubtitle();
            case ALT_TITLE:
                return getAlternativeTitle();
            case ALT_URL:
                return getAlternativeURL();
            case LICENSE:
                return getLicense();
            case NOTES_TEXT:
                return getNotesText();
            case PROD_DATE:
                return getProductionDate();
            case PROD_PLACE:
                return getProductionPlace();
            case DIST_DATE:
                return getDistributionDate();
            case DEPOSITOR:
                return getDepositor();
            case DEPOS_DATE:
                return getDateOfDeposit();
            case ORIG_OF_SOURCES:
                return getOriginOfSources();
            case CHAR_OF_SOURCES:
                return getCharacteristicOfSources();
            case ACCESS_TO_SOURCES:
                return getAccessToSources();
            case PUB_DATE:
                return getPublicationDate();
            case PUBLISHER:
                return getPublisher();
            case MAJOR_VERSION:
                return getVersionMajor();
            case MINOR_VERSION:
                return getVersionMinor();
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

    public void setVersionMajor(int versionMajor) {
        this.versionMajor = versionMajor;
    }

    public void setVersionMinor(int versionMinor) {
        this.versionMinor = versionMinor;
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

    public String getDOI(){
        return getAlternativeTitle();
    }

    public String getVersionMajor() {
        return String.valueOf(versionMajor);
    }

    public String getVersionMinor() {
        return String.valueOf(versionMinor);
    }
}
