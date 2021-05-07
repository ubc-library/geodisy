# Geodisy
#### The world of data at your fingertips

## What is Geodisy?
**Find research data visually, spatially and quickly**

Research data can be hard to find, and even harder if you're researching a specific *place*. **Geodisy** changes that, giving you a window into the world of research data with map-based tools familiar to everyone. Search by place name, or by drawing a box. The world of research data is yours to discover. **Geodisy** is the software that will let you do just that.

### Who will use Geodisy?
**People**  
Anyone looking for research data who wants to use a map or a box to find data. Researchers, students, journalists, and anyone else with an interest in data from university research will benefit from **Geodisy's** search tools.

**Institutions**  
The first users of **Geodisy** will be [FRDR](https://www.frdr.ca/repo/), Canada's Federated Research Data Repository, for the quick and easy discovery of Canadian Research Data. When released, *anyone* with the available infrastructure will be able to plug in **Geodisy** and make a compatible repository more discoverable.

### Why use Geodisy?
Data, and in particular research data, has always been difficult to find. Keywords can be hit and miss, and text based descriptions don't *show* you where your place of interest lies. **Geodisy** changes that, showing you *where*, not just *what*.

If you're a running a research data repository based on [Dataverse](https://dataverse.org), **Geodisy** will take your repository's data, search for geospatial metadata and files, and copy them to a new system which allows for visual searching. Your original data and search methods are untouched; you have the benefit of both.


### How does Geodisy work?
**Geodisy** is a separate server software component that examines the metadata, such as the study records for research data and any associated data. If **Geodisy** finds spatial data, metadata and data are harvested, then normalized to have the same geospatial metadata standard. Afterwards, both data and metadata are injected into a geospatial data server and a viewer/search component.

#### For the more technically inclined

**Geodisy** consists of middleware (a piece of software living on a server, not directly accessible to end users) that:

1. Harvests data and metadata from a repository (intially Dataverse)

2. Cleans and normalizes metadata and data found in study recods

3. Creates bounding boxes for data sets if applicable, and reads geometry from compatible files, such as shapefiles

4. Injects bounding box data and/ or geospatial data into a geospatial server, in this case [Open Geoserver](http://geoserver.org/)

5. Presents a visual search in the form of a [Geoblacklight](https://geoblacklight.org) front end

**Geodisy is open source**
All of the software you need will be free and open source (FOSS). The Geodisy middleware component will be available for download from [Github](https://github.com). In addition to Geodisy, you will need:

1. A [Dataverse](https://dataverse.org) repository to harvest from

2. [Open Geoserver](http://geoserver.org/) in which to place your data

3. [Geoblacklight](https://geoblacklight.org) to allow users to search

### When will it be available?
**Geodisy** is available now. See it in action at [geo.frdr-dfdr.ca](https://geo.frdr-dfdr.ca)

### Where can I find documentation?
**Geodisy** documentation is available in our [Github repository](https://github.com/ubc-library/geodisy/blob/master/Documentation/index.md)

### Where is the software?
Because **Geodisy** is an open source project, all of our software is freely available. Download or fork the software from [Github](https://github.com/ubc-library/geodisy/).

### Who is behind all of this?

#### None of this would be possible without our grant (RDM-059) from [CANARIE](https://www.canarie.ca/), to whom we extend our thanks.

|   |   |   |
|---|---|---|
|**Core Project Team (UBC)**| Eugene Barsky | Principal Investigator |
||Paul Dante | Software Developer |
||Edith Domingue| Advanced Research Computing (ARC) Client Services Manager|
||Mark Goodwin | Geospatial Metadata Coordinator|
||Tang Lee | Project Manager|
||Paul Lesack | Co-Principal Investigator|
||Evan Thornberry | Co-Principal Investigator|

|   |   |   |
|---|---|---|
|**Project Partners**|Jason Brodeur | McMaster University|
||Marcel Fortin | University of Toronto|
||Alex Garnett | SFU |
||Amber Leahey | Scholars Portal|
||Jason Hlady | University of Saskatchewan|
||Joel Farthing	| University of Saskatchewan|
||Venkat Mahadevan | UBC ARC|
||Todd Trann | University of Saskatchewan|
||Lee Wilson | Portage Network|


### I want to know more!
Look for us on [Twitter](https://twitter.com)! **#geodisy**

We are happy to chat. Contact the **Geodisy** team at [geodisy.info@ubc.ca](https://researchdata.library.ubc.ca)
