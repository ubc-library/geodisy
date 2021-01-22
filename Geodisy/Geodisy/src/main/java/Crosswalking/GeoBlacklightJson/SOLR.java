package Crosswalking.GeoBlacklightJson;

import BaseFiles.GeoLogger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

import java.io.IOException;

import static _Strings.GeodisyStrings.FRONTEND_ADDRESS;

public class SOLR {
    private String SOLRAddress = "https://" + FRONTEND_ADDRESS + ":8983/solr/geoblacklight-core";
    public SolrClient solrClient;
    GeoLogger logger = new GeoLogger(this.getClass());

    public SOLR() {
        solrClient = new HttpSolrClient.Builder(SOLRAddress).build();
    }

    public void clearIndex() throws SolrServerException,IOException{
            solrClient.deleteByQuery("*:*");
            solrClient.commit();
    }
}
