package TestFiles;

import Dataverse.FRDRAPI;

public class DatasetProcessTest implements Test{



    @Override
    public void run() {
        FRDRAPI frdrapi = new FRDRAPI();
        frdrapi.callFRDRHarvester(true);
    }
}
