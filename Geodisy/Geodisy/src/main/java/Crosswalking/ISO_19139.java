/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crosswalking;

import Crosswalking.GeoBlacklightJson.DataGBJSON;
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
public class ISO_19139 extends ISO_Schema {
    public XMLDocObject generateXML(SourceJavaObject sJO) {
            DataverseJavaObject djo = (DataverseJavaObject) sJO;
            ISOXMLGen xmlGenerator = new ISOXMLGen(djo);
            XMLDocObject xmlDocObject = xmlGenerator.generateXMLFile();
            saveXMLToFile(xmlDocObject.getDoc(),xmlDocObject.getDoi());
            return xmlDocObject;

    }

 }
