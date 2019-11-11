package Crosswalking.GeoBlacklightJson;

import BaseFiles.HTTPCaller;

public class GeoCombine {
    HTTPCaller caller;

    public GeoCombine() {
        caller = new HTTPCombineCaller();
    }

    public void index(){
        caller.callHTTP("bundle exec rake geocombine:index")
    }
}
