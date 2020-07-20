package Crosswalking.XML.XMLGroups;

import BaseFiles.GeoLogger;
import Crosswalking.XML.XMLTools.SubElement;
import Crosswalking.XML.XMLTools.XMLDocObject;
import Crosswalking.XML.XMLTools.XMLStack;
import Dataverse.DataverseGeoRecordFile;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicBoundingBox;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicCoverage;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicFields;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicUnit;
import Dataverse.DataverseJavaObject;
import org.w3c.dom.Element;


import java.util.LinkedList;
import java.util.List;

import static _Strings.GeodisyStrings.CHARACTER;
import static _Strings.GeodisyStrings.DECIMAL;
import static _Strings.XMLStrings.*;
import static _Strings.DVFieldNameStrings.*;

public class GeographicInfo extends SubElement {
GeographicFields gf;
List<GeographicCoverage> geoCovers;
List<GeographicBoundingBox> geoBBs;
List<GeographicUnit> geoUnits;
GeoLogger logger;

    public GeographicInfo(DataverseJavaObject djo, XMLDocObject doc, Element root) {
        super(djo, doc, root);
        gf = djo.getGeoFields();
        geoCovers = gf.getGeoCovers();
        geoBBs = gf.getGeoBBoxes();
        geoUnits = gf.getGeoUnits();
        logger = new GeoLogger(this.getClass());
    }
    @Override
    public Element getFields() {

        for(GeographicCoverage gc: geoCovers){
            String commonCountry = gc.getField(COMMON_COUNTRY);
            String givenCountry = gc.getField(GIVEN_COUNTRY);
            String commonProvince = gc.getField(COMMON_PROVINCE);
            String givenProvince = gc.getField(GIVEN_PROVINCE);
            String commonCity = gc.getField(COMMON_CITY);
            String givenCity = gc.getField(GIVEN_CITY);
            String other = gc.getField(OTHER_GEO_COV);
            String name;
            //if the researcher uses an alternative name for the country, province, and/or city, then two geographic coverage units will be created from the single coverage unit
            boolean twoCountryOptions = !commonCountry.equals(givenCountry);
            boolean twoProvinceOptions = !commonProvince.equals(givenProvince);
            boolean twoCityOptions = !commonCity.equals(givenCity);
            int numberOfOptions = (twoCountryOptions || twoProvinceOptions || twoCityOptions) ? 2:1;
            for(int i = 0; i<numberOfOptions; i++) {
                stack.push(root); //J
                stack.push(doc.createGMDElement(EXTENT)); //K
                stack.push(doc.createGMDElement(EX_EXTENT)); //L
                stack.push(doc.createGMDElement(DESCRIP)); //M
                name = (twoCountryOptions && i==1) ? commonCountry : givenCountry;
                if (!givenProvince.isEmpty())
                    name = name + ", " + ((twoProvinceOptions && i==1) ? commonProvince : givenProvince);
                if (!givenCity.isEmpty())
                    name = name + ", " + ((twoCityOptions && i==1) ? commonCity : givenCity);
                if (!other.isEmpty())
                    name = name + ", " + other;
                root = stack.zip(doc.addGCOVal(name,CHARACTER));
            }
        }
        for(GeographicUnit gu: geoUnits){
            stack.push(root);
            stack.push(doc.createGMDElement(EXTENT)); //K
            stack.push(doc.createGMDElement(EX_EXTENT)); //L
            stack.push(doc.createGMDElement(DESCRIP)); //M
            root = stack.zip(doc.addGCOVal(gu.getField(GEOGRAPHIC_UNIT),CHARACTER));
        }
        List<GeographicBoundingBox> gbbs = determineWhichBBs();

        if(gbbs.size()==0)
            logger.error("Record with PERSISTENT_ID: " + djo.getPID() + ", got to the creating XML stage without a valid bounding box.");
        else {
            stack.push(root);
            stack.push(doc.createGMDElement(EXTENT)); //K
            XMLStack lowerStack = new XMLStack();
            stack.push(doc.createGMDElement(EX_EXTENT)); //L
            Element levelM = doc.createGMDElement(GEO_ELEMENT); //M
            Element levelN;
            for(GeographicBoundingBox gbb:gbbs) {

                //West
                levelN = doc.createGMDElement(EX_GEO_BB);
                lowerStack.push(levelN);
                lowerStack.push(doc.createGMDElement("westBoundLongitude"));
                levelN = lowerStack.zip(doc.addGCOVal(gbb.getWestLongitude(), DECIMAL));

                //East
                lowerStack.push(levelN);
                lowerStack.push(doc.createGMDElement("eastBoundLongitude"));
                levelN = lowerStack.zip(doc.addGCOVal(gbb.getEastLongitude(), DECIMAL));

                //North
                lowerStack.push(levelN);
                lowerStack.push(doc.createGMDElement("northBoundLatitude"));
                levelN = lowerStack.zip(doc.addGCOVal(gbb.getNorthLatitude(), DECIMAL));

                //South
                lowerStack.push(levelN);
                lowerStack.push(doc.createGMDElement("southBoundLatitude"));
                levelN = lowerStack.zip(doc.addGCOVal(gbb.getSouthLatitude(), DECIMAL));

                levelM.appendChild(levelN);
            }
            root = stack.zip(levelM);
        }
        return root;
    }

    private List<GeographicBoundingBox> determineWhichBBs() {
        List<DataverseGeoRecordFile> geoBBRecs = djo.getGeoDataFiles();
        List<DataverseGeoRecordFile> metaBBrecs = djo.getGeoDataMeta();
        List<DataverseGeoRecordFile> recs = new LinkedList<>();
        recs = (geoBBRecs.size()>=metaBBrecs.size())? geoBBRecs : metaBBrecs;
        LinkedList<GeographicBoundingBox> boxes = new LinkedList<>();
        for(DataverseGeoRecordFile dgrf: recs){
            if(dgrf.hasValidBB())
                boxes.add(dgrf.getGBB());
        }
        return boxes;
    }

    public static String getGeoCovPrimeName(List<String> field){
        return field.isEmpty() ? "" : field.get(field.size()-1);
    }
}
