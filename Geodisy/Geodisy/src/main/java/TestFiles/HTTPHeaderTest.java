package TestFiles;

import Dataverse.HTTPCallerFiles;

public class HTTPHeaderTest {
    public void getHeader(){
        String urlString = "https://abacus.library.ubc.ca/api/access/datafile/64388";
        HTTPCallerFiles hCF = new HTTPCallerFiles();
        hCF.callHTTP(urlString);
    }
}
