/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crosswalking;

import Crosswalking.XML.XMLDocument;
import Dataverse.DataverseJavaObject;
import Dataverse.SourceJavaObject;
import org.w3c.dom.Document;

import java.util.LinkedList;
import java.util.List;

/**
 * An interface used for crosswalking from a datasource Java Object to
 * a metadata schema.
 * @author pdante
 */
public interface CrosswalkInterface {

    /**
     * Create ISO XML for the new/updated records
     * @param records
     */
    void convertSJOs(List<SourceJavaObject> records);

    /**
     * Send all the new ISO XML files to Geocombine
     * @param docs
     */
    void sendXMLToGit(List<XMLDocument> docs);
}