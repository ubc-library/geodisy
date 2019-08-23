/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crosswalking;

import Crosswalking.XML.XMLTools.XMLDocObject;
import Crosswalking.XML.XMLTools.XMLGenerator;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields.CitationFields;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONAstroFieldClasses.AstroFields;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONJournalFieldClasses.JournalFields;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONLifeFieldClasses.LifeFields;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONSocialFieldClasses.SocialFields;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicFields;
import Dataverse.DataverseJavaObject;
import Dataverse.DataverseRecordFile;
import Dataverse.SourceJavaObject;
import org.jdom2.Element;

import java.util.LinkedList;
import java.util.List;

/**
 * Output schema that will be then sent to Geoserver to be indexed for GeoBlacklight. Values for this will be
 * crosswalked in for the data repository Java Objects.
 * @author pdante
 */
public class ISO_19115 extends ISO_Schema {
    //TODO Create ISO_19115 schema
    @Override
    public LinkedList<XMLDocObject> generateXML(List<SourceJavaObject> sJOs) {
        LinkedList<XMLDocObject> documents = new LinkedList<>();
        for(SourceJavaObject sjo : sJOs) {
            DataverseJavaObject djo = (DataverseJavaObject) sjo;
            XMLGenerator xmlGenerator = new XMLGenerator(djo);
            documents.add(xmlGenerator.generateXMLFile());
        }
        return documents;

}
//TODO
    @Override
    public void crosswalk(SourceJavaObject sJO) {

    }

    //TODO Flesh this out
    private Element getFileFields(List<DataverseRecordFile> dataFiles) {
        Element fileFields = new Element("placeholder for file elements");


        return fileFields;
    }
    //TODO Flesh this out
    private Element getGeoFields(GeographicFields geoFields) {
        Element geo = new Element("placeholder for geo elements");


        return geo;
    }
    //TODO Flesh this out
    private Element getCitation(CitationFields citationFields) {
        Element citation = new Element("placeholder for citation elements");


        return citation;
    }
    //TODO Flesh this out
    private Element getCitation(SocialFields socialFields) {
        Element citation = new Element("placeholder for citation elements");


        return citation;
    }

    //TODO Flesh this out
    private Element getCitation(AstroFields astroFields) {
        Element citation = new Element("placeholder for citation elements");


        return citation;
    }
    //TODO Flesh this out
    private Element getCitation(LifeFields lifeFields) {
        Element citation = new Element("placeholder for citation elements");


        return citation;
    }
    //TODO Flesh this out
    private Element getCitation(JournalFields journalFields) {
        Element citation = new Element("placeholder for citation elements");


        return citation;
    }
 }
