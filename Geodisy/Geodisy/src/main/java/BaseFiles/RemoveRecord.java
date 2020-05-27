package BaseFiles;

import Dataverse.ExistingHarvests;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static Strings.GeodisyStrings.*;

public class RemoveRecord {
    String record2Remove = "10.5072/FK2/GFCTVC";
    public void removeRecord() {
    FileWriter fW = new FileWriter();
        fW.verifyFileExistence(EXISTING_BBOXES);
        fW.verifyFileExistence(EXISTING_RECORDS);
    ExistingHarvests existingHarvests = ExistingHarvests.getExistingHarvests();
        existingHarvests.deleteRecord(record2Remove);
        existingHarvests.saveExistingSearchs(existingHarvests.getRecordVersions(),EXISTING_RECORDS, "ExistingRecords");
        existingHarvests.saveExistingSearchs(existingHarvests.getbBoxes(),EXISTING_BBOXES, "ExistingBBoxes");
    String folderized = record2Remove.replace(".","/");
    File file = new File(DATA_DIR_LOC + folderized);
        try {
        FileUtils.deleteDirectory(file);
    } catch (
    IOException e) {
        e.printStackTrace();
    }
    file = new File("XMLFiles/" + folderized);
        try {
        FileUtils.deleteDirectory(file);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}
