package TestFiles;

import Dataverse.ExistingDatasetBBoxes;
import Dataverse.ExistingLocations;
import Dataverse.FindingBoundingBoxes.GeonamesBBs;

public class ExistingBoundingBoxesTest implements Test{
    public void run() {
        ExistingLocations ebb = ExistingLocations.getExistingLocations();
        ExistingDatasetBBoxes existingDatasetBBoxes = ExistingDatasetBBoxes.getExistingHarvests();
        GeonamesBBs gbb = new GeonamesBBs("fake_doi");
        gbb.getDVBoundingBox("Canada");
        System.out.println("test");
    }
}
