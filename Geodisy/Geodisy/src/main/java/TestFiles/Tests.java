package TestFiles;

public class Tests {

    public void runTests(){
        DatasetProcessTest t = new DatasetProcessTest();
        t.run();

        /*RepoNameNormalizationTest r = new RepoNameNormalizationTest();
        r.run();*/
        /*TestAddingDatasets t = new TestAddingDatasets();
        t.run();*/

        /*ThreadExitTest t = new ThreadExitTest();
        t.run();*/

        /*ExistingBoundingBoxesTest e = new ExistingBoundingBoxesTest();
        e.run();*/

        /*TabTest tt = new TabTest();
        tt.run();*/

       /*ZipTest zipTest = new ZipTest();
        zipTest.run();*/

        /*HTTPGetTest test = new HTTPGetTest();
        test.run();*/

        /*HTTPHeaderTest test = new HTTPHeaderTest();
        test.getHeader();*/

        /*ExistingLabelsTest exisitingLabelsTest = new ExistingLabelsTest();
        exisitingLabelsTest.run();*/

        //Testing uploading both Rasters and Vectors to Geoserver
        /*PostGIS postGIS = new PostGIS();
        DataverseJavaObject djo = new DataverseJavaObject("Fake");
        djo.setPID("https://hdl.handle.net/11272.1/AB2/OFSCDC");
        String geoserverLabel = "v0000000001";
        String fileName = "KelownaRoutes.shp";
        postGIS.addFile2PostGIS(djo,fileName,geoserverLabel);
        GeoServerAPI geoServerAPI = new GeoServerAPI(djo);
        //geoServerAPI.addPostGISLayerToGeoserver(geoserverLabel,fileName);
        djo.setPID("https://hdl.handle.net/11272.1/AB2/LSCA5E");
        geoServerAPI.addRaster("2009Hillshade.tif","r0000000001");*/

        /*System.out.println("Running a test on Raster Upload");
        GeoserverTest gt = new GeoserverTest();
        gt.testAddingARaster();*/
        /*gt.testAddingAVector();*/

        //testCombine(args);

        //Run the below solo to download
        /*DownloadRecord downloadRecord = new DownloadRecord();
        downloadRecord.run("doi:10.5072/FK2/KZRG9F");*/

        /*GDALTest gdalTest = new GDALTest();
        gdalTest.testUnzip();
        System.out.println("Finished unzipping");
        gdalTest.testTransform();*/
    }
}
