package tests.DataverseTestCases;

import Dataverse.DownloadedFiles;
import org.junit.jupiter.api.Test;

public class SaveRecords {
    @Test
    public void createLogTest(){
        DownloadedFiles df = DownloadedFiles.getDownloadedFiles();
        df.addDownload("TestFile1","doi:101.1231","1");
        df.addDownload("TestFile2","handle:FR/1231","3");
        df.addDownload("TestFile3","doi:A1","2");
        df.saveDownloads();
    }

}
