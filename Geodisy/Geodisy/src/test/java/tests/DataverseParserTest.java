package tests;

import Crosswalking.JSONParsing.DataverseParser;
import Dataverse.DataverseJSONFieldClasses.Fields.CompoundField.Author;
import Dataverse.DataverseJSONFieldClasses.Fields.CompoundField.DatasetContact;
import Dataverse.DataverseJSONFieldClasses.Fields.CompoundField.Description;
import Dataverse.DataverseJSONFieldClasses.Fields.SimpleJSONFields.SimpleFields;
import Dataverse.DataverseJavaObject;

import org.junit.Test;


import java.io.IOException;

import static Dataverse.DataverseJSONFieldClasses.DVFieldNames.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataverseParserTest {

    @Test
    public void testDataverseParser() throws IOException {
        String jsonData = "{\"status\":\"OK\",\"data\":{\"id\":900,\"identifier\":\"FK2/OVQBMK\",\"persistentUrl\":\"https://doi.org/10.5072/FK2/OVQBMK\",\"protocol\":\"doi\",\"authority\":\"10.5072\",\"publisher\":\"Demo Dataverse\",\"publicationDate\":\"2015-07-13\",\"storageIdentifier\":\"file://10.5072/FK2/OVQBMK\",\"latestVersion\":{\"id\":302,\"storageIdentifier\":\"file://10.5072/FK2/OVQBMK\",\"versionNumber\":1,\"versionMinorNumber\":0,\"versionState\":\"RELEASED\",\"productionDate\":\"Production Date\",\"lastUpdateTime\":\"2015-07-13T11:02:21Z\",\"releaseTime\":\"2015-07-13T11:02:21Z\",\"createTime\":\"2015-07-13T10:59:33Z\",\"license\":\"CC0\",\"termsOfUse\":\"CC0 Waiver\",\"metadataBlocks\":{\"citation\":{\"displayName\":\"Citation Metadata\",\"fields\":[{\"typeName\":\"title\",\"multiple\":false,\"typeClass\":\"primitive\",\"value\":\"Shapefile Dataset\"},{\"typeName\":\"author\",\"multiple\":true,\"typeClass\":\"compound\",\"value\":[{\"authorName\":{\"typeName\":\"authorName\",\"multiple\":false,\"typeClass\":\"primitive\",\"value\":\"Quigley, Elizabeth\"},\"authorAffiliation\":{\"typeName\":\"authorAffiliation\",\"multiple\":false,\"typeClass\":\"primitive\",\"value\":\"Harvard University\"}}]},{\"typeName\":\"datasetContact\",\"multiple\":true,\"typeClass\":\"compound\",\"value\":[{\"datasetContactName\":{\"typeName\":\"datasetContactName\",\"multiple\":false,\"typeClass\":\"primitive\",\"value\":\"Quigley, Elizabeth\"},\"datasetContactAffiliation\":{\"typeName\":\"datasetContactAffiliation\",\"multiple\":false,\"typeClass\":\"primitive\",\"value\":\"Harvard University\"},\"datasetContactEmail\":{\"typeName\":\"datasetContactEmail\",\"multiple\":false,\"typeClass\":\"primitive\",\"value\":\"equigley@iq.harvard.edu\"}}]},{\"typeName\":\"dsDescription\",\"multiple\":true,\"typeClass\":\"compound\",\"value\":[{\"dsDescriptionValue\":{\"typeName\":\"dsDescriptionValue\",\"multiple\":false,\"typeClass\":\"primitive\",\"value\":\"Dataset for shapefile\"}}]},{\"typeName\":\"subject\",\"multiple\":true,\"typeClass\":\"controlledVocabulary\",\"value\":[\"Earth and Environmental Sciences\"]},{\"typeName\":\"depositor\",\"multiple\":false,\"typeClass\":\"primitive\",\"value\":\"Quigley, Elizabeth\"},{\"typeName\":\"dateOfDeposit\",\"multiple\":false,\"typeClass\":\"primitive\",\"value\":\"2015-07-13\"}]}},\"files\":[{\"label\":\"shapefiletest.zip\",\"restricted\":false,\"version\":1,\"datasetVersionId\":302,\"dataFile\":{\"id\":901,\"persistentId\":\"\",\"pidURL\":\"\",\"filename\":\"shapefiletest.zip\",\"contentType\":\"application/zipped-shapefile\",\"filesize\":141004,\"storageIdentifier\":\"14e87ee7213-e642881106e3\",\"rootDataFileId\":-1,\"md5\":\"d1086c3d83d0f3e1d5af91a692493317\",\"checksum\":{\"type\":\"MD5\",\"value\":\"d1086c3d83d0f3e1d5af91a692493317\"}}}]}}}";
        DataverseJavaObject dataverseJavaObject = new DataverseJavaObject();
        dataverseJavaObject = getValues(dataverseJavaObject);

        //create ObjectMapper instance
        DataverseParser dataverseParser = new DataverseParser(jsonData);

        //TODO parse isn't parsing correctly
        assertEquals(dataverseParser.getdJO(), dataverseJavaObject);

    }

    private DataverseJavaObject getValues(DataverseJavaObject dataverseJavaObject) {
        SimpleFields sf = new SimpleFields();
        sf.setField(ALT_URL,"https://doi.org/10.5072/FK2/OVQBMK");
        sf.setField(PUBLISHER,"Demo Dataverse");
        sf.setField(PUB_DATE,"2015-07-13");
        sf.setField(PROD_DATE,"Production Date");
        sf.setField(DIST_DATE,"2015-07-13T11:02:21Z");
        sf.setField(DEPOS_DATE,"2015-07-13T10:59:33Z");
        sf.setField(LICENSE,"CC0");
        sf.setField(TITLE,"Shapefile Dataset");
        sf.setField(DEPOSITOR,"Quigley, Elizabeth");
        sf.setField(DEPOS_DATE,"2015-07-13");
        dataverseJavaObject.setSimpleFields(sf);
        Author a = new Author();
        a.setAuthorName("Quigley, Elizabeth");
        a.setAuthorAffiliation("Harvard University");
        dataverseJavaObject.addAuthor(a);
        DatasetContact dc = new DatasetContact();
        dc.setDatasetContactName("Quigley, Elizabeth");
        dc.setDatasetContactAffiliation("Harvard University");
        dc.setDatasetContactEmail("equigley@iq.harvard.edu");
        dataverseJavaObject.addDatasetContact(dc);
        Description d = new Description();
        d.setDsDescriptionValue("Dataset for shapefile");
        dataverseJavaObject.addDsDescription(d);
        dataverseJavaObject.addSubject("Earth and Environmental Sciences");
        return dataverseJavaObject;
    }

}
