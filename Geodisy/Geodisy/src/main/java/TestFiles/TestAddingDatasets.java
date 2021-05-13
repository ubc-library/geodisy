package TestFiles;

import Dataverse.FRDRAPI;

public class TestAddingDatasets implements Test{
    @Override
    public void run() {
        FRDRAPI api = new FRDRAPI();
        api.callFRDRHarvester(true);
    }
}
