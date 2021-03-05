## Help

### How do I search with the map in Geodisy?

1. Move the map to display your area of interest
    * Use the + and - buttons in the top left of the map to zoom in and out
    * Use your mouse’s scroll wheel to zoom in and out   
    * Click and drag within the map
    * Hold the shift key and click to draw a box for the map to zoom toward
    * On a mobile device, use two fingers to zoom and drag within the map

2. Click Search here to see the results

The “Relevance” of the search results is determined by bounding box size. Data with bounding boxes that most closely fit the area you are searching will be boosted, while bounding boxes that are far larger or smaller than the pictured area will be suppressed.

### What does the undetermined data type mean when I see results in Geodisy?

Geodisy harvests all kinds of data, and not all of those data types are explicitly geospatial. Undetermined can have multiple meanings:

* The data in question is related to place, but is not a traditional geospatial file. Examples include:

    * Statistical data about a place, such as a survey of residents of Vancouver
    * Weather data such as a comma separated value file of weather information for a province. Although the data is about a place, it is a CSV file without, say, point geospatial coordinates

* The data set is geospatial, but the detection systems in Geodisy were not able to classify by type. Geospatial data comes in a huge variety of formats, and not all of them are common or detectable by all software. Examples include:

    * Data in older formats, such as Esri .e00 interchange formats
    * Uncommon spatial data types, such as Spatialite databases

This does not mean that data sets will not work with a geographic information system, only that they are not downloadable as shapefiles or raster data from the Geodisy interface. They are still downloadable from the original source repository, by following the More details at link

### What kind of data is being searched in Geodisy?

Although Geodisy is a geospatial search tool, it searches for any type of data which has a geographic component in the description of the data. This means that it's possible to find data about a place. A map-based search has traditionally returned only items that can be viewed in an application specifically for mapping, but Geodisy returns all types of data. Searching for data in Nigeria will return survey data about Nigeria, even though the data set may not contain latitude/longitude points or imagery.

Currently in beta, the map search includes datasets from repositories indexed by FRDR with bounding box metadata. FRDR-indexed Dataverse repository datasets with location metadata and/or geospatial files are also included. FRDR’s Geodisy will continue to expand upon its collection to include more datasets from FRDR’s source list of institutional repositories

### Does Geodisy contain restricted data?

Geodisy only uses datasets that are open access. Datasets with any kind of restrictions are not included. Because Dataverse does not provide functionality for embargoed data, datasets with embargo restrictions are also not included.

### What is the ISO 19139 metadata link?

All items appearing in Geodisy will have ISO 19115 geospatial metadata created when added to Geodisy that is available in ISO 19139 XML format. ISO 19115 is an international standard used for describing geographic data. This ensures that:

* All metadata appearing in Geodisy will have a consistent, standardized schema
* Geographic information not present in the source record but discovered by Geodisy will be added to metadata in consistent fashion
* Metadata will be readable and usable by modern geographic information systems

### How do data repository records become Geodisy records?

Researchers need only deposit their data into a repository with a connection to FRDR. If the deposit contains appropriate information in the record or in the associated files:

* The record will be harvested by Geodisy
* Any geographic information found in the study record and associated files will be automatically harvested
* A unique record for each file containing data will be created in Geodisy, and will have a geographic area created for it based on its description or file content
* In the case of multiple files with data, a distinct Geodisy record will be created for each appropriate piece of data, and related files will be indicated
* ISO 19115 standardized metadata will be created
* Links to the source repository will be created
* Bounding boxes for data being harvested will be generated and made searchable by the interface
* Download links will be generated for valid file types

For Dataverse-specific guidance on adding metadata to records to make them discoverable by Geodisy, see our Dataverse depositor guide



