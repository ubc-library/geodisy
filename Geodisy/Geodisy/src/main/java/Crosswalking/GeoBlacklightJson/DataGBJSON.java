package Crosswalking.GeoBlacklightJson;

import BaseFiles.GeoLogger;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationSimpleJSONFields.Date;
import _Strings.GeodisyStrings;
import _Strings.DVFieldNameStrings;
import Dataverse.DataverseGeoRecordFile;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields.Author;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields.DateOfCollection;
import Dataverse.DataverseJSONFieldClasses.Fields.CitationCompoundFields.Description;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicBoundingBox;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicCoverage;
import Dataverse.DataverseJavaObject;
import Dataverse.DataverseRecordFile;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import static _Strings.GeodisyStrings.*;
import static _Strings.GeoBlacklightStrings.*;
import static _Strings.GeoBlacklightStrings.RECORD_URL;
import static _Strings.XMLStrings.TEST_OPEN_METADATA_LOCAL_REPO;
import static _Strings.DVFieldNameStrings.*;

public class DataGBJSON extends GeoBlacklightJSON{
    GeoLogger logger;

    public DataGBJSON(DataverseJavaObject djo) {
        super();
        javaObject = djo;
        geoBlacklightJson = "";
        doi = djo.getPID();
        logger = new GeoLogger(this.getClass());
        geoFiles = djo.getGeoDataFiles();
        geoMeta = djo.getGeoDataMeta();
    }
    @Override
    protected JSONObject getRequiredFields(GeographicBoundingBox gbb, int total){
        String number = gbb.getFileNumber();
        jo.put("geoblacklight_version","1.0");
        jo.put("dc_identifier_s", GeodisyStrings.urlSlashes(javaObject.getSimpleFieldVal(DVFieldNameStrings.RECORD_URL)));
        String geoserverLabel = getGeoserverLabel(gbb);
        jo.put("layer_slug_s", "geodisy:" + geoserverLabel);
        if(total>1) {
            number = padZeros(number,total);
            jo.put("dc_title_s", javaObject.getSimpleFields().getField(TITLE) + " (" + number + " of " + total + ")");
        }
        else
            jo.put("dc_title_s",javaObject.getSimpleFields().getField(TITLE));
        jo.put("dc_rights_s","Public");
        jo.put("dct_provenance_s",javaObject.getSimpleFields().getField(PUBLISHER));
        jo.put("dc_publisher_s",javaObject.getSimpleFields().getField(PUBLISHER));
        jo.put("solr_geom","ENVELOPE(" + getBBString(gbb.getBB()) + ")");
        return jo;
    }

    private String padZeros(String number, int total) {
        if(total>9) {
            if(number.length()<2)
                number = "0"+number;
        }
        if(total>99)
        {
            if(number.length()<3)
                number = "0"+number;
        }
        if(total>999)
        {
            if(number.length()<4)
                number = "0"+number;
        }
        if(total>9999)
        {
            if(number.length()<5)
                number = "0"+number;
        }
        return number;
    }

    private void addRecommendedFields(String geoserverLabel, GeographicBoundingBox gbb, boolean isOnGeoserver) {
        getDSDescriptionSingle();
        if(!gbb.getField(GEOMETRY).isEmpty())
            jo.put("layer_geom_type_s",gbb.getField(GEOMETRY));
        JSONObject j = addBaseRecordInfo();
        if(!gbb.getField(GEOMETRY).equals(UNDETERMINED) && USE_GEOSERVER) {
            j = addDataDownloadOptions(gbb, j, isOnGeoserver);
        }
        jo.put(EXTERNAL_SERVICES, j.toString());
        if(!geoserverLabel.isEmpty())
            jo.put("layer_id_s", geoserverLabel);
    }

    private String getGeoserverLabel(GeographicBoundingBox gbb) {
        return gbb.getField(GEOSERVER_LABEL);
    }


    private String getBBString(BoundingBox bb){
        return bb.getLongWest() + ", " + bb.getLongEast() + ", " + bb.getLatNorth() + ", " + bb.getLatSouth();
    }

    @Override
    protected JSONObject addDataDownloadOptions(GeographicBoundingBox gbb, JSONObject jo, boolean isOnGeoserver) {
        if(isOnGeoserver) {
            jo.put(WMS, GEOSERVER_WMS_LOCATION);
            if (gbb.getField(FILE_NAME).endsWith(".shp"))
                jo.put(WFS, GEOSERVER_WFS_LOCATION);
        }
        if(!gbb.getField(FILE_URL).isEmpty())
            jo.put(DIRECT_FILE_DOWNLOAD, gbb.getField(FILE_URL));
        return jo;
    }

    @Override
    protected JSONObject addBaseRecordInfo(){
        JSONObject jo = new JSONObject();
        jo.put(RECORD_URL,  GeodisyStrings.urlSlashes(javaObject.getSimpleFieldVal(DVFieldNameStrings.RECORD_URL)));
        jo.put(ISO_METADATA, END_XML_JSON_FILE_PATH + GeodisyStrings.urlSlashes(GeodisyStrings.removeHTTPS(javaObject.getSimpleFieldVal(PERSISTENT_ID)).replace(".","/") + "/" + ISO_METADATA_FILE_ZIP));
        return jo;
    }

    //TODO, check I am getting all the optional fields I should be
    @Override
    protected JSONObject getOptionalFields(DataverseRecordFile drf, int totalRecordsInStudy) {
        GeographicBoundingBox gbb = drf.getGBB();
        String geoserverLabel = getGeoserverLabel(gbb).toLowerCase();
        getFileType(drf);
        addRecommendedFields(geoserverLabel, gbb, drf.isOnGeoserver());
        getAuthors();
        getIssueDate();
        getLanguages();
        getPlaceNames(gbb);
        getSubjects();
        getType();
        getRelatedRecords(drf);
        getModifiedDate();
        getSolrYear();
        getTemporalRange();

        return jo;
    }

    private void getTemporalRange() {
        List<DateOfCollection> dates = javaObject.getCitationFields().getListField(DATE_OF_COLLECT);
        String dateRange = "";
        int count = 1;
        for(DateOfCollection d: dates){
            int start = d.getStartYear();
            int end = d.getEndYear();
            if(start==-111111){
                if(end==-111111) {
                    continue;
                }
                else{
                    if(count == 1) {
                        dateRange += end;
                        count++;
                        continue;
                    } else {
                        dateRange += ", " + end;
                        count++;
                        continue;
                    }
                }
            }else{
                if(end == -111111){
                    if(count == 1) {
                        dateRange += start;
                        count++;
                        continue;
                    } else {
                        dateRange += ", " + start;
                    }
                } else{
                    String range = "";
                    if(start<end){
                        range = start + "-" + end;
                    }else if (start==end) {
                        range = start + "";
                    }else {
                        range = end + "_" + start;
                    }
                    if(count>1) {
                        dateRange += ", " + range;
                        count++;
                        continue;
                    }
                    else {
                        dateRange += range;
                        count++;
                        continue;
                    }
                }
            }

        }
        if(!dateRange.equals(""))
            jo.put("dct_temporal_sm",dateRange);

    }

    private void getSolrYear() {
        List<DateOfCollection> dates = javaObject.getCitationFields().getListField(DATE_OF_COLLECT);
        int date = 10000;
        for(DateOfCollection doc: dates){
            String dateString = doc.getStartDate();
            int current;
            if(dateString.contains("-"))
                current = Integer.valueOf(dateString.substring(0,dateString.indexOf("-")));
            else
                current = Integer.valueOf(dateString);

            if(date>current)
                date = current;

        }
        if(date!=10000)
            jo.put("solr_year_i",String.valueOf(date));

    }

    private void getModifiedDate() {
        String modDate = javaObject.getSimpleFieldVal(LAST_MOD_DATE);
        if(!modDate.isEmpty() && !modDate.equals("9999"))
            jo.put("layer_modified_dt",modDate);
    }

    private void getRelatedRecords(DataverseRecordFile drf) {
        LinkedList<DataverseGeoRecordFile> geo = javaObject.getGeoDataFiles();
        LinkedList<DataverseGeoRecordFile> meta = javaObject.getGeoDataMeta();
        LinkedList<DataverseGeoRecordFile> recs = (geo.size()>=meta.size())? geo:meta;
        if(recs.size()>1){
            JSONArray ja = new JSONArray();
            for(DataverseGeoRecordFile dgrf : recs){
                if(!getGeoserverLabel(drf.getGBB()).equals(getGeoserverLabel(dgrf.getGBB())))
                    ja.put(getGeoserverLabel(dgrf.getGBB()).toLowerCase());
            }
            jo.put("dc_source_sm",ja);
            jo.put("dct_isPartOf_sm",javaObject.getSimpleFieldVal(TITLE));
        }
    }

    private void getFileType(DataverseRecordFile drf) {
        if(!drf.getGBB().getField(FILE_URL).isEmpty()) {
            String format = getFileTypeName(drf.getTranslatedTitle());
            if(format.isEmpty())
                format = "File";
            jo.put("dc_format_s", format);
        }
    }

    private String getFileTypeName(String translatedTitle) {
        try {
            String extension = translatedTitle.substring(translatedTitle.lastIndexOf(".") + 1).toLowerCase();
            switch (extension){
                case ("zip"):
                    return "Zip File";
                case ("shp"):
                    return "Shapefile";
                case ("geojson"):
                    return "GeoJSON";
                case ("tif"):
                case ("geotif"):
                case ("tiff"):
                case ("geotiff"):
                    return "GeoTIFF";
                case ("png"):
                    return "PNG";
                default:
                    return "Geospatial File";



            }
        }catch (IndexOutOfBoundsException e){
            logger.warn("There was no extension on the original file name for record " + this.doi);
            return "";
        }
    }

    private void getType() {
        jo.put("dc_type_s","Dataset");
    }

    private void getSubjects() {
        JSONArray ja = new JSONArray();
        List<String> subjects = javaObject.getCitationFields().getListField(SUBJECT);
        for(String s : subjects){
            ja.put(s);
        }
        jo.put("dc_subject_sm",ja);
    }

    /*This version is only be activated when the GeoBlacklight schema changes to allow multiple values
    private void getLanguages() {
        JSONArray ja = new JSONArray();
        List<String> languages = javaObject.getCitationFields().getListField(LANGUAGE);
        for(String s : languages){
            ja.put(s);
        }
        jo.put("dc_language_s",ja);
    }
     */
    // This version is only to be used while the GeoBlacklight schema doesn't allow multiple value, when that happens use the method directly above
    private void getLanguages() {
        String languageStrings = "";
        List<String> languages = javaObject.getCitationFields().getListField(LANGUAGE);
        if(languages.size()==0)
            return;
        for (String s : languages) {
            if (languageStrings.isEmpty())
                languageStrings = s;
            else
                languageStrings = languageStrings + ", " + s;
        }
        jo.put("dc_language_s", languageStrings);
    }

    private void getIssueDate() {
        String dateString = javaObject.getSimpleFields().getField(PUB_DATE);
        if(dateString.equals(""))
            return;
        Date d = new Date(dateString);
        jo.put("dct_issued_s",d.getDateAsString());
    }


    private void getAuthors() {
        JSONArray ja = new JSONArray();
        List<Author> authors = javaObject.getCitationFields().getListField(AUTHOR);
        if(authors.size()==0)
            return;
        for(Author a:authors){
            ja.put(a.getField(AUTHOR_NAME));
        }
        jo.put("dc_creator_sm",ja);
    }
    private void getPlaceNames(GeographicBoundingBox gbb){
        JSONArray ja =  new JSONArray();
        HashSet<String> placeNames = new HashSet<>();
        List<GeographicCoverage> places = javaObject.getGeoFields().getGeoCovers();
        for(GeographicCoverage gc:places){
            for(String place:gc.getPlaceNames()){
                placeNames.add(place);
            }
        }
        if(!gbb.getField(PLACE).isEmpty()){
            ja.put(gbb.getField(PLACE));
            jo.put("dct_spatial_sm",ja);
        } else if(placeNames.size()>0) {
            for (String s : placeNames) {
                ja.put(s);
            }
            jo.put("dct_spatial_sm",ja);
        }

    }
    //Description as array, but that seems to be wrong
    private void getDSDescription() {
        JSONArray ja = new JSONArray();
        List<Description> descriptions = javaObject.getCitationFields().getListField(DS_DESCRIPT);
        if(descriptions.size()==0)
            return;
        for(Description d:descriptions){
            ja.put(d.getDsDescriptionValue());
        }
        jo.put("dc_description_s",ja);
    }
    //Description as String, but that could be wrong
    private void getDSDescriptionSingle(){
        String answer = "";
        List<Description> descriptions = javaObject.getCitationFields().getListField(DS_DESCRIPT);
        if(descriptions.size()==0)
            return;
        for(Description d:descriptions){
            answer += d.getDsDescriptionValue()+" ";
        }
        if(!answer.isEmpty())
            jo.put("dc_description_s",answer);
    }

    public void saveJSONToFile(String json, String doi, String folderName){
        String name = folderName;
        String end = "";
        if(folderName.contains("(")){
            name = folderName.substring(0,folderName.indexOf(" ("));
            end = "/" + getNumber(folderName);
        }
        genDirs(name + end, BASE_LOCATION_TO_STORE_METADATA);
        BaseFiles.FileWriter file = new BaseFiles.FileWriter();
        try {
            file.writeStringToFile(json,GeodisyStrings.getRoot() + BASE_LOCATION_TO_STORE_METADATA + name.replace(".","/") + end + "/" +"geoblacklight.json");
        } catch (IOException e) {
            logger.error("Something went wrong trying to create a JSON file with doi:" + doi);
        }

    }

    private String getNumber(String folderName) {
        int start = folderName.indexOf("File ") + 5;
        int end = folderName.indexOf(" of");
        return folderName.substring(start,end);
    }

    public void saveJSONToTestFile(String json, String doi, String uuid){
        genDirs(doi, TEST_OPEN_METADATA_LOCAL_REPO);
        BaseFiles.FileWriter file = new BaseFiles.FileWriter();
        try {
            file.writeStringToFile(json,"./"+TEST_OPEN_METADATA_LOCAL_REPO + doi + "/" + uuid + "/" +"geoblacklight.json");
        } catch (IOException e) {
            logger.error("Something went wrong trying to create a JSON file with doi:" + doi);
        }

    }
}
