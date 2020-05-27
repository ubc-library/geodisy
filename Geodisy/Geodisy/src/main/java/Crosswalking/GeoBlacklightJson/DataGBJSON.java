package Crosswalking.GeoBlacklightJson;

import BaseFiles.GeoLogger;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

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
        doi = djo.getDOI();
        logger = new GeoLogger(this.getClass());
        geoFiles = djo.getGeoDataFiles();
        geoMeta = djo.getGeoDataMeta();
    }
    //TODO check if Dataverse publisher field be included in the slug?
    @Override
    protected JSONObject getRequiredFields(GeographicBoundingBox gbb, int total){
        String number = gbb.getFileNumber();
        jo.put("geoblacklight_version","1.0");
        jo.put("dc_identifier_s", GeodisyStrings.urlSlashes(javaObject.getSimpleFieldVal(DVFieldNameStrings.RECORD_URL)));
        String geoserverLabel = getGeoserverLabel(gbb).toLowerCase();
        jo.put("layer_slug_s", geoserverLabel);
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

    private void addRecommendedFields(String geoserverLabel, GeographicBoundingBox gbb) {
        getDSDescriptionSingle();
        if(!gbb.getField(GEOMETRY).isEmpty())
            jo.put("layer_geom_type_s",gbb.getField(GEOMETRY));
        JSONArray ja = addBaseRecordInfo();
        if(!gbb.getField(GEOMETRY).equals(UNDETERMINED) && USE_GEOSERVER) {
            ja = addDataDownloadOptions(gbb, ja);
        }
        String externalServices = "{";
        for (Object o : ja) {
            if (!externalServices.equals("{"))
                externalServices += ",";
            externalServices += (String) o;
        }
        externalServices += "}";

        jo.put(EXTERNAL_SERVICES, externalServices);
        if(!geoserverLabel.isEmpty())
            jo.put("layer_id_s", geoserverLabel);
    }

    private String getGeoserverLabel(GeographicBoundingBox gbb) {
        boolean generated = gbb.isGeneratedFromGeoFile();
        if (generated) {
            return "geodisy:" + gbb.getField(GEOSERVER_LABEL);
        }
        else
            return gbb.getField(GEOSERVER_LABEL);
    }


    private String getBBString(BoundingBox bb){
        return bb.getLongWest() + ", " + bb.getLongEast() + ", " + bb.getLatNorth() + ", " + bb.getLatSouth();
    }

    @Override
    protected JSONArray addDataDownloadOptions(GeographicBoundingBox gbb, JSONArray ja) {
        if (gbb.getField(FILE_NAME).endsWith(".tif"))
        ja.put(WMS);
        if (gbb.getField(FILE_NAME).endsWith(".shp"))
            ja.put(WFS);
        if(!gbb.getField(FILE_URL).isEmpty())
            ja.put(DIRECT_FILE_DOWNLOAD + stringed(gbb.getField(FILE_URL)));
        return ja;
    }

    @Override
    protected JSONArray addBaseRecordInfo(){
        JSONArray ja = new JSONArray();
        ja.put(RECORD_URL + GeodisyStrings.urlSlashes(stringed(javaObject.getSimpleFieldVal(DVFieldNameStrings.RECORD_URL))));
        ja.put(ISO_METADATA + stringed(END_XML_JSON_FILE_PATH + GeodisyStrings.urlSlashes(javaObject.getSimpleFieldVal(PERSISTENT_ID).replace(".","/") + "/" + ISO_METADATA_FILE_ZIP)));
        return ja;
    }

    //TODO, check I am getting all the optional fields I should be
    @Override
    protected JSONObject getOptionalFields(DataverseRecordFile drf, int totalRecordsInStudy) {
        GeographicBoundingBox gbb = drf.getGBB();
        String geoserverLabel = getGeoserverLabel(gbb).toLowerCase();
        getFileType(drf);
        addRecommendedFields(geoserverLabel, gbb);
        getAuthors();
        getIssueDate();
        getLanguages();
        getPlaceNames(gbb);
        getSubjects();
        getType();
        getRelatedRecords(drf);
        getModifiedDate();
        getSolrYear();

        return jo;
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
        if(!modDate.isEmpty())
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss'Z'");
        LocalDate date = LocalDate.parse(dateString);
        LocalDateTime dateTime = date.atStartOfDay();
        String dateTimeString = dateTime.format(formatter);
        jo.put("dct_issued_dt",dateTimeString);
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
