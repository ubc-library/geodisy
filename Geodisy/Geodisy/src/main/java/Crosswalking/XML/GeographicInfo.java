package Crosswalking.XML;

import BaseFiles.GeoLogger;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicBoundingBox;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicCoverage;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicFields;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicUnit;
import Dataverse.DataverseJavaObject;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.w3c.dom.Element;


import java.util.List;

import static BaseFiles.GeodisyStrings.CHARACTER;
import static BaseFiles.GeodisyStrings.DECIMAL;
import static Crosswalking.XML.XMLStrings.*;
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
    //TODO complete
    @Override
    public Element getFields() {

        for(GeographicCoverage gc: geoCovers){
            stack.push(root); //J
            stack.push(doc.createGMDElement(EXTENT)); //K
            stack.push(doc.createGMDElement(EX_EXTENT)); //L
            stack.push(doc.createGMDElement(DESCRIP)); //M
            String country = gc.getCountry();
            String city = gc.getCity();
            String province = gc.getState();
            String other = gc.getOtherGeographicCoverage();
            String name;
            name = country;
            if(!province.isEmpty())
                name = name + ", " + province;
            if(!city.isEmpty())
                name = name + ", " + city;
            if(!other.isEmpty())
                name = name + ", " + other;
            root = stack.zip(doc.addGCOVal(name,CHARACTER));
        }
        for(GeographicUnit gu: geoUnits){
            stack.push(root);
            stack.push(doc.createGMDElement(EXTENT)); //K
            stack.push(doc.createGMDElement(EX_EXTENT)); //L
            stack.push(doc.createGMDElement(DESCRIP)); //M
            root = stack.zip(doc.addGCOVal(gu.getField(GEOGRAPHIC_UNIT),CHARACTER));
        }

        BoundingBox gbb = gf.getBoundingBox();
        if(gbb.getLatSouth()==-361||gbb.getLatSouth()==361)
            logger.error("Record with DOI: " + djo.getDOI() + ", got to the creating XML stage without a valid bounding box.");
        stack.push(root);
        stack.push(doc.createGMDElement(EXTENT)); //K
        XMLStack lowerStack = new XMLStack();
        Element levelL = doc.createGMDElement(EX_EXTENT); //L
        //West
        lowerStack.push(levelL);
        lowerStack.push(doc.createGMDElement(GEO_ELEMENT));
        lowerStack.push(doc.createGMDElement(EX_GEO_BB));
        lowerStack.push(doc.createGMDElement("westBoundLongitude"));
        levelL = lowerStack.zip(doc.addGCOVal( Double.toString(gbb.getLongWest()),DECIMAL));

        //East
        lowerStack.push(levelL);
        lowerStack.push(doc.createGMDElement(GEO_ELEMENT));
        lowerStack.push(doc.createGMDElement(EX_GEO_BB));
        lowerStack.push(doc.createGMDElement("eastBoundLongitude"));
        levelL = lowerStack.zip(doc.addGCOVal( Double.toString(gbb.getLongEast()),DECIMAL));

        //North
        lowerStack.push(levelL);
        lowerStack.push(doc.createGMDElement(GEO_ELEMENT));
        lowerStack.push(doc.createGMDElement(EX_GEO_BB));
        lowerStack.push(doc.createGMDElement("northBoundLatitude"));
        levelL = lowerStack.zip(doc.addGCOVal( Double.toString(gbb.getLatNorth()),DECIMAL));

        //South
        lowerStack.push(levelL);
        lowerStack.push(doc.createGMDElement(GEO_ELEMENT));
        lowerStack.push(doc.createGMDElement(EX_GEO_BB));
        lowerStack.push(doc.createGMDElement("southBoundLatitude"));

        root = stack.zip(lowerStack.zip(doc.addGCOVal( Double.toString(gbb.getLatSouth()),DECIMAL)));
        return root;
    }
}
