package TestFiles;

import Dataverse.HTTPCallerFiles;

public class HTTPHeaderTest implements Test{
    public void run(){
        String urlString = "https://abacus.library.ubc.ca/api/access/datafile/64388";
        HTTPCallerFiles hCF = new HTTPCallerFiles();
        System.out.println(hCF.getFileLength(urlString));
        urlString = "https://data.winnipeg.ca/resource/tnhm-5bxt.csv";
        System.out.println(hCF.getFileLength(urlString));
    }
}
