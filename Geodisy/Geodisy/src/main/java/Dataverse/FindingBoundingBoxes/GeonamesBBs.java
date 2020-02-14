package Dataverse.FindingBoundingBoxes;

import BaseFiles.Geonames;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicBoundingBox;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicCoverage;
import Dataverse.DataverseJavaObject;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import Dataverse.FindingBoundingBoxes.LocationTypes.City;
import Dataverse.FindingBoundingBoxes.LocationTypes.Country;
import Dataverse.FindingBoundingBoxes.LocationTypes.Province;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.HashMap;
import java.util.List;

import static Dataverse.DVFieldNameStrings.*;

/**
 * GeonamesBBs collects a bounding box for a record that has enough geospatial fields filled out.
 * If just country is available then a local file with all the country bounding boxes is used.
 * If country and state/province or country state/province and city are provided, a call to the geonames.org API is
 * made to get a bounding box for that place. If no bounding box can be found, then a bounding box with all lat/longs
 * set to 361 is used.
 */
public class GeonamesBBs extends FindBoundBox {
    private String USER_NAME = "geodisy";
    Countries countries;
    HashMap<String, BoundingBox> bBoxes;
    String doi;
    public DataverseJavaObject djo;
    Geonames geo;

    public GeonamesBBs(String doi) {
        this.doi = doi;
        this.countries = Countries.getCountry();
        this.bBoxes = countries.getBoundingBoxes();
        geo = new Geonames();
    }

    public GeonamesBBs(DataverseJavaObject djo){
        this.djo = djo;
        doi = this.djo.getDOI();
        this.countries = Countries.getCountry();
        this.bBoxes = countries.getBoundingBoxes();
        geo = new Geonames();
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
        if(countries.isCountryCode(countryName))
            country = countries.getCountryByCode(countryName);
         else
             country = countries.getCountryByName(countryName);
        if(country.getCountryCode().matches("_JJ")||country.getCountryCode().matches("")){
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
        BoundingBox box = countries.getExistingBB(country, province);
        if(box.getLongWest()!=361)
            return box;

        String unURLedProvince =province;
        try {
            province = URLEncoder.encode(province, "UTF-8");
        } catch (UnsupportedEncodingException ignored) {
            // Can be safely ignored because UTF-8 is always supported
        }
        String responseString = geo.getGeonamesProvince(province,country);
        box = readResponse(responseString,doi, djo);
        if(box.getLongWest()!=361) {
            Province p = new Province(unURLedProvince, country);
            p.setBoundingBox(box);
            bBoxes.put(addDelimiter(country, unURLedProvince), box);
            countries.setBoundingBoxes(bBoxes);
        }
        return box;
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
        BoundingBox box = countries.getExistingBB(country, province, city);
        if(box.getLongWest()!=361)
            return box;
        String unURLedProvince = province;
        String unURLedCity = city;
        try {
            city = URLEncoder.encode(city, "UTF-8");
            province = URLEncoder.encode(province, "UTF-8");
        } catch (UnsupportedEncodingException ignored) {
            // Can be safely ignored because UTF-8 is always supported
        }

        String responseString = geo.getGeonamesCity(city,province, country);
        box = readResponse(responseString,doi, djo);
        if(box.getLongWest()==361) {
            City cit = new City(city, province, country);
            cit.setBoundingBox(box);
            bBoxes.put(addDelimiter(country, unURLedProvince, unURLedCity), box);
            countries.setBoundingBoxes(bBoxes);
        }

        return box;
    }

    /**
     *
     * @param country
     * @param other
     * @return a bounding box with likely no real values
     *
     * We decided that if the Other Geographic Coverage is filled in at all that an actual human needs to look into
     * the geographic coverage.
     */
    @Override
    public BoundingBox getDVBoundingBoxOther(String country, String other) {
        BoundingBox box =  countries.getExistingBB(country, other);

        /*
        if(box.getLongWest()!=361)
            return box;
        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", GEONAMES_USERNAME);
        parameters.put("style","FULL");
        String countryCode = countries.isCountryCode(country) ? country : countries.getCountryByCode(country);
        parameters.put("country",countryCode);
        String unURLedOther = other;
        try {
            other = URLEncoder.encode(other, "UTF-8");
        } catch (UnsupportedEncodingException ignored) {
            // Can be safely ignored because UTF-8 is always supported
        }
        String urlString  = other + addParameters(parameters);
        HttpURLConnection con = getHttpURLConnection(urlString);

        try {
            box = readResponse(con);
        }catch (IOException e){
            logger.error("something went wrong with the http request for a bounding box");
        }
        if(box.getLongWest()!=361)
            bBoxes.put(addDelimiter(country,unURLedOther),box);
        else
            box = getDVBoundingBox(country);*/
        if(box.getLatSouth() == 361)
            logger.info("Record with PERSISTENT_ID: " + doi + " has something in the 'Other Geographic Coverage' field so needs to be inspected" ,djo);
        return box;

    }
    /**
     *
     * @param country
     * @param province
     * @param other
     * @return a bounding box with likely no real values
     *
     * We decided that if the Other Geographic Coverage is filled in at all that an actual human needs to look into
     * the geographic coverage.
     */
    @Override
    BoundingBox getDVBoundingBoxOther(String country, String province, String other) {
        BoundingBox box =  countries.getExistingBB(country, province, other);
        /*if(box.getLongWest()!=361)
            return box;
        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", GEONAMES_USERNAME);
        parameters.put("style","FULL");
        String countryCode = countries.isCountryCode(country) ? country : countries.getCountryByCode(country);
        parameters.put("country",countryCode);
        String unURLedOther = other;
        String unURLedProvince = province;
        try {
            other = URLEncoder.encode(other, "UTF-8");
            province = URLEncoder.encode(province, "UTF-8");
        } catch (UnsupportedEncodingException ignored) {
            // Can be safely ignored because UTF-8 is always supported
        }
        String urlString  = other + "%2C%20" + province + addParameters(parameters);
        HttpURLConnection con = getHttpURLConnection(urlString);

        try {
            box = readResponse(con);
        }catch (IOException e){
            logger.error("something went wrong with the http request for a bounding box");
        }
        if(box.getLongWest()!=361)
            bBoxes.put(addDelimiter(country,unURLedProvince,unURLedOther),box);
        else
            box = getDVBoundingBoxOther(country, other);*/
        if(box.getLatSouth() == 361)
            logger.info("Record with PERSISTENT_ID: " + doi + " has something in the 'Other Geographic Coverage' field so needs to be inspected" ,djo);
        return box;
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
