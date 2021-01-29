package BaseFiles;

import Dataverse.ExistingDatasetBBoxes;
import _Strings.GeodisyStrings;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static _Strings.GeodisyStrings.*;

public class RemoveRecord {
    String record2Remove = "insert record's path to folderize like '10.5072/FK2/GFCTVC'";
    public void removeRecord() {
    FileWriter fW = new FileWriter();
    fW.verifyFileExistence(EXISTING_DATASET_BBOXES);
    ExistingDatasetBBoxes existingDatasetBBoxes = ExistingDatasetBBoxes.getExistingHarvests();
    existingDatasetBBoxes.saveExistingSearchs(existingDatasetBBoxes.getbBoxes(), EXISTING_DATASET_BBOXES, "ExistingBBoxes");
    String folderized = GeodisyStrings.removeHTTPSAndReplaceAuthority(record2Remove).replace(".","/");
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
