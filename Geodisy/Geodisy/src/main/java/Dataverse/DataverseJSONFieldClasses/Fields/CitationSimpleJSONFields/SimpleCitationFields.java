package Dataverse.DataverseJSONFieldClasses.Fields.CitationSimpleJSONFields;

import Dataverse.DataverseJSONFieldClasses.JSONField;
import static _Strings.DVFieldNameStrings.*;

public class SimpleCitationFields extends JSONField {
    /**
     *  * Title : String: title
     *  * Subtitle : String: subtitle
     *  * Alternative Title : String : alternativeTitle
     *  * Alternative URL: String : alternativeURL
     *  * License : String : license
     *  * Publisher : String : publisher
     *  * Publication Date : String : Publication Date
     *  * OtherLocation ID : OtherID : otherID
     *  * Authority : String : authority   (organization number in PERSISTENT_ID)
     *  * Identifier : String : identifier (record identifier in PERSISTENT_ID)
     *
     *  ___________________________________________________________
     *  Terms of Use and Access Fields
     *  * Terms Of Use : String : termsOfUse
     *  * Confidentiality Declaration : String : confidDec
     *  * Availability Status : String : availabStat
     *  * Special Permissions : String : specialPerms
     *  * Restrictions : String : restrictions
     *  * Citation Requirements : String : citationRequs
     *  * Depositor Requirements : String : depositReqs
     *  * Conditions : String : conditions
     *  * Disclaimer : String : disclaimer
     *  * Terms of Access : String : termsOfAcc
     *  * Data Access Place : String : dataAccPlace
     *  * Original Archive : String : origlArch
     *  * Contact for Access : String : contactForAcc
     *  * Size of Collection : String : sizeOfColl
     *  * Study Completion : String : studyComp
     */
    private String title, subtitle, alternativeTitle, alternativeURL, license,notesText,productionPlace,depositor, accessToSources, publisher,originOfSources, characteristicOfSources, pID, authority, identifier, termsOfUse, confidDec, availabStat, specialPerms, restrictions, citationReqs, depositReqs, conditions, disclaimer, termsOfAcc, dataAccPlace, origArch, contactForAcc, sizeOfColl, studyComp,protocol, pURL;
    private Date productionDate,distributionDate,dateOfDeposit, publicationDate, lastModDate;
    private int versionMajor, versionMinor;

    public SimpleCitationFields() {
        this.pID = "";
        this.pURL = "";
        this.authority = "";
        this.identifier = "";
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
        this.termsOfUse = "";
        this.confidDec = "";
        this.availabStat = "";
        this.specialPerms = "";
        this.restrictions = "";
        this.citationReqs = "";
        this.depositReqs = "";
        this.conditions = "";
        this.disclaimer = "";
        this.termsOfAcc = "";
        this.dataAccPlace = "";
        this.origArch = "";
        this.contactForAcc = "";
        this.sizeOfColl = "";
        this.studyComp = "";
        this.protocol = "";
    }

    public boolean hasField(String fieldName){
        if(fieldName.equals(MAJOR_VERSION)||fieldName.equals(MINOR_VERSION))
            return !getField(MAJOR_VERSION).equals("0");
        return !getField(fieldName).equals("");

    }
    /**
     *
     * @param label Field name label
     * @param value Value to put into that field
     *
     * This method is for using the SimpleCitationFields class as the Class to store the simple fields values rather than have them be individual fields in the larger DataverseJavaObject class.
     */
    public void setField(String label, String value){
        switch(label) {
            case RECORD_URL:
                setPersistentID(value);
                break;
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
            case IDENTIFIER:
                setIdentifier(value);
                break;
            case AUTHORITY:
                setAuthority(value);
                break;
            case PROTOCOL:
                setProtocol(value);
                break;
            case LAST_MOD_DATE:
                setLastModDate(value);
                break;
            default:
                setTermsAndAccField(label,value);
        }
    }

    private void setTermsAndAccField(String label, String value) {
        switch(label) {
            case TERMS_OF_USE:
                setTermsOfUse(value);
                break;
            case CONFID_DEC:
                setConfidDec(value);
                break;
            case AVALIB_STATUS:
                setAvailabStat(value);
                break;
            case SPECIAL_PERMS:
                setSpecialPerms(value);
                break;
            case RESTRICTIONS:
                setRestrictions(value);
                break;
            case CITATION_REQUIREMENTS:
                setCitationReqs(value);
                break;
            case DEPOSIT_REQUIREMENTS:
                setDepositReqs(value);
                break;
            case CONDITIONS:
                setConditions(value);
                break;
            case DISCLAIMER:
                setDisclaimer(value);
                break;
            case TERMS_OF_ACCESS:
                setTermsOfAcc(value);
                break;
            case DATA_ACC_PLACE:
                setDataAccPlace(value);
                break;
            case ORIG_ARCHIVE:
                setOrigArch(value);
                break;
            case CONTACT_FOR_ACCESS:
                setContactForAcc(value);
                break;
            case SIZE_OF_COLLECTION:
                setSizeOfColl(value);
                break;
            case STUDY_COMPLETION:
                setStudyComp(value);
                break;
            case PERSISTENT_ID:
                setPersistentID(value);
                break;
            default:
                errorParsing(this.getClass().getName(),label);
    }
    }


    @Override
    public String getField(String fieldName) {
        switch (fieldName) {
            case PERSISTENT_ID:
                return getPersistentID();
            case RECORD_URL:
                return getPersistentURL();
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
            case AUTHORITY:
                return authority;
            case IDENTIFIER:
                return identifier;
            case PROTOCOL:
                return protocol;
            case LAST_MOD_DATE:
                return getLastModDate();
            default:
                return getTermsAndAccessField(fieldName);
        }

    }
    private String getTermsAndAccessField(String fieldName) {
        switch (fieldName) {
            case TERMS_OF_USE:
                return termsOfUse;
            case CONFID_DEC:
                return confidDec;
            case AVALIB_STATUS:
                return availabStat;
            case SPECIAL_PERMS:
                return specialPerms;
            case RESTRICTIONS:
                return restrictions;
            case CITATION_REQUIREMENTS:
                return citationReqs;
            case DEPOSIT_REQUIREMENTS:
                return depositReqs;
            case CONDITIONS:
                return conditions;
            case DISCLAIMER:
                return disclaimer;
            case TERMS_OF_ACCESS:
                return termsOfAcc;
            case DATA_ACC_PLACE:
                return dataAccPlace;
            case ORIG_ARCHIVE:
                return origArch;
            case CONTACT_FOR_ACCESS:
                return contactForAcc;
            case SIZE_OF_COLLECTION:
                return sizeOfColl;
            case STUDY_COMPLETION:
                return studyComp;
            default:
            errorParsing(this.getClass().getName(), fieldName);
            return "Bad Field Name";
        }
    }
    public int getVersion(){
        int major = Integer.parseInt(getVersionMajor());
        int minor = Integer.parseInt(getVersionMinor());
        return major*1000+minor;
    }

    private void setPersistentID(String persistentURL) {
        pURL = persistentURL;
        String filteredDOI = filterURL(persistentURL);
        if(filteredDOI.isEmpty()) {
            this.pID = filteredDOI;
            logger.error("Something went wrong as the PERSISTENT_ID us wonky: " + persistentURL);
        }else {
            this.pID = filteredDOI.substring(filteredDOI.indexOf(getField(AUTHORITY)));
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
        alternativeURL = filterURL(alternativeURL);
        this.alternativeURL = alternativeURL;

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

    private void setLastModDate(String lastModDate) {this.lastModDate = new Date(lastModDate);}

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
        if(productionDate==null)
            return "";
        return productionDate.getDateAsString();
    }

    private String getDistributionDate() {
        if(distributionDate==null)
            return "";
        return distributionDate.getDateAsString();
    }

    private String getDateOfDeposit() {
        if(dateOfDeposit==null)
            return "";
        return dateOfDeposit.getDateAsString();
    }

    private String getPublicationDate() {
        if(publicationDate==null)
            return "";
        return publicationDate.getDateAsString();
    }

    private String getLastModDate() {
        if(lastModDate==null)
            return "";
        return lastModDate.getDateAsString();
    }

    public String getPersistentID(){
        return pID;
    }

    public String getPersistentURL(){
        return pURL;
    }

    private String getVersionMajor() {
        return String.valueOf(versionMajor);
    }

    private String getVersionMinor() {
        return String.valueOf(versionMinor);
    }

    private void setIdentifier(String value) {
        identifier = value;
    }

    private void setAuthority(String value){
        authority = value;
    }


    private void setTermsOfUse(String termsOfUse) {
        this.termsOfUse = termsOfUse;
    }


    private void setConfidDec(String confidDec) {
        this.confidDec = confidDec;
    }


    private void setAvailabStat(String availabStat) {
        this.availabStat = availabStat;
    }


    private void setSpecialPerms(String specialPerms) {
        this.specialPerms = specialPerms;
    }


    private void setRestrictions(String restrictions) {
        this.restrictions = restrictions;
    }


    private void setCitationReqs(String citationReqs) {
        this.citationReqs = citationReqs;
    }


    private void setDepositReqs(String depositReqs) {
        this.depositReqs = depositReqs;
    }


    private void setConditions(String conditions) {
        this.conditions = conditions;
    }


    private void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }


    private void setTermsOfAcc(String termsOfAcc) {
        this.termsOfAcc = termsOfAcc;
    }


    private void setDataAccPlace(String dataAccPlace) {
        this.dataAccPlace = dataAccPlace;
    }


    private void setOrigArch(String origArch) {
        this.origArch = origArch;
    }


    private void setContactForAcc(String contactForAcc) {
        this.contactForAcc = contactForAcc;
    }


    private void setSizeOfColl(String sizeOfColl) {
        this.sizeOfColl = sizeOfColl;
    }

    private void setStudyComp(String studyComp) {
        this.studyComp = studyComp;
    }

    private String getProtocol() {
        return protocol;
    }

    private void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
