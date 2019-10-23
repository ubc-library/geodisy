package Crosswalking.XML.XMLGroups;

import BaseFiles.GeoLogger;
import Crosswalking.XML.XMLTools.SubElement;
import Crosswalking.XML.XMLTools.XMLDocObject;
import Crosswalking.XML.XMLTools.XMLStack;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicBoundingBox;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicCoverage;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicFields;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicUnit;
import Dataverse.DataverseJavaObject;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.w3c.dom.Element;


import java.util.LinkedList;
import java.util.List;

import static BaseFiles.GeodisyStrings.CHARACTER;
import static BaseFiles.GeodisyStrings.DECIMAL;
import static Crosswalking.XML.XMLTools.XMLStrings.*;
import static Dataverse.DVFieldNameStrings.GEOGRAPHIC_UNIT;

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
            List<String> country = gc.getCountryList();
            List<String> city = gc.getCityList();
            List<String> province = gc.getProvinceList();
            List<String> other = gc.getOtherGeographicCoverage();
            String name;
            //if the researcher uses an alternative name for the country, province, and/or city, then two geographic coverage units will be created from the single coverage unit
            int numberOfOptions = (country.size()>1 || province.size()>1 || city.size()>1) ? 2:1;
            for(int i = 0; i<numberOfOptions; i++) {
                stack.push(root); //J
                stack.push(doc.createGMDElement(EXTENT)); //K
                stack.push(doc.createGMDElement(EX_EXTENT)); //L
                stack.push(doc.createGMDElement(DESCRIP)); //M
                name = (country.size()==2 && i==1) ? country.get(1) : country.get(0);
                if (!province.isEmpty())
                    name = name + ", " + ((province.size()==2 && i==1) ? province.get(1) : province.get(0));
                if (!city.isEmpty())
                    name = name + ", " + ((city.size()==2 && i==1) ? city.get(1) : city.get(0));
                if (!other.isEmpty())
                    name = name + ", " + other.get(0);
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
            logger.error("Record with PERSISTENT_ID: " + djo.getDOI() + ", got to the creating XML stage without a valid bounding box.");
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
        List<GeographicBoundingBox> geoBBs = gf.getGeoBBoxes();
        List<GeographicBoundingBox> entered = new LinkedList<>();
        List<GeographicBoundingBox> generated = new LinkedList<>();
        for(GeographicBoundingBox geoBB: geoBBs){
            BoundingBox bb = geoBB.getbb();
            if(!bb.hasBoundingBox())
                continue;
            if(bb.isGenerated())
                generated.add(geoBB);
            else
                entered.add(geoBB);
        }
        return entered.size()<=generated.size()? generated : entered;
    }

    public static String getGeoCovPrimeName(List<String> field){
        return field.isEmpty() ? "" : field.get(field.size()-1);
    }
}
