package Dataverse.DataverseJSONFieldClasses;

import java.util.Arrays;
import java.util.List;

public class DVFieldNames {

    //General Field Names
    public final static String TYPE_NAME = "typeName";
    public final static String VAL = "value";
    public final static String DV_ID = "entity_id";
    public final static String FIELDS = "fields";
    public final static String CITATION = "citation";
    public final static String BASE_DV_URL = "https://206-12-90-131.cloud.computecanada.ca/"; //currently our sandbox


    //Simple Field Names
    public final static String ACCESS_TO_SOURCES = "accessToSources";
    public final static String ALT_TITLE = "alternativeTitle";
    public final static String ALT_URL = "alternativeURL";
    public final static String CHAR_OF_SOURCES = "characteristicOfSources";
    public final static String DEPOS_DATE = "dateOfDeposit";
    public final static String DEPOSITOR = "depositor";
    public final static String DIST_DATE = "distributionDate";
    public final static String LICENSE = "license";
    public final static String NOTES_TEXT = "notesText";
    public final static String ORIG_OF_SOURCES = "originOfSources";
    public final static String PROD_DATE = "productionDate";
    public final static String PROD_PLACE = "productionPlace";
    public final static String PUB_DATE = "publicationDate";
    public final static String PUBLISHER = "publisher";
    public final static String TITLE = "title";
    public final static String SUBTITLE = "subtitle";
    public final static List<String> SIMPLE_FIELD_NAMES = Arrays.asList(ACCESS_TO_SOURCES,ALT_TITLE,ALT_URL,CHAR_OF_SOURCES,DEPOS_DATE,DEPOSITOR,DIST_DATE,LICENSE,NOTES_TEXT,ORIG_OF_SOURCES,PROD_DATE,PROD_PLACE,PUB_DATE,PUBLISHER,TITLE,SUBTITLE);

    //String List Field Names
    public final static String RELATED_MATERIAL = "relatedMaterial";
    public final static String RELATED_DATASETS = "relatedDatasets";
    public final static String OTHER_REFERENCES = "otherReferences";
    public final static String DATA_SOURCE = "dataSource";
    public final static String KIND_OF_DATA = "kindOfData";
    public final static String LANGUAGE = "language";
    public final static String SUBJECT = "subject";

    //Author Field Names
    public final static String AUTHOR = "author";
    public final static String AUTHOR_AFFIL = "authorAffiliation";
    public final static String AUTHOR_ID = "authorIdentifier";
    public final static String AUTHOR_ID_SCHEME = "authorIdentifierScheme";
    public final static String AUTHOR_NAME = "authorName";

    //Contributor Field Names
    public final static String CONTRIB = "contributor";
    public final static String CONTRIB_NAME = "contributorName";
    public final static String CONTRIB_TYPE = "contributorType";

    //Dataset Contact Field Names
    public final static String DS_CONTACT = "datasetContact";
    public final static String DS_CONTACT_AFFIL = "datasetContactAffiliation";
    public final static String DS_CONTACT_EMAIL = "datasetContactEmail";
    public final static String DS_CONTACT_NAME = "datasetContactName";

    //Date of Collection Field Names
    public final static String DATE_OF_COLLECT_START = "dateOfCollectionStart";
    public final static String DATE_OF_COLLECT_END = "dateOfCollectionEnd";

    //Description Field Names
    public final static String DS_DESCRIPT = "dsDescription";
    public final static String DS_DESCRIPT_DATE = "dsDescriptionDate";
    public final static String DS_DESCRIPT_VAL = "dsDescriptionValue";

    //Distribution Field Names
    public final static String DISTRIB = "distributor";
    public final static String DISTRIB_ABRIV = "distributorAbbreviation";
    public final static String DISTRIB_AFFIL = "distributorAffiliation";
    public final static String DISTRIB_LOGO_URL = "distributorLogoURL";
    public final static String DISTRIB_NAME = "distributorName";
    public final static String DISTRIB_URL = "distributorURL";

    //Grant Number Field Names
    public final static String GRANT_NUM = "grantNumber";
    public final static String GRANT_NUM_AGENCY = "grantNumberAgency";
    public final static String GRANT_NUM_VAL = "grantNumberValue";

    //Keyword Field Names
    public final static String KEYWORD = "keyword";
    public final static String KEYWORD_VAL = "keywordValue";
    public final static String KEYWORD_VOCAB = "keywordVocabulary";
    public final static String KEYWORD_VOCAB_URL = "keywordVocabularyURL";

    //OtherLocation ID Field Names
    public final static String OTHER_ID = "otherId";
    public final static String OTHER_ID_AGENCY = "otherIdAgency";
    public final static String OTHER_ID_VAL = "otherIdValue";

    //Producer Field Names
    public final static String PRODUCER = "producer";
    public final static String PROD_ABBREV = "producerAbbreviation";
    public final static String PROD_AFFIL = "producerAffiliation";
    public final static String PROD_LOGO_URL = "producerLogoURL";
    public final static String PROD_NAME = "producerName";
    public final static String PROD_URL = "producerURL";

    //Related Publication Field Names
    public final static String PUBLICATION = "publication";
    public final static String PUB_CITE = "publicationCitation";
    public final static String PUB_ID_NUM = "publicationIDNumber";
    public final static String PUB_ID_TYPE = "publicationIDType";
    public final static String PUB_URL = "publicationURL";

    //Series Field Names
    public final static String SERIES = "series";
    public final static String SERIES_INFO = "seriesInformation";
    public final static String SERIES_NAME = "seriesName";

    //Software Field Names
    public final static String SOFTWARE = "software";
    public final static String SOFTWARE_NAME = "softwareName";
    public final static String SOFTWARE_VERSION = "softwareVersion";

    //Time Period Covered Field Names
    public final static String TIME_PER_COV = "timePeriodCovered";
    public final static String TIME_PER_COV_START = "timePeriodCoveredStart";
    public final static String TIME_PER_COV_END = "timePeriodCoveredEnd";

    //Topic Classification Field Names
    public final static String TOPIC_CLASS = "topicClassification";
    public final static String TOPIC_CLASS_VAL = "topicClassValue";
    public final static String TOPIC_CLASS_VOCAB = "topicClassVocab";
    public final static String TOPIC_CLASS_VOCAB_URL = "topicClassVocabURL";

    //Geographic
    //Geospatial Metadata Fields
    public final static String GEOGRAPHIC_COVERAGE = "geographicCoverage";
    public final static String GEOGRAPHIC_BBOX = "geographicBoundingBox";
    public final static String GEOGRAPHIC_UNIT = "geographicUnit";
    public final static String GEOSPATIAL = "geospatial";

    //Geospatial Bounding Box Field Names
    public final static String WEST_LONG = "westLongitude";
    public final static String EAST_LONG = "eastLongitude";
    public final static String NORTH_LAT = "northLatitude";
    public final static String SOUTH_LAT = "southLatitude";
    public final static String NORTH_LAT_LONG = "northLongitude";
    public final static String SOUTH_LAT_LONG = "southLongitude";

    //Geographic Coverage Field Names
    public final static String COUNTRY = "country";
    public final static String STATE = "state";
    public final static String CITY = "city";
    public final static String OTHER_GEO_COV = "otherGeographicCoverage";

    public final static List<String> GEO_META_FIELDS = Arrays.asList(GEOGRAPHIC_COVERAGE, GEOGRAPHIC_BBOX, GEOGRAPHIC_UNIT, WEST_LONG, EAST_LONG, NORTH_LAT, NORTH_LAT_LONG, SOUTH_LAT, SOUTH_LAT_LONG, COUNTRY, STATE, CITY, OTHER_GEO_COV);
}
