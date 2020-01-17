package com.company;

public class HTTPCallerGeoNames extends HTTPCaller {

    public HTTPCallerGeoNames() {
        logger = new GeoLogger(this.getClass());
    }

    @Override
    protected void ioError(){
        logger.error("Something went wrong getting a bounding box from Geonames");
    }
}