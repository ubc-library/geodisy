/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crosswalking;

import Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields.CitationFields;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONAstroFieldClasses.AstroFields;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONJournalFieldClasses.JournalFields;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONLifeFieldClasses.LifeFields;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONSocialFieldClasses.SocialFields;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicFields;
import Dataverse.DataverseRecordFile;
import Dataverse.SourceJavaObject;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Output schema that will be then sent to Geoserver to be indexed for GeoBlacklight. Values for this will be
 * crosswalked in for the data repository Java Objects.
 * @author pdante
 */
public class ISO_19115 extends ISO_Schema {
    //TODO Create ISO_19115 schema
    @Override
    public void generateXML(SourceJavaObject sJO) {
        String fileName = "need to grab this from the sJO";
        try {
            Element root = new Element("temp val");
            Document doc = new Document();
            doc.setRootElement(root);
            doc.getRootElement().addContent(getCitation(sJO.getCitationFields()));
            doc.getRootElement().addContent(getGeoFields(sJO.getGeoFields()));
            doc.getRootElement().addContent(getFileFields(sJO.getDataFiles()));

            // new XMLOutputter().output(doc, System.out);
            XMLOutputter xmlOutput = new XMLOutputter();

            // display nice nice
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(doc, new FileWriter(fileName));
        }catch (IOException io) {
            System.out.println(io.getMessage());
            logger.error("Something went wrong trying to create XML file: " + fileName);
    }



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
