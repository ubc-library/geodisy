package Dataverse.FindingBoundingBoxes;

import BaseFiles.Geonames;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicBoundingBox;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicCoverage;
import Dataverse.DataverseJavaObject;
import Dataverse.ExistingLocations;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import Dataverse.FindingBoundingBoxes.LocationTypes.City;
import Dataverse.FindingBoundingBoxes.LocationTypes.Country;
import Dataverse.FindingBoundingBoxes.LocationTypes.Province;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.HashMap;
import java.util.List;

import static _Strings.DVFieldNameStrings.*;

/**
 * GeonamesBBs collects a bounding box for a record that has enough geospatial fields filled out.
 * If just country is available then a local file with all the country bounding boxes is used.
 * If country and state/province or country state/province and city are provided, a call to the geonames.org API is
 * made to get a bounding box for that place. If no bounding box can be found, then a bounding box with all lat/longs
 * set to 361 is used.
 */
public class GeonamesBBs extends FindBoundBox {
    String doi;
    public DataverseJavaObject djo;
    Geonames geo;
    ExistingLocations existingLocations;

    public GeonamesBBs(String doi) {
        this.doi = doi;
        geo = new Geonames();
        existingLocations = ExistingLocations.getExistingLocations();
    }

    public GeonamesBBs(DataverseJavaObject djo){
        this.djo = djo;
        doi = this.djo.getPID();
        geo = new Geonames();
        existingLocations = ExistingLocations.getExistingLocations();
    }

    /**
     *
     * @param countryName
     * Country givenName is compared to list of country bounding boxes in Geonames_countries.xml
     * @return value from xml file, or a 361 lat/long bounding box if the country can't be found
     */
    @Override
    public BoundingBox getDVBoundingBox(String countryName) {
        Country country;
        if(countryName.startsWith("http")) {
            logger.error("Somehow got a country name that was a url: " + countryName + " from record" + doi);
            return new BoundingBox();
        }
        String c = existingLocations.getCountryFromCode(countryName);
        if(c.isEmpty())
            country = new Country(countryName);
         else
             country = new Country(c);
        if(!country.hasBoundingBox()){
            logger.info(countryName + " is not a valid country givenName in record at PERSISTENT_ID: " + doi + ", so no bounding box could be automatically generated. Check this record manually ", djo);
            return new BoundingBox();
        }
        return country.getBoundingBox();
    }

    /**
     * Get the bounding box from GeonamesBBs when Country and State/Province/Etc is known
     * For other GeonamesBBs Feature Codes [fcode] see: https://www.geonames.org/export/codes.html
     * @param country
     * @param province
     * @return a Bounding box with N/S/E/W extents
     *
     * If bounding box can't be found in geonames, return a search for just the country.
     */
    @Override
    public BoundingBox getDVBoundingBox(String country, String province)  {
        BoundingBox box = existingLocations.getBB(country, province);
        if(box.getLongWest()!=361)
            return box;
        Province p = new Province(province, country);
        if(p.hasBoundingBox())
            return p.getBoundingBox();
        logger.info(province + ", " + country + " is not returned nothing from geonames, so no bounding box could be automatically generated from it. Check "+ doi + " record manually ", djo);
        return new BoundingBox();
    }


    /**
     *
     * Get the bounding box from GeonamesBBs when Country, State/Province/Etc, and city are known
     *      * For other GeonamesBBs Feature Codes [fcode] see: https://www.geonames.org/export/codes.html
     * @param country
     * @param province
     * @param city
     * @return a Bounding box with N/S/E/W extents
     *
     * If bounding box cannot be found in geonames, run search with just country and province/province
     */

    @Override
    public BoundingBox getDVBoundingBox(String country, String province, String city) {
        BoundingBox box = existingLocations.getBB(country, province, city);
        if(box.getLongWest()!=361)
            return box;
        City cit = new City(city, province, country);
        if(cit.hasBoundingBox())
            return cit.getBoundingBox();
        logger.info(city+ ", " + province + ", " + country + " is not returned nothing from geonames, so no bounding box could be automatically generated from it. Check "+ doi + " record manually ", djo);
        return new BoundingBox();
    }

    public DataverseJavaObject getDJO(){
        return djo;
    }

    public void setDJO(DataverseJavaObject djo){
        this.djo = djo;
    }

    public List<GeographicBoundingBox> getDVBoundingBox(GeographicCoverage geoCoverage, List<GeographicBoundingBox> geoBBs ) {
        String givenCountry = geoCoverage.getField(GIVEN_COUNTRY);
        String givenProvince = geoCoverage.getField(GIVEN_PROVINCE);
        String givenCity = geoCoverage.getField(GIVEN_CITY);
        String commonCountry = geoCoverage.getField(COMMON_COUNTRY);
        String commonProvince = geoCoverage.getField(COMMON_PROVINCE);
        String commonCity = geoCoverage.getField(COMMON_CITY);
        String other = geoCoverage.getField(OTHER_GEO_COV);
        if(!other.equals(""))
            logger.info("This record had something in the Other field in the geographic coverage. Check out: " + djo.getSimpleFieldVal(RECORD_URL), djo);
        Boolean second = !givenCountry.equals(commonCountry) || !givenProvince.equals(commonProvince) || !givenCity.equals(commonCity);
        GeographicBoundingBox gbb;
        if(!givenCountry.isEmpty()) {
            if (!givenProvince.isEmpty()){
                if(!givenCity.isEmpty()) {
                    gbb = new GeographicBoundingBox(doi, getDVBoundingBox(givenCountry, givenProvince, givenCity));
                    geoBBs.add(gbb);
                    if(second){
                        gbb = new GeographicBoundingBox(doi, getDVBoundingBox(commonCountry, commonProvince, commonCity));
                        geoBBs.add(gbb);
                    }
                }else {
                    gbb = new GeographicBoundingBox(doi, getDVBoundingBox(givenCountry, givenProvince));
                    geoBBs.add(gbb);
                    if (second) {
                        gbb = new GeographicBoundingBox(doi, getDVBoundingBox(commonCountry, commonProvince));
                        geoBBs.add(gbb);
                    }
                }
            }else {
                gbb = new GeographicBoundingBox(doi, getDVBoundingBox(givenCountry));
                geoBBs.add(gbb);
                if (second) {
                    gbb = new GeographicBoundingBox(doi, getDVBoundingBox(commonCountry));
                    geoBBs.add(gbb);
                }
            }
        }else{
            if(!givenProvince.isEmpty()||!givenCity.isEmpty()||!other.isEmpty())
                logger.info("Record " + doi + " has Geographic Coverage fields filled out but no Country filled out",djo);
            else
                logger.error("Somehow got to calling Geonames without there being anything in the geographic coverage for " + doi);

        }
        return geoBBs;
    }
}
