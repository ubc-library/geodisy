package Dataverse;

import java.util.Arrays;
import java.util.List;

/**
 * Central location for all the dataverse json file names. If any field name changes we only have to alter it here
 * rather than search through all the code to find all the usages of the old field name.
 */
public class DVFieldNameStrings {

    //General Field Names
    public final static String TYPE_NAME = "typeName";
    public final static String VAL = "value";
    public final static String DV_ID = "entity_id";
    public final static String FIELDS = "fields";
    public final static String CITATION = "citation";

    public final static String PERSISTENT_ID = "persistentId";
    public final static String RECORD_URL = "persistentUrl";
    public final static String FILE_URL = "pidURL";
    public final static String MAJOR_VERSION = "versionNumber";
    public final static String MINOR_VERSION = "versionMinorNumber";
    public final static String AUTHORITY = "authority";
    public final static String IDENTIFIER = "identifier";
    public final static String PROTOCOL = "protocol";

    public final static String DV_FILE_ACCESS_PATH = "api/access/datafile/";


    //Simple Field Names
    public final static String ACCESS_TO_SOURCES = "accessToSources";
    public final static String ALT_TITLE = "alternativeTitle";
    public final static String ALT_URL = "alternativeURL";
    public final static String CHAR_OF_SOURCES = "characteristicOfSources";
    public final static String DEPOS_DATE = "dateOfDeposit";
    public final static String DEPOSITOR = "depositor";
    public final static String DIST_DATE = "distributionDate";
    public final static String NOTES_TEXT = "notesText";
    public final static String ORIG_OF_SOURCES = "originOfSources";
    public final static String PROD_DATE = "productionDate";
    public final static String PROD_PLACE = "productionPlace";
    public final static String PUB_DATE = "publicationDate";
    public final static String PUBLISHER = "publisher";
    public final static String TITLE = "title";
    public final static String SUBTITLE = "subtitle";

    //Terms of Use and Access______________________________________________________________________
    public final static String LICENSE = "license";
    public final static String TERMS_OF_USE = "termsOfUse";
    public final static String CONFID_DEC = "confidentialityDeclaration";
    public final static String AVALIB_STATUS = "availabilityStatus";
    public final static String SPECIAL_PERMS = "specialPermissions";
    public final static String RESTRICTIONS = "restrictions";
    public final static String CITATION_REQUIREMENTS = "citationRequirements";
    public final static String DEPOSIT_REQUIREMENTS = "depositorRequirements";
    public final static String CONDITIONS = "conditions";
    public final static String DISCLAIMER = "disclaimer";
    public final static String TERMS_OF_ACCESS = "termsOfAccess";
    public final static String DATA_ACC_PLACE = "dataAccessPlace";
    public final static String ORIG_ARCHIVE = "originalArchive";
    public final static String CONTACT_FOR_ACCESS = "contactForAccess";
    public final static String SIZE_OF_COLLECTION = "sizeOfCollection";
    public final static String STUDY_COMPLETION = "studyCompletion";
    public final static List<String> TERMS_AND_ACCESS_NAME = Arrays.asList(TERMS_OF_USE,CONFID_DEC,AVALIB_STATUS,SPECIAL_PERMS,RESTRICTIONS,CITATION_REQUIREMENTS,DEPOSIT_REQUIREMENTS,CONDITIONS,DISCLAIMER,TERMS_OF_ACCESS,DATA_ACC_PLACE,ORIG_ARCHIVE,CONTACT_FOR_ACCESS,SIZE_OF_COLLECTION,STUDY_COMPLETION);
    public final static List<String> SIMPLE_FIELD_NAMES = Arrays.asList(ACCESS_TO_SOURCES,ALT_TITLE,ALT_URL,CHAR_OF_SOURCES,DEPOS_DATE,DEPOSITOR,DIST_DATE,LICENSE,NOTES_TEXT,ORIG_OF_SOURCES,PROD_DATE,PROD_PLACE,PUB_DATE,PUBLISHER,TITLE,SUBTITLE, AUTHORITY, IDENTIFIER);

    //String List Field Names
    public final static String RELATED_MATERIAL = "relatedMaterial";
    public final static String RELATED_DATASETS = "relatedDatasets";
    public final static String OTHER_REFERENCES = "otherReferences";
    public final static String DATA_SOURCES = "dataSources";
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
    public final static String DATE_OF_COLLECT = "dateOfCollection";
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
    public final static String KEYWORD_VOCAB_URI = "keywordVocabularyURI";

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
    public final static String TOPIC_CLASS_VOCAB_URI = "topicClassVocabURI";

    //Geographic___________________________________________________________________________________________
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
    public final static String FILE_NAME = "fileName";
    public final static String GEOMETRY = "geometryType";
    public final static String PROJECTION = "projection";
    public final static String GEOSERVER_LABEL = "geoserverLabel";
    public final static String POLYGON = "Polygon";


    //Geographic Coverage Field Names
    public final static String COUNTRY = "country";
    public final static String PROVINCE = "province";
    public final static String STATE = "state";
    public final static String CITY = "city";
    public final static String GIVEN_COUNTRY = "givenCountry";
    public final static String GIVEN_PROVINCE = "givenProvince";
    public final static String GIVEN_CITY = "givenCity";
    public final static String COMMON_COUNTRY = "commonCountry";
    public final static String COMMON_PROVINCE = "commonProvice";
    public final static String COMMON_CITY = "commonCity";
    public final static String OTHER_GEO_COV = "otherGeographicCoverage";
    public final static List<String> GEO_META_FIELDS = Arrays.asList(GEOGRAPHIC_COVERAGE, GEOGRAPHIC_BBOX, GEOGRAPHIC_UNIT, WEST_LONG, EAST_LONG, NORTH_LAT, NORTH_LAT_LONG, SOUTH_LAT, SOUTH_LAT_LONG, COUNTRY, PROVINCE, CITY, OTHER_GEO_COV);

    //Astrophysics_________________________________________________________________________________
    // Spatial Field Names
    public final static String SPATIAL_FIELDS = "spatial";
    public final static String SPATIAL_RESOLUTION = "resolution.Spatial";
    public final static String SPATIAL_COVERAGE = "coverage.Spatial";

    //Spectral Field Names
    public final static String SPECTRAL_FIELDS = "spectral";
    public final static String SPECTRAL_RESOLUTION = "resolution.Spectral";
    public final static String SPECTRAL_BANDPASS_COVERAGE = "coverage.Spectral.Bandpass";
    public final static String SPECTRAL_CENTRAL_WAVELENGTH_COVERAGE = "coverage.Spectral.CentralWavelength";
    public final static String SPECTRAL_WAVELENGTH_COVERAGE = "coverage.Spectral.Wavelength";
    public final static String SPECTRAL_MIN_WAVELENGTH_COVERAGE = "coverage.Spectral.MinimumWavelength";
    public final static String SPECTRAL_MAX_WAVELENGTH_COVERAGE = "coverage.Spectral.MaximumWavelength";

    //Temporal Field Names
    public final static String TEMPORAL_FIELDS = "temporal";
    public final static String TEMPORAL_RESOLUTION = "resolution.Temporal";
    public final static String TEMPORAL_COVERAGE = "coverage.Temporal";
    public final static String TEMPORAL_COVERAGE_START_TIME = "coverage.Temporal.StartTime";
    public final static String TEMPORAL_COVERAGE_STOP_TIME = "coverage.Temporal.StopTime";

    //Redshift Field Names
    public final static String REDSHIFT_FIELDS = "redshift";
    public final static String REDSHIFT_TYPE = "redshiftType";
    public final static String REDSHIFT_RESOLUTION = "resolution.Redshift";
    public final static String REDSHIFT_VALUE_COVERAGE = "coverage.RedshiftValue";
    public final static String REDSHIFT_MINIMUM_VALUE_COVERAGE = "coverage.Redshift.MinimumValue";
    public final static String REDSHIFT_MAXIMUM_VALUE_COVERAGE = "coverage.Redshift.MaximumValue";

    //Simple Astrophysics Field Names
    public final static String ASTRO_TYPE = "astroType";
    public final static String ASTRO_FACILITY = "astroFacility";
    public final static String ASTRO_INSTRUMENT = "astroInstrument";
    public final static String ASTRO_OBJECT = "astroObject";
    public final static String DEPTH_COVERAGE = "coverage.Depth";
    public final static String OBJECT_DENSITY_COVERAGE = "coverage.ObjectDensity";
    public final static String OBJECT_COUNT_COVERAGE = "coverage.ObjectCount";
    public final static String SKY_FRACTION_COVERAGE = "coverage.SkyFraction";
    public final static String POLARIZATION_COVERAGE = "coverage.Polarization";

    //Journals_________________________________________________________________________________________
    public final static String JOURNAL_FIELDS = "journal";
    public final static String JOURNAL_VOLUME_ISSUE = "journalVolumeIssue";
    public final static String JOURNAL_VOLUME = "journalVolume";
    public final static String JOURNAL_ISSUE = "journalIssue";
    public final static String JOURNAL_PUB_DATE = "journalPubDate";
    public final static String JOURNAL_ARTICLE_TYPE = "journalArticleType";

    //Life Sciences_____________________________________________________________________________________
    public final static String STUDY_DESIGN_TYPE = "studyDesignType";
    public final static String STUDY_FACTOR_TYPE = "studyFactorType";
    public final static String STUDY_ASSAY_ORGANISM = "studyAssayOrganism";
    public final static String STUDY_ASSAY_OTHER_ORGANISM = "studyAssayOtherOrganism";
    public final static String STUDY_ASSAY_MEASUREMENT_TYPE = "studyAssayMeasurementType";
    public final static String STUDY_ASSAY_OTHER_MEASUREMENT_TYPE = "studyAssayOtherMeasurementType";
    public final static String STUDY_ASSAY_TECHNOLOGY_TYPE = "studyAssayTechnologyType";
    public final static String STUDY_ASSAY_PLATFORM = "studyAssayPlatform";
    public final static String STUDY_ASSAY_CELL_TYPE = "studyAssayCellType";

    //Social__________________________________________________________________________________________
    public final static String UNIT_OF_ANALYSIS = "unitOfAnalysis";
    public final static String UNIVERSE = "universe";
    public final static String TIME_METHOD = "timeMethod";
    public final static String DATA_COLLECTOR = "dataCollector";
    public final static String COLLECTOR_TRAINING = "collectorTraining";
    public final static String FREQUENCY_OF_DATA_COLLECTION = "frequencyOfDataCollection";
    public final static String SAMPLING_PROCEDURE = "samplingProcedure";
    public final static String TARGET_SAMPLE_SIZE_FIELDS = "targetSampleSize";
    public final static String TARGET_SAMPLE_ACTUAL_SIZE = "targetSampleActualSize";
    public final static String TARGET_SAMPLE_SIZE_FORMULA = "targetSampleSizeFormula";
    public final static String DEVIATIONS_FROM_SAMPLE_DESIGN = "deviationsFromSampleDesign";
    public final static String COLLECTION_MODE = "collectionMode";
    public final static String RESEARCH_INSTRUMENT = "researchInstrument";
    public final static String DATA_COLLECTION_SITUATION = "dataCollectionSituation";
    public final static String ACTIONS_TO_MINIMIZE_LOSS = "actionsToMinimizeLoss";
    public final static String CONTROL_OPERATIONS = "controlOperations";
    public final static String WEIGHTING = "weighting";
    public final static String CLEANING_OPERATIONS = "cleaningOperations";
    public final static String DATASET_LEVEL_ERROR_NOTES = "datasetLevelErrorNotes";
    public final static String RESPONSE_RATE = "responseRate";
    public final static String SAMPLING_ERROR_ESTIMATES = "samplingErrorEstimates";
    public final static String OTHER_DATA_APPRAISAL = "otherDataAppraisal";
    public final static String SOCIAL_SCIENCE_NOTES_FIELDS = "socialScienceNotes";
    public final static String SOCIAL_SCIENCE_NOTES_TYPE = "socialScienceNotesType";
    public final static String SOCIAL_SCIENCE_NOTES_SUBJECT = "socialScienceNotesSubject";
    public final static String SOCIAL_SCIENCE_NOTES_TEXT = "socialScienceNotesText";
    public final static List<String> SOCIAL_SIENCE_NOTES_ALL_FIELDS = Arrays.asList(SOCIAL_SCIENCE_NOTES_TEXT, SOCIAL_SCIENCE_NOTES_SUBJECT,SOCIAL_SCIENCE_NOTES_TEXT);
    public final static String SOCIAL_SCIENCE = "socialscience";
    public final static String FILES = "files";

}
