/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crosswalking;

import Crosswalking.XML.XMLTools.XMLDocObject;
import Crosswalking.XML.XMLTools.ISOXMLGen;
import Dataverse.DataverseJavaObject;
import Dataverse.SourceJavaObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Output schema that will be then sent to Geoserver to be indexed for GeoBlacklight. Values for this will be
 * crosswalked in for the data repository Java Objects.
 * @author pdante
 */
public class ISO_19115 extends ISO_Schema {
    @Override
    public LinkedList<XMLDocObject> generateXML(List<SourceJavaObject> sJOs) {
        LinkedList<XMLDocObject> documents = new LinkedList<>();
        for(SourceJavaObject sjo : sJOs) {
            DataverseJavaObject djo = (DataverseJavaObject) sjo;
            ISOXMLGen xmlGenerator = new ISOXMLGen(djo);
            documents.add(xmlGenerator.generateXMLFile());
        }
        return documents;

    }

 }
