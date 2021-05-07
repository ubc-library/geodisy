## Frequently Asked Questions

### How do I search with the map in Geodisy?

1. Move the map to display your area of interest
	* Use the + and - buttons in the top left of the map to zoom in and out  
	* Use your mouse’s scroll wheel to zoom in and out    
	* Click and drag within the map  
	* Hold the shift key and click to draw a box for the map to zoom toward  
	* On a mobile device, use two fingers to zoom and drag within the map  

2. Click **Search here** to see the results	 

The “Relevance” of the search results is determined by bounding box size. Data with bounding boxes that most closely fit the area you are searching will be boosted, while bounding boxes that are far larger or smaller than the pictured area will be suppressed.

### What does the  _undetermined_  data type mean when I see results in Geodisy?

_Geodisy_ harvests all kinds of data, and not all of those data types are explicitly geospatial. _Undetermined_ can have multiple meanings:

* The data in question is related to place, but is not a traditional geospatial file. Examples include:

	* Statistical data about a place, such as a survey of residents of Vancouver  
	* Weather data such as a comma separated value file of weather information for a province. Although the data is about a place, it is a CSV file without, say, point geospatial coordinates  
	
* The data set _is_ geospatial, but the detection systems in Geodisy were not able to classify by type. Geospatial data comes in a huge variety of formats, and not all of them are common or detectable by all software. Examples include:  

	* Data in older formats, such as Esri .e00 interchange formats  
	* Uncommon spatial data types, such as Spatialite databases  

This does not mean that data sets will not work with a geographic information system, only that they are not downloadable as shapefiles or raster data from the _Geodisy_ interface. They are still downloadable from the original source repository, by following the **More details at** link

### What kind of data is being searched in Geodisy?

Although _Geodisy_ is a geospatial search tool, it searches for any type of data which has a geographic component in the _description_ of the data. This means that it's possible to find data **about** a place. A map-based search has traditionally returned only items that can be viewed in an application specifically for mapping, but Geodisy returns all types of data. Searching for data in Nigeria will return survey data **about** Nigeria, even though the data set may not contain latitude/longitude points or imagery.

### Does Geodisy contain restricted data?

Geodisy only uses datasets that are open access. Datasets with any kind of restrictions are not included. Because Dataverse does not provide functionality for embargoed data, datasets with embargo restrictions are also not included. 

### What is the ISO 19139 metadata link?

All items appearing in _Geodisy_ will have [ISO 19115](https://www.iso.org/standard/53798.html) geospatial metadata created when added to Geodisy that is available in [ISO 19139](https://www.iso.org/standard/32557.html) XML format. ISO 19115 is an international standard used for describing geographic data. This ensures that:

* all metadata appearing in _Geodisy_ will have a consistent, standardized schema  
* Geographic information not present in the source record but discovered by _Geodisy_ will be added to metadata in consistent fashion  
* Metadata will be readable and usable by modern geographic information systems  

### Where can I find links to guides about Geodisy and how it works?

Documentation for Geodisy can be found on its home page: <https://github.com/ubc-library/geodisy/blob/master/Documentation/>

Brief user guides are available for:

* Adding metadata to records so that they're easily discoverable by Geodisy: [Depositor guide](https://github.com/ubc-library/geodisy/blob/master/Documentation/userguides/GeodisyDepositorGuide.md)  
* Connecting your Dataverse repository to Geodisy at Canada's [Federated Research Data Repository](https://geo.frdr-dfdr.ca): [Institutional guide](https://github.com/ubc-library/geodisy/blob/master/Documentation/userguides/GeodisyInstitutionalGuide.md)

### How do Dataverse records become Geodisy records?

Researchers need only deposit their data into a [Dataverse](https://dataverse.org) repository with a connection to an instance of Geodisy. If the deposit contains appropriate information in the record _or_ in the associated files:

* The record will be harvested by _Geodisy_  
* Any geographic information found in the study record and associated files will be automatically harvested  
* A unique record for each file containing data will be created in _Geodisy_, and will have a geographic area created for it based on its description or file content
* in the case of multiple files with data, multiple _Geodisy_ records will be created for each appropriate piece of data, and related files shown
* ISO 19115 standardized metadata will be created  
* Links to the source repository will be created  
* Bounding boxes for data being harvested will be generated and made searchable by the interface  
* Download links will be generated for valid file types  

		


 
