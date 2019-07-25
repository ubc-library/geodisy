package Crosswalking.XML;

import BaseFiles.GeoLogger;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicBoundingBox;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicCoverage;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicFields;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicUnit;
import Dataverse.DataverseJavaObject;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;


import java.util.List;

public class GeographicInfo extends SubElement {
GeographicFields gf;
List<GeographicCoverage> geoCovers;
List<GeographicBoundingBox> geoBBs;
List<GeographicUnit> geoUnits;
GeoLogger logger;

    public GeographicInfo(DataverseJavaObject djo, XMLDocument doc, Element root) {
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
        stack.push(root); //J
        Element levelK = doc.createGMDElement("extent");
        Element levelL = doc.createGMDElement("EX_extent");
        for(GeographicCoverage gc: geoCovers){

        }
        for(GeographicUnit gu: geoUnits){


        }
        Boolean noBoundingBox = true;
        for(GeographicBoundingBox gBB: geoBBs){
            if(gBB.getSouthLatDub()==-361)
                continue;
           noBoundingBox = false;
        }
        if(noBoundingBox)
            logger.error("Record with DOI: " + djo.getDOI() + ", got to the creating XML stage without a valid bounding box.");







        return root;
    }
}
