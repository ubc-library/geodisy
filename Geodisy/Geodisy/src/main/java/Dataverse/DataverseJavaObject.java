package Dataverse;

import BaseFiles.GeoLogger;
import BaseFiles.GeodisyStrings;
import Crosswalking.XML.XMLGroups.GeographicInfo;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.*;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONSocialFieldClasses.SocialFields;
import Dataverse.FindingBoundingBoxes.GeonamesBBs;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.File;
import java.util.LinkedList;
import java.util.List;
import static Dataverse.DVFieldNameStrings.*;

/**
 * Java object structure to parse Dataverse Json into
 * May need to change field types for dates, URLs, and/or email addresses.
 *
 * Dataverse Alias: Java Class: JavaObject Variable
 *
 *
 */

public class DataverseJavaObject extends SourceJavaObject {
    private GeoLogger logger = new GeoLogger(this.getClass());

    public DataverseJavaObject(String server) {
        super(server);
    }

    /**
     * Gets the Citation metadata for the record
     * @param citationFieldsArray
     */
    @Override
    public void parseCitationFields(JSONObject citationFieldsArray) {
        citationFields.setBaseFields(citationFieldsArray);
        JSONObject metadata = getVersionSection(citationFieldsArray).getJSONObject("metadataBlocks");
        JSONArray currentArray = metadata.getJSONObject(CITATION).getJSONArray(FIELDS);
        for (Object o : currentArray) {
            JSONObject jo = (JSONObject) o;
            citationFields.setFields(jo);
        }
        hasContent=true;
        geoFields = new GeographicFields(this);
        socialFields = new SocialFields(this);
    }

    /**
     * Gets the Geospatial metadata for the record
     * @param geoFieldsArray
     */
    @Override
    public void parseGeospatialFields(JSONArray geoFieldsArray) {
        for(Object o: geoFieldsArray){
            JSONObject jo = (JSONObject) o;
            this.geoFields.setFields(jo);
        }
        List<BoundingBox> bb = new LinkedList<>();
        GeonamesBBs geonames = new GeonamesBBs(this);
        if(!geoFields.hasBB()){
            List<GeographicCoverage> coverages = geoFields.getListField(GEOGRAPHIC_COVERAGE);
            if(!coverages.isEmpty())

                for(GeographicCoverage gc: coverages){
                    String country = GeographicInfo.getGeoCovPrimeName(gc.getCountryList());
                    String province = GeographicInfo.getGeoCovPrimeName(gc.getProvinceList());
                    String city = GeographicInfo.getGeoCovPrimeName(gc.getCityList());
                    if(country.isEmpty())
                        continue;
                    else{
                        if(province.isEmpty())
                            bb.add(gc.getCountryObject().getBoundingBox());
                        else{
                            if(city.isEmpty())
                                bb.add(gc.getProvinceObject().getBoundingBox());
                            else
                                bb.add(gc.getCityObject().getBoundingBox());
                        }
                    }
                }
        }
    }
    @Override
    public void parseSocialFields(JSONArray socialFieldsArray){
        for(Object o: socialFieldsArray){
            JSONObject jo = (JSONObject) o;
            this.socialFields.setFields(jo);
        }

    }

   /* //Uncomment if we decide to include the Journal Field Metadata block
    @Override
    public void parseJournalFields(JSONArray journalFieldsArray) {
        for (Object o : journalFieldsArray) {
            JSONObject jo = (JSONObject) o;
            this.journalFields.setFields(jo);
        }
    }*/
    /**
     * Gets the File metadata for the record.
     * @param fileFieldsArray
     */
    @Override
    public void parseFiles(JSONArray fileFieldsArray) {
        for(Object o: fileFieldsArray){
            JSONObject jo = (JSONObject) o;
            if(jo.getBoolean("restricted"))
                continue;
            String title = jo.getString("label");
            JSONObject dataFile = (JSONObject) jo.get("dataFile");
            DataverseRecordFile dRF;
            //Some Dataverses don't have individual DOIs for files, so for those I will use the database's file id instead
            if(dataFile.has(DOI)&& !dataFile.getString(DOI).equals("")) {
                String doi = dataFile.getString(DOI);
                dRF = new DataverseRecordFile(title, doi,dataFile.getInt("id"), server, citationFields.getDOI(), this);
            }else{
                int dbID = (int) dataFile.get("id");
                dRF = new DataverseRecordFile(title,dbID,server,citationFields.getDOI());
            }
            dataFiles.add(dRF);
        }
    }
    //if changed, need to change copy in CitationFields Class
    @Override
    public JSONObject getVersionSection(JSONObject current) {
        if(current.has("latestVersion"))
            return current.getJSONObject("latestVersion");
        else if(current.has("datasetVersion"))
            return current.getJSONObject("datasetVersion");
        else{
            logger.error("missing a _____Version field in the dataverseJson in " + current.toString());
            return new JSONObject();
        }
    }

    public DataverseRecordInfo generateDRI(){
        String major, minor,doi;
        major = getCitationFields().getSimpleCitationFields().getVersionMajor();
        minor = getCitationFields().getSimpleCitationFields().getVersionMinor();
        doi = getCitationFields().getDOI();
        DataverseRecordInfo answer = new DataverseRecordInfo();
        answer.setDoi(doi);
        answer.setMajor(major);
        answer.setMinor(minor);
        return answer;
    }

    /**
     *  Deletes the directory of the record's files if it exists and then downloads the updated
     *  files, excluding non-geospatial file types other than .zip
     *  If there aren't any geospatial files uploaded, the entire directory get's deleted.
     */
    @Override
    public void downloadFiles() {
        File f = new File("datasetFiles\\\\" + urlized(citationFields.getDOI()));
        deleteDir(f);
        for (DataverseRecordFile dRF : dataFiles) {
            if(GeodisyStrings.fileToIgnore(dRF.title))
                continue;
            dRF.getFile();
            if(GeodisyStrings.hasGeospatialFile(dRF.title))
                hasGeospatialFile = true;
        }
        if(f.list() != null && f.list().length==0)
            f.delete();

    }
}
