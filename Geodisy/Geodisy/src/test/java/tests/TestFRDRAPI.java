package tests;


import Dataverse.FRDRAPI;
import Dataverse.SourceJavaObject;
import org.junit.Test;

import java.util.LinkedList;

public class TestFRDRAPI {
    @Test
    public void getJSONRecords(){
        FRDRAPI f = new FRDRAPI();
        LinkedList<SourceJavaObject> djos = f.callFRDRHarvester(true);
        for(SourceJavaObject s: djos){
            System.out.println(s.getPID());
        }

    }
}
