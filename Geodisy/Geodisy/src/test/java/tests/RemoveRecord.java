package tests;

import BaseFiles.FileWriter;
import Dataverse.ExistingDatasetBBoxes;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;


import java.io.File;
import java.io.IOException;

import static _Strings.GeodisyStrings.*;

public class RemoveRecord {
    String record2Remove = "10.5072/FK2/HAQQUC";
    @Test
    public void removeRecord() {
        FileWriter fW = new FileWriter();
        fW.verifyFileExistence(EXISTING_DATASET_BBOXES);
        ExistingDatasetBBoxes existingDatasetBBoxes = ExistingDatasetBBoxes.getExistingHarvests();
        existingDatasetBBoxes.saveExistingSearchs(existingDatasetBBoxes.getbBoxes(), EXISTING_DATASET_BBOXES, "ExistingBBoxes");
        String folderized = record2Remove.replace(".","/");
        File file = new File(DATA_DIR_LOC + folderized);
        try {
            FileUtils.deleteDirectory(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        file = new File("metadata/" + folderized);
        try {
            FileUtils.deleteDirectory(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}