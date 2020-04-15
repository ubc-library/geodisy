package Crosswalking.XML.XMLTools;

import static BaseFiles.GeodisyStrings.OPENGEOMETADATA_PASSWORD;
import static BaseFiles.GeodisyStrings.OPENGEOMETADATA_USERNAME;

public class XMLStrings {
    public final static String CI_RESPONSIBILITY = "CI_Responsibility";
    public final static String IDENT_INFO = "identificationInfo";
    public final static String MD_DATA_IDENT  =  "MD_DataIdentification";
    public final static String CI_ORG = "CI_Organisation";
    public final static String PARTY = "party";
    public final static String EXTENT = "extent";
    public final static String BEGIN_POSITION = "beginPosition";
    public final static String END_POSITION = "endPosition";
    public final static String CI_CITE = "CI_Citation";
    public final static String TIME_PERIOD = "TimePeriod";
    public final static String OTHER_CITE_DEETS = "otherCitationDetails";
    public final static String ADDITIONAL_DOCS = "additionalDocumentation";
    public final static String MD_IDENT = "MD_Identifier";
    public final static String PARTY_IDENT = "partyIdentifier";
    public final static String CODE = "code";
    public final static String CODE_SPACE = "codeSpace";
    public final static String IDENT = "identifier";
    public final static String NAME = "name";
    public final static String CI_ONLINE_RES = "CI_OnlineResource";
    public final static String ONLINE_RES = "onlineResource";
    public final static String LINKAGE = "linkage";
    public final static String CI_INDIV = "CI_Individual";
    public final static String XMLDATE = "date";
    public final static String CI_DATE = "CI_Date";
    public final static String MD_DIST = "MD_Distribution";
    public final static String MD_DISTRIBUTOR = "MD_Distributor";
    public final static String DESCRIP = "description";
    public final static String EX_EXTENT = "EX_extent";
    public final static String EX_GEO_BB =  "EX_GeographicBoundingBox";
    public final static String GEO_ELEMENT = "geographicElement";
    public final static String P_OF_CONTACT = "pointOfContact";
    public final static String RESOURCE_CONSTRAINTS = "resourceConstraints";
    public final static String MD_LEGAL_CONSTRAINTS = "MD_LegalConstraints";
    public final static String OTHER_CONSTRAINTS = "otherConstraints";
    public final static String SUPPLEMENTAL_INFO = "supplementalInformation";

    //TODO figure out what file(s) to actually use
    public final static String ISO_19139_VALIDATION_FILE_PATH = "./geodisyFiles/gco.xsd";

    //OPEN METADATA
    public final static String OPEN_METADATA_LOCAL_REPO = "/var/www/geoserver.frdr.ca/html/geodisy/";
    public final static String TEST_OPEN_METADATA_LOCAL_REPO = "XMLFilesTest/";
    public final static String OPEN_METADATA_REMOTE_REPO = "https://github.com/OpenGeoMetadata/ca.frdr.geodisy/";
    public final static String OPEN_METADATA_REMOTE_USERNAME = OPENGEOMETADATA_USERNAME;
    public final static String OPEN_METADATA_REMOTE_PASSWORD = OPENGEOMETADATA_PASSWORD;
}
